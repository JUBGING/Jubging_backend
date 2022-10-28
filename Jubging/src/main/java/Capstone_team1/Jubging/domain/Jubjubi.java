package Capstone_team1.Jubging.domain;

import Capstone_team1.Jubging.domain.model.JubjubiStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "jubjubi")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = {AuditingEntityListener.class})
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
    @Enumerated(EnumType.STRING)
    private JubjubiStatus status;

    public Jubjubi serve(){
        if(this.plastic_bag_cnt > 0) this.plastic_bag_cnt -= 1;
        if(this.tongs_cnt > 0) this.tongs_cnt -= 1;
        return this;
    }

}
