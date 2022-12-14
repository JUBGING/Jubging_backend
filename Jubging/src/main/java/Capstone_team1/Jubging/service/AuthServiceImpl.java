package Capstone_team1.Jubging.service;

import Capstone_team1.Jubging.config.exception.BadRequestException;
import Capstone_team1.Jubging.config.exception.ConflictException;
import Capstone_team1.Jubging.config.exception.ErrorCode;
import Capstone_team1.Jubging.config.exception.NotFoundException;
import Capstone_team1.Jubging.config.exception.UnauthorizedException;
import Capstone_team1.Jubging.config.jwt.JwtTokenProvider;
import Capstone_team1.Jubging.config.utils.SecurityUtil;
import Capstone_team1.Jubging.config.validation.EnumTypeCorrectValidator;
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
                        ErrorCode.INVALID_SIGNIN,
                        "????????? ????????? ?????????."
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


        // 1. Login ID/PW ??? ???????????? AuthenticationToken ??????
        UsernamePasswordAuthenticationToken authenticationToken = userSignInRequestDto.toAuthentication();

        // 2. ????????? ?????? (????????? ???????????? ??????) ??? ??????????????? ??????
        //    authenticate ???????????? ????????? ??? ??? CustomUserDetailsService ?????? ???????????? loadUserByUsername ???????????? ?????????
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. ?????? ????????? ???????????? JWT ?????? ??????
        JwtTokenDto jwtTokenDto = jwtTokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken redis??? ??????
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(),
                        jwtTokenDto.getRefreshToken(),
                        jwtTokenDto.getRefreshTokenExpiresIn(),
                        TimeUnit.MILLISECONDS);

        // 5. ?????? ??????
        return jwtTokenDto;
    }

    @Transactional
    @Override
    public UserResponseDto signUp(UserRequestSignUpDto userRequestSignUpDto) {

        this.userRepository.findByEmail(userRequestSignUpDto.getEmail()).ifPresent(
                email->{
                    throw new BadRequestException(
                            ErrorCode.DUPLICATE_EMAIL,
                            "???????????? ????????? ?????????."
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
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER, "????????? ?????? ????????? ????????????."));
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
                .orElseThrow(()-> new ConflictException(ErrorCode.UPDATE_FAIL, "???????????? ??????????????? ??????????????????."));
    }

    @Transactional
    @Override
    public JwtTokenDto reissue(JwtTokenRequestReissueDto jwtTokenRequestDto) {
        // 1. Refresh Token ??????
        if (!jwtTokenProvider.validateToken(jwtTokenRequestDto.getRefreshToken())) {
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_JWT, "Refresh Token??? ???????????? ????????????.");
        }

        // 2. Access Token ?????? Member ID ????????????
        Authentication authentication = jwtTokenProvider.getAuthentication(jwtTokenRequestDto.getAccessToken());

        // 3. Redis?????? Refresh Token ??? ?????????
        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());

        //?????????????????? Redis ??? RefreshToken ??? ???????????? ?????? ?????? ??????
        if(ObjectUtils.isEmpty(refreshToken)) {
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_JWT, "????????? ???????????????.");
        }
        //refresh token??? ?????? ??????
        if(!refreshToken.equals(jwtTokenRequestDto.getRefreshToken()))
        {
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_JWT, "Refresh Token??? ???????????? ????????????.");
        }

        // 4. ????????? ?????? ??????
        JwtTokenDto jwtTokenDto = jwtTokenProvider.generateTokenDto(authentication);

        // 5. RefreshToken Redis ????????????
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(),
                        jwtTokenDto.getRefreshToken(),
                        jwtTokenDto.getRefreshTokenExpiresIn(),
                        TimeUnit.MILLISECONDS);

        // ?????? ??????
        return jwtTokenDto;
    }

    @Transactional
    @Override
    public String logout(JwtTokenRequestLogoutDto jwtTokenRequestLogoutDto)
    {
        // 1. Access Token ??????
        if (!jwtTokenProvider.validateToken(jwtTokenRequestLogoutDto.getAccessToken())) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_JWT, "????????? ???????????????.");
        }
        // 2. Access Token ?????? User??? name(id)??? ????????????.
        Authentication authentication = jwtTokenProvider.getAuthentication(jwtTokenRequestLogoutDto.getAccessToken());
        // 3. Redis ?????? ?????? User??? name(email) ??? ????????? Refresh Token ??? ????????? ????????? ?????? ??? ?????? ?????? ???????????????.
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Refresh Token ??????
            redisTemplate.delete("RT:" + authentication.getName());
        }
        // 4. ?????? Access Token ???????????? ????????? ?????? BlackList ??? ????????????
        Long expiration = jwtTokenProvider.getExpiration(jwtTokenRequestLogoutDto.getAccessToken());
        redisTemplate.opsForValue()
                .set(jwtTokenRequestLogoutDto.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        return "???????????? ???????????????.";
    }
    @Transactional
    @Override
    public String statusChange(UserStatusChangeRequestDto userStatusChangeRequestDto)
    {
        User findUser = this.userRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER, "????????? ?????? ????????? ????????????."));

        ValidatorBucket.of()
                .consistOf(EnumTypeCorrectValidator.of(userStatusChangeRequestDto.getStatus()))
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
                .orElseThrow(()-> new ConflictException(ErrorCode.UPDATE_FAIL, "?????? status ??????????????? ??????????????????."));

        return "?????? ????????? ?????????????????????.";
    }
}
