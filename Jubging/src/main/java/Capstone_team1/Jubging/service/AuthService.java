package Capstone_team1.Jubging.service;

import Capstone_team1.Jubging.dto.auth.UserRequestUpdatePasswordDto;
import Capstone_team1.Jubging.dto.auth.UserResponseDto;
import Capstone_team1.Jubging.dto.auth.UserSignInRequestDto;
import Capstone_team1.Jubging.dto.jwt.JwtTokenDto;

public interface AuthService {
    JwtTokenDto signIn(UserSignInRequestDto userSignInRequestDto);
    UserResponseDto updatePassword(UserRequestUpdatePasswordDto updatePasswordDto);
}
