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
    private Integer distance;

    @Column(name = "time")
    private Time time;

    @Column(name = "calorie")
    private Integer calorie;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "img_url", length = 60)
    private String imgUrl;

    @ManyToOne
    private User user;
}
