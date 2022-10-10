package Capstone_team1.Jubging.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Time;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "jubging_data")
public class JubgingData extends BaseTimeEntity{
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "step_cnt")
    private Integer stepCnt;

    @Column(name = "distance")
    private Float distance;

    @Column(name = "time")
    private Time time;

    @Column(name = "calorie")
    private Integer calorie;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "img_url", length = 60)
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="tongs_id")
    private Tong tong;

    @Column(name = "tongs_return")
    private Boolean tongs_return;

    public static JubgingData create(User user, Tong tong){
        JubgingData jubgingData = new JubgingData();
        jubgingData.setUser(user);
        jubgingData.setTong(tong);
        return jubgingData;
    }

    public JubgingData update(int stepCnt, float distance, int calorie, String imgUrl, User user, Tong tong, boolean tongs_return){
        this.stepCnt = stepCnt;
        this.distance = distance;
        this.calorie = calorie;
        this.imgUrl = imgUrl;
        this.user = user;
        this.tong = tong;
        this.tongs_return = tongs_return;
        return this;
    }


}
