package Capstone_team1.Jubging.repository;

import Capstone_team1.Jubging.domain.JubgingData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JubgingDataRepository {

    private final JpaJubgingDataRepository jpaJubgingDataRepository;

    public JubgingData create(JubgingData jubgingData){return this.jpaJubgingDataRepository.save(jubgingData);}
}
