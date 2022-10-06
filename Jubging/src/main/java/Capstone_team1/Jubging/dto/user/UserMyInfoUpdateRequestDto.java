package Capstone_team1.Jubging.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserMyInfoUpdateRequestDto {

    @NotBlank(message = "사용자 이름이 입력되지 않았습니다.")
    private String name;
}
