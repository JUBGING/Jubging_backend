package Capstone_team1.Jubging.service;

import Capstone_team1.Jubging.dto.auth.UserSignInRequestDto;

public interface AuthService {
    public String signIn(UserSignInRequestDto userSignInRequestDto);
}
