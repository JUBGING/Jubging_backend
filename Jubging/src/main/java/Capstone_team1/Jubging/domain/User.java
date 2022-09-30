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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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

    private User(
            String email,
            String name,
            String password,
            String profileImageUrl,
            Role role,
            UserState state
    ) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.state = state;
    }

    private User(
            String id,
            String email,
            String name,
            String password,
            String profileImageUrl,
            Role role,
            UserState state
    ) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.state = state;
    }

    public static User of(
            String email,
            String name,
            String password,
            String profileImageUrl,
            Role role,
            UserState state
    ) {
        return new User(
                email,
                name,
                password,
                profileImageUrl,
                role,
                state
        );
    }

    public static User of(
            String id,
            String email,
            String name,
            String password,
            String profileImageUrl,
            Role role,
            UserState state
    ) {
        return new User(
                id,
                email,
                name,
                password,
                profileImageUrl,
                role,
                state
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
                userDomainModel.getState()
        );
    }

    public User update(String name, String password, String profileImageUrl, Role role, UserState userState) {
        this.name = name;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.state = userState;

        return this;
    }

    public String updatePassword(String newPassword)
    {
        this.password = newPassword;
        return newPassword;
    }

    public static User createUser(String email, String password, String name, Role role, UserState userState){
        User user = new User();
        user.email = email;
        user.password = password;
        user.name = name;
        user.role = role;
        user.state = userState;

        return user;
    }
}
