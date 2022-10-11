package Capstone_team1.Jubging.dto.auth;

import Capstone_team1.Jubging.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private String id;
    private String email;
    private String name;

    private UserResponseDto(String id, String email, String name){
        this.id = id; this.email = email; this.name = name;
    }

    public static UserResponseDto of(User user){
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getName());
    }
}
