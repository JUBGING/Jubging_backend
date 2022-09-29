package Capstone_team1.Jubging.repository;

import Capstone_team1.Jubging.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    List<User> findByName(String name);

    Optional<User> findByEmailAndName(String email, String name);

    boolean existsByEmail(String email);
}
