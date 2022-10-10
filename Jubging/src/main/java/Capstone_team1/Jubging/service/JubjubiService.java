package Capstone_team1.Jubging.service;

import Capstone_team1.Jubging.config.exception.ErrorCode;
import Capstone_team1.Jubging.config.exception.NotFoundException;
import Capstone_team1.Jubging.config.utils.SecurityUtil;
import Capstone_team1.Jubging.domain.JubgingData;
import Capstone_team1.Jubging.domain.Jubjubi;
import Capstone_team1.Jubging.domain.Tong;
import Capstone_team1.Jubging.domain.User;
import Capstone_team1.Jubging.dto.jubjubi.JubjubiResponseDto;
import Capstone_team1.Jubging.dto.jubjubi.StartJubgingRequestDto;
import Capstone_team1.Jubging.dto.jubjubi.StartJubgingResponseDto;
import Capstone_team1.Jubging.repository.JpaJubjubiRepository;
import Capstone_team1.Jubging.repository.JpaTongRepository;
import Capstone_team1.Jubging.repository.JubgingDataRepository;
import Capstone_team1.Jubging.repository.TongRepository;
import Capstone_team1.Jubging.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static Capstone_team1.Jubging.domain.model.TongStatus.OCCUPIED;

@AllArgsConstructor
@Service
public class JubjubiService {
    private final JpaJubjubiRepository jubjubiRepository;
    private final UserRepository userRepository;
    private final TongRepository tongRepository;
    private final JubgingDataRepository jubgingDataRepository;

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

    @Transactional
    public StartJubgingResponseDto startJubging(StartJubgingRequestDto startJubgingRequestDto){
        int tongs_id = startJubgingRequestDto.getTongs_id();
        int jubjubi_id = startJubgingRequestDto.getJubjubi_id();

        //로그인 유저 확인
        Optional<User> user = userRepository.findByEmail(SecurityUtil.getCurrentUserEmail());
        if(user.isEmpty()){
            throw new NotFoundException(ErrorCode.NOT_FOUND_USER, "로그인 유저 정보가 없습니다.");
        }
        //집게id 확인
        Optional<Tong> tong = tongRepository.findById(tongs_id);
        if(tong.isEmpty()){
            throw new NotFoundException(ErrorCode.NOT_FOUND_TONG, "집게 정보가 없습니다.");
        }
        //이미 사용중 인 경우 예외 처리
        //OCCUPIED, UNOCCUPIED

        //새로운 줍깅 데이터 생성
        JubgingData newJubgingData = JubgingData.create(user.get(),tong.get());
        jubgingDataRepository.create(newJubgingData);
        //집게 테이블 status 변경
        tong.get().updateStatus(OCCUPIED);
        tongRepository.update(tong.get());
        //줍줍이 집게, 봉투 카운트 --
        Jubjubi currentJubjubi = jubjubiRepository.findById(jubjubi_id)
                .map(Jubjubi::serve)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_JUBJUBI, "줍줍이 정보가 없습니다."));
        jubjubiRepository.save(currentJubjubi);

        return new StartJubgingResponseDto(currentJubjubi.getTongs_unlock_key());
    }
}
