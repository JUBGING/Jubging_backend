package Capstone_team1.Jubging.dto.jubjubi;

import Capstone_team1.Jubging.domain.JubgingData;
import Capstone_team1.Jubging.domain.User;
import Capstone_team1.Jubging.dto.user.UserMyInfoUpdateResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EndJubgingResponseDto {
    private float weight;
    private int calorie;
    private float distance;
    private int step_cnt;
    private boolean tongs_return;

    public static EndJubgingResponseDto of(JubgingData jubgingData){
        return new EndJubgingResponseDto(
                jubgingData.getWeight(),
                jubgingData.getCalorie(),
                jubgingData.getDistance(),
                jubgingData.getStepCnt(),
                jubgingData.getTongs_return()
        );
    }

}
