package Capstone_team1.Jubging.service;

import Capstone_team1.Jubging.config.exception.ConflictException;
import Capstone_team1.Jubging.config.exception.ErrorCode;
import Capstone_team1.Jubging.config.exception.NotFoundException;
import Capstone_team1.Jubging.config.exception.UnauthorizedException;
import Capstone_team1.Jubging.config.jwt.JwtTokenProvider;
import Capstone_team1.Jubging.config.utils.SecurityUtil;
import Capstone_team1.Jubging.config.validation.PasswordCorrectValidator;
import Capstone_team1.Jubging.config.validation.UserStateValidator;
import Capstone_team1.Jubging.config.validation.ValidatorBucket;
import Capstone_team1.Jubging.domain.User;
import Capstone_team1.Jubging.dto.auth.UserRequestUpdatePasswordDto;
import Capstone_team1.Jubging.dto.auth.UserResponseDto;
import Capstone_team1.Jubging.dto.auth.UserSignInRequestDto;
import Capstone_team1.Jubging.dto.jwt.JwtTokenDto;
import Capstone_team1.Jubging.infrastructure.MailSender;
import Capstone_team1.Jubging.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final RedisTemplate<String, Object> redisTemplate;

    private final MailSender mailSender;

    @Override
    @Transactional(readOnly = true)
    public JwtTokenDto signIn(UserSignInRequestDto userSignInRequestDto)
    {
        User user = this.userRepository.findByEmail(userSignInRequestDto.getEmail()).orElseThrow(
                () -> new UnauthorizedException(
                        ErrorCode.INVALID_SIGNIN,
                        "잘못된 이메일 입니다."
                )
        );

        /* Validate the input password and user state
         * The sign-in process is rejected if the user is in BLOCKED, WAIT, or INACTIVE state.
         */
        ValidatorBucket.of()
                .consistOf(PasswordCorrectValidator.of(
                        this.passwordEncoder,
                        user.getPassword(),
                        userSignInRequestDto.getPassword()))
                .consistOf(UserStateValidator.of(user.getState()))
                .validate();


        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = userSignInRequestDto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtTokenDto jwtTokenDto = jwtTokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken redis에 저장
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(),
                        jwtTokenDto.getRefreshToken(),
                        jwtTokenDto.getRefreshTokenExpiresIn(),
                        TimeUnit.MILLISECONDS);

        // 5. 토큰 발급
        return jwtTokenDto;
    }
    @Transactional
    @Override
    public UserResponseDto updatePassword(UserRequestUpdatePasswordDto updatePasswordDto) {
        User findUser = this.userRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER, "로그인 유저 정보가 없습니다."));
        ValidatorBucket.of()
                        .consistOf(PasswordCorrectValidator.of(
                                passwordEncoder,
                                findUser.getPassword(),
                                updatePasswordDto.getOriginPassword()
                        ))
                    .validate();

        findUser.updatePassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));

        return this.userRepository.update(findUser)
                .map(UserResponseDto::of)
                .orElseThrow(()-> new ConflictException(ErrorCode.UPDATE_FAIL, "비밀번호 업데이트를 실패했습니다."));
    }
}
