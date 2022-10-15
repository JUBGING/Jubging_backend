package Capstone_team1.Jubging.service;

import Capstone_team1.Jubging.config.exception.BadRequestException;
import Capstone_team1.Jubging.config.exception.ConflictException;
import Capstone_team1.Jubging.config.exception.ErrorCode;
import Capstone_team1.Jubging.config.exception.NotFoundException;
import Capstone_team1.Jubging.config.exception.UnauthorizedException;
import Capstone_team1.Jubging.config.jwt.JwtTokenProvider;
import Capstone_team1.Jubging.config.utils.SecurityUtil;
import Capstone_team1.Jubging.config.validation.EnumUserStateTypeCorrectValidator;
import Capstone_team1.Jubging.config.validation.PasswordCorrectValidator;
import Capstone_team1.Jubging.config.validation.UserStateValidator;
import Capstone_team1.Jubging.config.validation.ValidatorBucket;
import Capstone_team1.Jubging.domain.Points;
import Capstone_team1.Jubging.domain.User;
import Capstone_team1.Jubging.domain.model.Role;
import Capstone_team1.Jubging.domain.model.UserState;
import Capstone_team1.Jubging.dto.auth.UserRequestSignUpDto;
import Capstone_team1.Jubging.dto.auth.UserRequestUpdatePasswordDto;
import Capstone_team1.Jubging.dto.auth.UserResponseDto;
import Capstone_team1.Jubging.dto.auth.UserSignInRequestDto;
import Capstone_team1.Jubging.dto.auth.UserStatusChangeRequestDto;
import Capstone_team1.Jubging.dto.jwt.JwtTokenDto;
import Capstone_team1.Jubging.dto.jwt.JwtTokenRequestLogoutDto;
import Capstone_team1.Jubging.dto.jwt.JwtTokenRequestReissueDto;
import Capstone_team1.Jubging.repository.PointsRepository;
import Capstone_team1.Jubging.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final PointsRepository pointsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final RedisTemplate<String, Object> redisTemplate;


    @Override
    @Transactional(readOnly = true)
    public JwtTokenDto signIn(UserSignInRequestDto userSignInRequestDto)
    {
        User user = this.userRepository.findByEmail(userSignInRequestDto.getEmail()).orElseThrow(
                () -> new UnauthorizedException(
                        ErrorCode.INVALID_EMAIL,
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
    public UserResponseDto signUp(UserRequestSignUpDto userRequestSignUpDto) {

        this.userRepository.findByEmail(userRequestSignUpDto.getEmail()).ifPresent(
                email->{
                    throw new BadRequestException(
                            ErrorCode.DUPLICATE_EMAIL,
                            "중복되는 이메일 입니다."
                    );
                }
        );
        Points points = pointsRepository.create(Points.createPoints(0, 0));
        User newUser = User.createUser(
                userRequestSignUpDto.getEmail(),
                passwordEncoder.encode(userRequestSignUpDto.getPassword()),
                userRequestSignUpDto.getName(),
                Role.USER,
                UserState.ACTIVE,
                points
        );

        return UserResponseDto.of(this.userRepository.create(newUser));
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

    @Transactional
    @Override
    public JwtTokenDto reissue(JwtTokenRequestReissueDto jwtTokenRequestDto) {
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(jwtTokenRequestDto.getRefreshToken())) {
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_JWT, "Refresh Token이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(jwtTokenRequestDto.getAccessToken());

        // 3. Redis에서 Refresh Token 값 가져옴
        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());

        //로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
        if(ObjectUtils.isEmpty(refreshToken)) {
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_JWT, "잘못된 요청입니다.");
        }
        //refresh token이 있는 경우
        if(!refreshToken.equals(jwtTokenRequestDto.getRefreshToken()))
        {
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_JWT, "Refresh Token이 유효하지 않습니다.");
        }

        // 4. 새로운 토큰 생성
        JwtTokenDto jwtTokenDto = jwtTokenProvider.generateTokenDto(authentication);

        // 5. RefreshToken Redis 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(),
                        jwtTokenDto.getRefreshToken(),
                        jwtTokenDto.getRefreshTokenExpiresIn(),
                        TimeUnit.MILLISECONDS);

        // 토큰 발급
        return jwtTokenDto;
    }

    @Transactional
    @Override
    public String logout(JwtTokenRequestLogoutDto jwtTokenRequestLogoutDto)
    {
        // 1. Access Token 검증
        if (!jwtTokenProvider.validateToken(jwtTokenRequestLogoutDto.getAccessToken())) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_JWT, "잘못된 요청입니다.");
        }
        // 2. Access Token 에서 User의 name(id)을 가져온다.
        Authentication authentication = jwtTokenProvider.getAuthentication(jwtTokenRequestLogoutDto.getAccessToken());
        // 3. Redis 에서 해당 User의 name(email) 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + authentication.getName());
        }
        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(jwtTokenRequestLogoutDto.getAccessToken());
        redisTemplate.opsForValue()
                .set(jwtTokenRequestLogoutDto.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        return "로그아웃 되었습니다.";
    }
    @Transactional
    @Override
    public String statusChange(UserStatusChangeRequestDto userStatusChangeRequestDto)
    {
        User findUser = this.userRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER, "로그인 유저 정보가 없습니다."));

        ValidatorBucket.of()
                .consistOf(EnumUserStateTypeCorrectValidator.of(userStatusChangeRequestDto.getStatus()))
                .validate();


        findUser.update(
                findUser.getName(),
                findUser.getPassword(),
                findUser.getProfileImageUrl(),
                findUser.getRole(),
                UserState.valueOf(userStatusChangeRequestDto.getStatus()),
                findUser.getPoints()
        );

        this.userRepository.update(findUser)
                .map(UserResponseDto::of)
                .orElseThrow(()-> new ConflictException(ErrorCode.UPDATE_FAIL, "회원 status 업데이트를 실패했습니다."));

        return "회원 상태가 변경되었습니다.";
    }
}
