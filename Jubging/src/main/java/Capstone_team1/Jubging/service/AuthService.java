package Capstone_team1.Jubging.service;

import Capstone_team1.Jubging.dto.MessageResponseDto;
import Capstone_team1.Jubging.dto.auth.UserRequestSignUpDto;
import Capstone_team1.Jubging.dto.auth.UserRequestUpdatePasswordDto;
import Capstone_team1.Jubging.dto.auth.UserResponseDto;
import Capstone_team1.Jubging.dto.auth.UserSignInRequestDto;
import Capstone_team1.Jubging.dto.auth.UserStatusChangeRequestDto;
import Capstone_team1.Jubging.dto.jwt.JwtTokenDto;
import Capstone_team1.Jubging.dto.jwt.JwtTokenRequestLogoutDto;
import Capstone_team1.Jubging.dto.jwt.JwtTokenRequestReissueDto;

public interface AuthService {
    JwtTokenDto signIn(UserSignInRequestDto userSignInRequestDto);
    UserResponseDto updatePassword(UserRequestUpdatePasswordDto updatePasswordDto);

    UserResponseDto signUp(UserRequestSignUpDto userRequestSignUpDto);

    JwtTokenDto reissue(JwtTokenRequestReissueDto jwtTokenRequestDto);

    MessageResponseDto logout(JwtTokenRequestLogoutDto jwtTokenRequestLogoutDto);

    MessageResponseDto statusChange(UserStatusChangeRequestDto userStatusChangeRequestDto);
}
