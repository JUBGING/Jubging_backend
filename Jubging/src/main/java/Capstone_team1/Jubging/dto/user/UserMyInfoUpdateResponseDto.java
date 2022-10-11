package Capstone_team1.Jubging.dto.user;

import Capstone_team1.Jubging.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserMyInfoUpdateResponseDto {
    private String email;
    private String name;

    private UserMyInfoUpdateResponseDto(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public static UserMyInfoUpdateResponseDto of(User user){
        return new UserMyInfoUpdateResponseDto(
                user.getEmail(),
                user.getName());
    }
}
