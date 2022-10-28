package Capstone_team1.Jubging.service;

import Capstone_team1.Jubging.config.exception.BadRequestException;
import Capstone_team1.Jubging.config.exception.ConflictException;
import Capstone_team1.Jubging.config.exception.ErrorCode;
import Capstone_team1.Jubging.config.exception.NotFoundException;
import Capstone_team1.Jubging.config.utils.SecurityUtil;
import Capstone_team1.Jubging.domain.Dibs;
import Capstone_team1.Jubging.domain.Product;
import Capstone_team1.Jubging.domain.User;
import Capstone_team1.Jubging.dto.MessageResponseDto;
import Capstone_team1.Jubging.dto.products.ProductsResponseInfo;
import Capstone_team1.Jubging.dto.user.AddShoppingBagRequestDto;
import Capstone_team1.Jubging.dto.user.DeleteShoppingBagRequestDto;
import Capstone_team1.Jubging.dto.user.FindUserInfoDto;
import Capstone_team1.Jubging.dto.user.SearchShoppingBagResponseDto;
import Capstone_team1.Jubging.dto.user.UserJubgingDataResponseDto;
import Capstone_team1.Jubging.dto.user.UserMyInfoUpdateRequestDto;
import Capstone_team1.Jubging.dto.user.UserMyInfoUpdateResponseDto;
import Capstone_team1.Jubging.dto.user.UserPurchaseDetailsResponseDto;
import Capstone_team1.Jubging.repository.DibsRepository;
import Capstone_team1.Jubging.repository.ProductsRepository;
import Capstone_team1.Jubging.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final ProductsRepository productsRepository;

    private final DibsRepository dibsRepository;

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
        User findUser = this.userRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER, "로그인 유저 정보가 없습니다."));

        return this.userRepository.findOrderInfo(SecurityUtil.getCurrentUserEmail());
    }

    @Transactional
    public MessageResponseDto addShoppingBag(AddShoppingBagRequestDto addShoppingBagRequestDto)
    {
        User findUser = this.userRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER, "로그인 유저 정보가 없습니다."));
        Product findProduct = productsRepository.findById(addShoppingBagRequestDto.getProductId())
                .orElseThrow(()-> new NotFoundException(ErrorCode.NOT_FOUND_PRODUCTS, "상품을 찾을 수 없습니다."));

        Optional<Dibs> dibs = this.dibsRepository.findById(findUser, findProduct);

        if(dibs.isEmpty())
        {
            if(findProduct.getCnt() < addShoppingBagRequestDto.getCnt())
            {
                throw new BadRequestException(ErrorCode.INVALID_PRODUCT_CNT, "상품 갯수가 유효하지 않습니다.");
            }

            Dibs newDibs = Dibs.createDibs(findUser, findProduct, addShoppingBagRequestDto.getCnt());
            dibsRepository.create(newDibs);
        }
        else {
            if(dibs.get().getCnt() + addShoppingBagRequestDto.getCnt() > findProduct.getCnt())
            {
                throw new BadRequestException(ErrorCode.INVALID_PRODUCT_CNT, "상품 갯수가 유효하지 않습니다.");
            }

            Dibs updateDibs = Dibs.createDibs(findUser, findProduct, dibs.get().getCnt() + addShoppingBagRequestDto.getCnt());
            dibsRepository.update(findUser, findProduct, updateDibs);
        }

        return new MessageResponseDto("장바구니에 상품이 추가가 되었습니다.");
    }

    @Transactional
    public MessageResponseDto deleteShoppingBag(DeleteShoppingBagRequestDto deleteShoppingBagRequestDto)
    {
        User findUser = this.userRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER, "로그인 유저 정보가 없습니다."));
        Product findProduct = productsRepository.findById(deleteShoppingBagRequestDto.getProductId())
                .orElseThrow(()-> new NotFoundException(ErrorCode.NOT_FOUND_PRODUCTS, "상품을 찾을 수 없습니다."));

        Optional<Dibs> dibs = this.dibsRepository.findById(findUser, findProduct);

        if(dibs.isEmpty())
        {
            throw new BadRequestException(ErrorCode.INVALID_PRODUCT_DELETE, "장바구니에 삭제할 목록이 없습니다.");
        }
        else {
            if(dibs.get().getCnt() - deleteShoppingBagRequestDto.getCnt() <= 0)
            {
                dibsRepository.delete(findUser, findProduct);
            }
            else {
                Dibs updateDibs = Dibs.createDibs(findUser, findProduct, dibs.get().getCnt() - deleteShoppingBagRequestDto.getCnt());
                dibsRepository.update(findUser, findProduct, updateDibs);
            }
        }

        return new MessageResponseDto("장바구니에 상품이 제거가 되었습니다.");
    }

    @Transactional(readOnly = true)
    public List<SearchShoppingBagResponseDto> getShoppingBagList()
    {
        User findUser = this.userRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER, "로그인 유저 정보가 없습니다."));

        return this.dibsRepository.findByUserId(findUser).stream().map(SearchShoppingBagResponseDto::of).collect(Collectors.toList());
    }
}
