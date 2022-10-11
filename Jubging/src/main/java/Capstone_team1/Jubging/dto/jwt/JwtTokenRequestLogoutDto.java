package Capstone_team1.Jubging.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenRequestLogoutDto {
    @NotNull
    private String accessToken;
}
