package Capstone_team1.Jubging.dto.user;

import Capstone_team1.Jubging.domain.JubgingData;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserJubgingDataResponseDto {
    private LocalDateTime createDate;
    private Integer step_cnt;
    private Integer distance;
    private Time time;
    private Float weight;
    private String img_url;

    private UserJubgingDataResponseDto(LocalDateTime createDate, Integer step_cnt, Integer distance, Time time, Float weight, String img_url) {
        this.createDate = createDate;
        this.step_cnt = step_cnt;
        this.distance = distance;
        this.time = time;
        this.weight = weight;
        this.img_url = img_url;
    }

    public static UserJubgingDataResponseDto of(JubgingData jubgingData){
        return new UserJubgingDataResponseDto(
                jubgingData.getCreateDate(),
                jubgingData.getStepCnt(),
                jubgingData.getDistance(),
                jubgingData.getTime(),
                jubgingData.getWeight(),
                jubgingData.getImgUrl());
    }
}
