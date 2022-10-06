package Capstone_team1.Jubging.service;

import Capstone_team1.Jubging.config.exception.ConflictException;
import Capstone_team1.Jubging.config.exception.ErrorCode;
import Capstone_team1.Jubging.config.exception.NotFoundException;
import Capstone_team1.Jubging.config.utils.SecurityUtil;
import Capstone_team1.Jubging.domain.User;
import Capstone_team1.Jubging.dto.user.FindUserInfoDto;
import Capstone_team1.Jubging.dto.user.UserJubgingDataResponseDto;
import Capstone_team1.Jubging.dto.user.UserMyInfoUpdateRequestDto;
import Capstone_team1.Jubging.dto.user.UserMyInfoUpdateResponseDto;
import Capstone_team1.Jubging.dto.user.UserPurchaseDetailsResponseDto;
import Capstone_team1.Jubging.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public FindUserInfoDto getMyInfo() {
        return this.userRepository.findUserInfo(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER,"로그인 유저 정보가 없습니다."));
    }

    @Transactional
    public UserMyInfoUpdateResponseDto updateMyInfo(UserMyInfoUpdateRequestDto userMyInfoUpdateRequestDto) {

        User updateUser = this.userRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .map(user -> user.update(
                        userMyInfoUpdateRequestDto.getName(),
                        user.getPassword(),
                        user.getProfileImageUrl(),
                        user.getRole(),
                        user.getState(),
                        user.getPoints()))
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER, "로그인 유저 정보가 없습니다."));

        return this.userRepository.update(updateUser)
                .map(UserMyInfoUpdateResponseDto::of)
                .orElseThrow(()-> new ConflictException(ErrorCode.UPDATE_FAIL, "현재 사용자 정보 업데이트를 실패했습니다."));
    }

    @Transactional(readOnly = true)
    public List<UserJubgingDataResponseDto> getJubgingData(){

        User findUser = this.userRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER, "로그인 유저 정보가 없습니다."));

        return findUser.getJubgingDataList().stream().map(UserJubgingDataResponseDto::of).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserPurchaseDetailsResponseDto> getPurchaseDetails(){
       return this.userRepository.findOrderInfo(SecurityUtil.getCurrentUserEmail());
    }
}
