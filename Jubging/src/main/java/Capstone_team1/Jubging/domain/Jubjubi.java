package Capstone_team1.Jubging.domain;

import Capstone_team1.Jubging.domain.BaseTimeEntity;
import org.hibernate.annotations.CollectionId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Jubjubi")
public class Jubjubi extends BaseTimeEntity {
    @Id
    @Column(name = "jubjubi_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(20)")
    private String name;

    @Column(name = "weight", nullable = false)
    private float weight;

    @Column(name = "tongs_cnt", nullable = false)
    private int tongs_cnt;

    @Column(name = "plastic_bag_cnt", nullable = false)
    private int plastic_bag_cnt;

    @Column(name = "lat", nullable = false)
    private float lat;

    @Column(name = "lng", nullable = false)
    private float lng;

    @Column(name = "status", nullable = false, columnDefinition = "varchar(10) default ACTIVE")
    private String status;

}
