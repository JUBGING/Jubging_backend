package Capstone_team1.Jubging.repository;

import Capstone_team1.Jubging.domain.Tong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaTongRepository extends JpaRepository<Tong, Integer> {

    Optional<Tong> findById(Integer tongs_id);
}
