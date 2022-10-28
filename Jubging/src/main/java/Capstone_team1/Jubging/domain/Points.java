package Capstone_team1.Jubging.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = {AuditingEntityListener.class})
@Table(name = "points")
public class Points extends BaseTimeEntity{

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "current_points", columnDefinition = "integer default 0")
    private Integer current_points;

    @Column(name = "total_points", columnDefinition = "integer default 0")
    private Integer total_points;

    public Points update(Integer current_points, Integer total_points)
    {
        this.current_points = current_points;
        this.total_points = total_points;

        return this;
    }

    public static Points createPoints(Integer current_points, Integer total_points){
        Points points = new Points();
        points.current_points = current_points;
        points.total_points = total_points;

        return points;
    }
}
