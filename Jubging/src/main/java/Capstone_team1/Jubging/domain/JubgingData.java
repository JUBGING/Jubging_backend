package Capstone_team1.Jubging.domain;

import Capstone_team1.Jubging.domain.model.JubgingDataStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Time;

@Getter
@Setter
@Entity
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = {AuditingEntityListener.class})
@Table(name = "jubging_data")
public class JubgingData extends BaseTimeEntity{
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "step_cnt", nullable = false)
    @ColumnDefault("0")
    private Integer stepCnt;

    @Column(name = "distance", nullable = false)
    @ColumnDefault("0.00")
    private Float distance;

    @Column(name = "time", nullable = false)
    @ColumnDefault("'00:12:48'")
    private Time time;

    @Column(name = "calorie", nullable = false)
    @ColumnDefault("0")
    private Integer calorie;

    @Column(name = "weight", nullable = false)
    @ColumnDefault("0.00")
    private Float weight;

    @Column(name = "img_url", length = 200, nullable = false)
    @ColumnDefault("'/default/imgUrl'")
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="tongs_id", nullable = false)
    private Tong tong;

    @Column(name = "tongs_return", nullable = false)
    @ColumnDefault("0")
    private Boolean tongs_return;

    @Column(name = "status", nullable = false)
    @ColumnDefault("'INPROGRESS'")
    @Enumerated(EnumType.STRING)
    private JubgingDataStatus status;

    public static JubgingData create(User user, Tong tong){
        JubgingData jubgingData = new JubgingData();
        jubgingData.setUser(user);
        jubgingData.setTong(tong);
        return jubgingData;
    }

    public JubgingData update(float weight, int stepCnt, float distance, int calorie, User user, Tong tong, boolean tongs_return, JubgingDataStatus status){
        this.weight = weight;
        this.stepCnt = stepCnt;
        this.distance = distance;
        this.calorie = calorie;
        this.user = user;
        this.tong = tong;
        this.tongs_return = tongs_return;
        this.status = status;
        return this;
    }

    public JubgingData updateImage(String imgUrl){
        this.imgUrl = imgUrl;
        return this;
    }


}
