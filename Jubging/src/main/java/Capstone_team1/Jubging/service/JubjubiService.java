package Capstone_team1.Jubging.service;

import Capstone_team1.Jubging.dto.jubjubi.JubjubiResponseDto;
import Capstone_team1.Jubging.repository.JubjubiRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.Math.*;

@AllArgsConstructor
@Service
public class JubjubiService {
    private final JubjubiRepository jubjubiRepository;

    public List<JubjubiResponseDto> findByUserPosition(String userPosition){
        String[] coord = userPosition.split(" ");
        String[] northWestLatLng = coord[0].split(",");
        String[] southEastLatLng = coord[1].split(",");
        float minLat = Float.parseFloat(northWestLatLng[0]);
        float maxLng = Float.parseFloat(northWestLatLng[1]);
        float maxLat = Float.parseFloat(southEastLatLng[0]);
        float minLng = Float.parseFloat(southEastLatLng[1]);

        List<JubjubiResponseDto> jubjubiNearByList = jubjubiRepository.findByUserPosition(minLat, minLng, maxLat, maxLng);
        return jubjubiNearByList;
    }
}
