package Capstone_team1.Jubging.controller;

import Capstone_team1.Jubging.dto.auth.UserSignInRequestDto;
import Capstone_team1.Jubging.service.AuthServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthServiceImpl authServiceImpl;

    public AuthController(AuthServiceImpl authServiceImpl) { this.authServiceImpl = authServiceImpl; }

    @PostMapping("/sign-in")
    @ResponseStatus(value = HttpStatus.OK)
    public String signIn(@RequestBody UserSignInRequestDto userSignInRequestDto) {
        return this.authServiceImpl.signIn(userSignInRequestDto);
    }
}
