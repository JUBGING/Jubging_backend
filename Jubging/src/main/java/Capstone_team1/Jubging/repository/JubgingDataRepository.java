package Capstone_team1.Jubging.repository;

import Capstone_team1.Jubging.domain.JubgingData;
import Capstone_team1.Jubging.domain.model.JubgingDataStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JubgingDataRepository {

    private final JpaJubgingDataRepository jpaJubgingDataRepository;

    public JubgingData create(JubgingData jubgingData){return this.jpaJubgingDataRepository.save(jubgingData);}

    public Optional<JubgingData> findJubgingDataInProgress(String id, JubgingDataStatus status){return this.jpaJubgingDataRepository.findByUser_idAndStatus(id,status);}

}