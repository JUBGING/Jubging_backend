package Capstone_team1.Jubging.dto.jwt;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenRequestReissueDto {
    @NotNull
    private String accessToken;
    @NotNull
    private String refreshToken;
}
