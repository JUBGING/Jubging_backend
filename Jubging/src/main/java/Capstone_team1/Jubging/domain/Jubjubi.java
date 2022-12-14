package Capstone_team1.Jubging.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "jubjubi")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Jubjubi extends BaseTimeEntity {
    @Id
    @Column(name = "jubjubi_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(20)")
    private String name;

    @Column(name = "weight", nullable = false)
    private Float weight;

    @Column(name = "tongs_unlock_key", nullable = false)
    private String tongs_unlock_key;

    @Column(name = "tongs_cnt", nullable = false)
    private Integer tongs_cnt;

    @Column(name = "plastic_bag_cnt", nullable = false)
    private Integer plastic_bag_cnt;

    @Column(name = "lat", nullable = false)
    private Float lat;

    @Column(name = "lng", nullable = false)
    private Float lng;

    @Column(name = "status", nullable = false, columnDefinition = "varchar(10) default 'ACTIVE'")
    private String status;

    public Jubjubi serve(){
        if(this.plastic_bag_cnt > 0) this.plastic_bag_cnt -= 1;
        if(this.tongs_cnt > 0) this.tongs_cnt -= 1;
        return this;
    }

}
