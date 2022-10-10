package Capstone_team1.Jubging.repository;

import Capstone_team1.Jubging.domain.JubgingData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaJubgingDataRepository extends JpaRepository<JubgingData, String> {

}
