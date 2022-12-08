package Capstone_team1.Jubging.dto.jubjubi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EndJubgingRequestDto {
    private int tongs_id;
    private int jubjubi_id;
    private float weight;
    private int calorie;
    private float distance;
    private int step_cnt;
    private boolean tongs_return;
    private String time;
}
