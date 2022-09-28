package Capstone_team1.Jubging.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserDomainModel {
    private String id;

    private String profileImageUrl;

    @NotBlank(message = "사용자 이름이 입력되지 않았습니다.")
    private String name;

    @Email(message = "잘못된 이메일 형식입니다.")
    @NotNull(message = "이메일이 입력되지 않았습니다.")
    private String email;

    @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
    private String password;

    @NotNull(message = "사용자 권한이 입력되지 않았습니다.")
    private Role role;

    @NotNull(message = "사용자 상태가 입력되지 않았습니다.")
    private UserState state;

    private UserDomainModel(
            String id,
            String email,
            String name,
            String password,
            String profileImageUrl,
            Role role,
            UserState state
    ) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.state = state;
    }

    public static UserDomainModel of(
            String id,
            String email,
            String name,
            String password,
            String profileImageUrl,
            Role role,
            UserState state
    ) {
        return new UserDomainModel(
                id,
                email,
                name,
                password,
                profileImageUrl,
                role,
                state
        );
    }


    public void update(
            String email,
            String name,
            String profileImageUrl
    ) {
        this.email = email;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    public String updatePassword(String newPassword) {
        this.password = newPassword;
        return this.password;
    }
}