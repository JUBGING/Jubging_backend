package Capstone_team1.Jubging.controller;

import Capstone_team1.Jubging.dto.auth.UserRequestUpdatePasswordDto;
import Capstone_team1.Jubging.dto.auth.UserResponseDto;
import Capstone_team1.Jubging.dto.auth.UserSignInRequestDto;
import Capstone_team1.Jubging.dto.jwt.JwtTokenDto;
import Capstone_team1.Jubging.service.AuthService;
import Capstone_team1.Jubging.service.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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

    @PostMapping("/sign-in")
    @ResponseStatus(value = HttpStatus.OK)
    public JwtTokenDto signIn(@RequestBody @Validated UserSignInRequestDto userSignInRequestDto) {
        return this.authService.signIn(userSignInRequestDto);
    }

    @PutMapping("/password")
    @ResponseStatus(value = HttpStatus.OK)
    public UserResponseDto updatePassword(@RequestBody @Validated UserRequestUpdatePasswordDto updatePasswordDto){
        return this.authService.updatePassword(updatePasswordDto);
    }
}
