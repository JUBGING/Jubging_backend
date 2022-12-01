package Capstone_team1.Jubging.repository;

import Capstone_team1.Jubging.domain.Jubjubi;
import Capstone_team1.Jubging.dto.jubjubi.JubjubiResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaJubjubiRepository extends JpaRepository<Jubjubi, Integer> {

    @Query(value = "select jubjubi_id, name, lat, lng, tongs_cnt, plastic_bag_cnt " +
            "from jubjubi " +
            "where lat >= :minLat and lat <= :maxLat " +
            "and lng >= :minLng and lng <= :maxLng " +
            "and status='ACTIVE'", nativeQuery = true)
    List<JubjubiResponseDto> findByUserPosition(@Param("minLat") double minLat, @Param("minLng") double minLng, @Param("maxLat") double maxLat, @Param("maxLng") double maxLng);

    Optional<Jubjubi> findById(String jubjubiId);
}
