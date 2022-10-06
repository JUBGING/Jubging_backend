package Capstone_team1.Jubging.domain;


import Capstone_team1.Jubging.domain.model.Role;
import Capstone_team1.Jubging.domain.model.UserDomainModel;
import Capstone_team1.Jubging.domain.model.UserState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = {AuditingEntityListener.class})
public class User extends BaseTimeEntity{
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "user_id", nullable = false, unique = true)
    private String id;

    @Column(unique = true, length = 40)
    private String email;

    @Column(length = 40)
    private String name;

    @Column(columnDefinition = "char(60)")
    private String password;

    @Column(name = "profile_image_url", columnDefinition = "varchar(60) default '/default/image'")
    private String profileImageUrl;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserState state;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id", nullable = false)
    private Points points;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private final List<JubgingData> jubgingDataList = new ArrayList<JubgingData>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private final List<Order> orderList = new ArrayList<Order>();


    private User(
            String email,
            String name,
            String password,
            String profileImageUrl,
            Role role,
            UserState state,
            Points points
    ) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.state = state;
        this.points = points;
    }

    private User(
            String id,
            String email,
            String name,
            String password,
            String profileImageUrl,
            Role role,
            UserState state,
            Points points
    ) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.state = state;
        this.points = points;
    }

    public static User of(
            String email,
            String name,
            String password,
            String profileImageUrl,
            Role role,
            UserState state,
            Points points
    ) {
        return new User(
                email,
                name,
                password,
                profileImageUrl,
                role,
                state,
                points
        );
    }

    public static User of(
            String id,
            String email,
            String name,
            String password,
            String profileImageUrl,
            Role role,
            UserState state,
            Points points
    ) {
        return new User(
                id,
                email,
                name,
                password,
                profileImageUrl,
                role,
                state,
                points
        );
    }

    public static User from(UserDomainModel userDomainModel) {
        return new User(
                userDomainModel.getId(),
                userDomainModel.getEmail(),
                userDomainModel.getName(),
                userDomainModel.getPassword(),
                userDomainModel.getProfileImageUrl(),
                userDomainModel.getRole(),
                userDomainModel.getState(),
                userDomainModel.getPoints()
        );
    }

    public User update(String name, String password, String profileImageUrl, Role role, UserState userState, Points points) {
        this.name = name;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.state = userState;
        this.points = points;
        return this;
    }

    public String updatePassword(String newPassword)
    {
        this.password = newPassword;
        return newPassword;
    }

    public static User createUser(String email, String password, String name, Role role, UserState userState, Points points){
        User user = new User();
        user.email = email;
        user.password = password;
        user.name = name;
        user.role = role;
        user.state = userState;
        user.points = points;

        return user;
    }
}
