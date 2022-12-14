package Capstone_team1.Jubging.service;

import Capstone_team1.Jubging.config.exception.ErrorCode;
import Capstone_team1.Jubging.config.exception.NotFoundException;
import Capstone_team1.Jubging.config.utils.SecurityUtil;
import Capstone_team1.Jubging.config.validation.TongStatusValidator;
import Capstone_team1.Jubging.config.validation.UserStateValidator;
import Capstone_team1.Jubging.config.validation.ValidatorBucket;
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

        ValidatorBucket validatorBucket = ValidatorBucket.of();
        //????????? ?????? ??????
        User user = userRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER, "????????? ?????? ????????? ????????????."));
        //??????id ??????
        Tong tong = tongRepository.findById(tongs_id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_TONG, "?????? ????????? ????????????."));
        //????????? ?????? ?????? status ??????
        validatorBucket
                .consistOf(UserStateValidator.of(user.getState()))          //?????? status ??????
                .consistOf(TongStatusValidator.of(tong.getStatus()));       //???????????? ???????????? ??????

        validatorBucket.validate();
        //????????? ?????? ????????? ??????
        JubgingData newJubgingData = JubgingData.create(user,tong);
        jubgingDataRepository.create(newJubgingData);
        //?????? ????????? status ??????
        tong.updateStatus(OCCUPIED);
        tongRepository.update(tong);
        //????????? ??????, ?????? ????????? --
        Jubjubi currentJubjubi = jubjubiRepository.findById(jubjubi_id)
                .map(Jubjubi::serve)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_JUBJUBI, "????????? ????????? ????????????."));
        jubjubiRepository.save(currentJubjubi);

        return new StartJubgingResponseDto(currentJubjubi.getTongs_unlock_key());
    }
}
