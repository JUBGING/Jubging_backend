package Capstone_team1.Jubging.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestSignUpDto {

    @Email(message = "잘못된 이메일 형식입니다.")
    @NotNull(message = "이메일이 입력되지 않았습니다.")
    private String email;

    @NotBlank(message = "사용자 이름이 입력되지 않았습니다.")
    private String name;
    @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{8,14}",
            message = "비밀번호는 영문자와 숫자, 특수기호가 적어도 1개 이상 포함된 8자~14자의 비밀번호여야 합니다.")
    private String password;
}
