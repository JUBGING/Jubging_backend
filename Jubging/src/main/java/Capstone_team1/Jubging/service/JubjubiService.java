package Capstone_team1.Jubging.service;

import Capstone_team1.Jubging.dto.jubjubi.JubjubiResponseDto;
import Capstone_team1.Jubging.repository.JpaJubjubiRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class JubjubiService {
    private final JpaJubjubiRepository jubjubiRepository;

    public List<JubjubiResponseDto> findByUserPosition(String userPosition){
        String[] coord = userPosition.split(" ");
        String[] northWestLatLng = coord[0].split(",");
        String[] southEastLatLng = coord[1].split(",");
        float maxLat = Float.parseFloat(northWestLatLng[0]);
        float minLng = Float.parseFloat(northWestLatLng[1]);
        float minLat = Float.parseFloat(southEastLatLng[0]);
        float maxLng = Float.parseFloat(southEastLatLng[1]);

        List<JubjubiResponseDto> jubjubiNearByList = jubjubiRepository.findByUserPosition(minLat, minLng, maxLat, maxLng);
        return jubjubiNearByList;
    }
}
