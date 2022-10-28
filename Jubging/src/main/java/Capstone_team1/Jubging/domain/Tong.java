package Capstone_team1.Jubging.domain;

import Capstone_team1.Jubging.domain.model.TongStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = {AuditingEntityListener.class})
@Table(name = "tongs")
public class Tong extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tongs_id")
    private Integer tongs_id;
    @Column(name = "status",length = 10)
    @Enumerated(EnumType.STRING)
    private TongStatus status;
    public Tong updateStatus(TongStatus status){
        this.status = status;
        return this;
    }
}
