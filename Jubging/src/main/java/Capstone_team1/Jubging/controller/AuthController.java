package Capstone_team1.Jubging.controller;

import Capstone_team1.Jubging.dto.MessageResponseDto;
import Capstone_team1.Jubging.dto.auth.UserRequestSignUpDto;
import Capstone_team1.Jubging.dto.auth.UserRequestUpdatePasswordDto;
import Capstone_team1.Jubging.dto.auth.UserResponseDto;
import Capstone_team1.Jubging.dto.auth.UserSignInRequestDto;
import Capstone_team1.Jubging.dto.auth.UserStatusChangeRequestDto;
import Capstone_team1.Jubging.dto.jwt.JwtTokenDto;
import Capstone_team1.Jubging.dto.jwt.JwtTokenRequestLogoutDto;
import Capstone_team1.Jubging.dto.jwt.JwtTokenRequestReissueDto;
import Capstone_team1.Jubging.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/sign-in")
    @ResponseStatus(value = HttpStatus.OK)
    public JwtTokenDto signIn(@RequestBody @Validated UserSignInRequestDto userSignInRequestDto) {

        return this.authService.signIn(userSignInRequestDto);
    }

    @PostMapping("/sign-up")
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserResponseDto signUp(@RequestBody @Validated UserRequestSignUpDto userRequestSignUpDto) {
        return this.authService.signUp(userRequestSignUpDto);
    }

    @PutMapping("/password")
    @ResponseStatus(value = HttpStatus.OK)
    public UserResponseDto updatePassword(@RequestBody @Validated UserRequestUpdatePasswordDto updatePasswordDto){
        return this.authService.updatePassword(updatePasswordDto);
    }
    @PostMapping("/logout")
    @ResponseStatus(value = HttpStatus.OK)
    public MessageResponseDto logout(@RequestBody @Validated JwtTokenRequestLogoutDto jwtTokenRequestLogoutDto){
        return this.authService.logout(jwtTokenRequestLogoutDto);
    }
    @PostMapping("/reissue")
    @ResponseStatus(value = HttpStatus.CREATED)
    public JwtTokenDto reissue(@RequestBody @Validated JwtTokenRequestReissueDto jwtTokenRequestDto) {
        return this.authService.reissue(jwtTokenRequestDto);
    }

    @PatchMapping("/status")
    @ResponseStatus(value = HttpStatus.OK)
    public MessageResponseDto statusChange(@RequestBody @Validated UserStatusChangeRequestDto userStatusChangeRequestDto){
        return this.authService.statusChange(userStatusChangeRequestDto);
    }
}
