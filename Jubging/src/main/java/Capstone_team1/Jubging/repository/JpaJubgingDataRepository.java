package Capstone_team1.Jubging.repository;

import Capstone_team1.Jubging.domain.JubgingData;
import Capstone_team1.Jubging.domain.model.JubgingDataStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaJubgingDataRepository extends JpaRepository<JubgingData, String> {
    Optional<JubgingData> findByUser_idAndStatus(String user_id, JubgingDataStatus status);

}
