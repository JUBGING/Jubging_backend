package Capstone_team1.Jubging.dto.user;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class DeleteShoppingBagRequestDto {

    @NotNull
    private String productId;

    @NotNull
    private Integer cnt;
}
