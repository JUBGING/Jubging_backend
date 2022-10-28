package Capstone_team1.Jubging.controller;

import Capstone_team1.Jubging.dto.MessageResponseDto;
import Capstone_team1.Jubging.dto.user.AddShoppingBagRequestDto;
import Capstone_team1.Jubging.dto.user.DeleteShoppingBagRequestDto;
import Capstone_team1.Jubging.dto.user.FindUserInfoDto;
import Capstone_team1.Jubging.dto.user.SearchShoppingBagResponseDto;
import Capstone_team1.Jubging.dto.user.UserJubgingDataResponseDto;
import Capstone_team1.Jubging.dto.user.UserMyInfoUpdateRequestDto;
import Capstone_team1.Jubging.dto.user.UserMyInfoUpdateResponseDto;
import Capstone_team1.Jubging.dto.user.UserPurchaseDetailsResponseDto;
import Capstone_team1.Jubging.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public FindUserInfoDto getMyInfo() { return this.userService.getMyInfo(); }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public UserMyInfoUpdateResponseDto updateMyInfo(@RequestBody @Validated UserMyInfoUpdateRequestDto userMyInfoUpdateRequestDto){
        return this.userService.updateMyInfo(userMyInfoUpdateRequestDto);
    }

    @GetMapping("/jubging_data")
    @ResponseStatus(value = HttpStatus.OK)
    public List<UserJubgingDataResponseDto> getJubgingData() { return this.userService.getJubgingData(); }

    @GetMapping("/purchase_detail")
    @ResponseStatus(value = HttpStatus.OK)
    public List<UserPurchaseDetailsResponseDto> getPurchaseDetails() {return this.userService.getPurchaseDetails(); }

    @PostMapping("/shopping_bag")
    @ResponseStatus(value = HttpStatus.CREATED)
    public MessageResponseDto addShoppingBag(@RequestBody @Validated AddShoppingBagRequestDto addShoppingBagRequestDto)
    {
        return this.userService.addShoppingBag(addShoppingBagRequestDto);
    }

    @DeleteMapping("/shopping_bag")
    @ResponseStatus(value = HttpStatus.OK)
    public MessageResponseDto deleteShoppingBag(@RequestBody @Validated DeleteShoppingBagRequestDto deleteShoppingBagRequestDto)
    {
        return this.userService.deleteShoppingBag(deleteShoppingBagRequestDto);
    }

    @GetMapping("/shopping_bag")
    @ResponseStatus(value = HttpStatus.OK)
    public List<SearchShoppingBagResponseDto> getShoppingBagList()
    {
        return this.userService.getShoppingBagList();
    }
}


