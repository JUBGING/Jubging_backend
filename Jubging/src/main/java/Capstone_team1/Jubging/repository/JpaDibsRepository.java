package Capstone_team1.Jubging.repository;

import Capstone_team1.Jubging.domain.Dibs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface JpaDibsRepository extends JpaRepository<Dibs, String> {

    Optional<Dibs> findByUser_idAndProduct_id(String user_id, String product_id);
    List<Dibs> findByUser_id(String user_id);
    void deleteByUser_idAndProduct_id(String user_id, String product_id);
}
