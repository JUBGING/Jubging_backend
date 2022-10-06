package Capstone_team1.Jubging.repository;

import Capstone_team1.Jubging.domain.User;
import Capstone_team1.Jubging.dto.user.FindUserInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    List<User> findByName(String name);

    Optional<User> findByEmailAndName(String email, String name);

    boolean existsByEmail(String email);

    @Query(nativeQuery = true, value =
            "SELECT users.email, users.name, SUM(jubging_data.step_cnt), SUM(jubging_data.distance), SUM(jubging_data.calorie), points.total_points" +
                    " FROM users LEFT OUTER JOIN points ON users.user_id = points.id" +
                    " LEFT OUTER JOIN jubging_data ON users.user_id = jubging_data.id" +
                    " WHERE users.email = ?1" +
                    " GROUP BY users.email")
    Optional<FindUserInfoDto> findUserInfo(String email);
}
