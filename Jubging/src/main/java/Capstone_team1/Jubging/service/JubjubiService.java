package Capstone_team1.Jubging.service;

import Capstone_team1.Jubging.config.exception.BaseRuntimeException;
import Capstone_team1.Jubging.config.exception.ErrorCode;
import Capstone_team1.Jubging.config.exception.NotFoundException;
import Capstone_team1.Jubging.config.utils.SecurityUtil;
import Capstone_team1.Jubging.config.validation.JubjubiStatusValidator;
import Capstone_team1.Jubging.config.validation.TongStatusValidator;
import Capstone_team1.Jubging.config.validation.UserPositionValidator;
import Capstone_team1.Jubging.config.validation.UserStateValidator;
import Capstone_team1.Jubging.config.validation.ValidatorBucket;
import Capstone_team1.Jubging.domain.JubgingData;
import Capstone_team1.Jubging.domain.Jubjubi;
import Capstone_team1.Jubging.domain.Tong;
import Capstone_team1.Jubging.domain.User;
import Capstone_team1.Jubging.domain.model.JubgingDataStatus;
import Capstone_team1.Jubging.dto.jubjubi.*;
import Capstone_team1.Jubging.repository.JpaJubjubiRepository;
import Capstone_team1.Jubging.repository.JubgingDataRepository;
import Capstone_team1.Jubging.repository.TongRepository;
import Capstone_team1.Jubging.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static Capstone_team1.Jubging.domain.model.JubgingDataStatus.FINISHED;
import static Capstone_team1.Jubging.domain.model.JubgingDataStatus.INPROGRESS;
import static Capstone_team1.Jubging.domain.model.TongStatus.OCCUPIED;
import static Capstone_team1.Jubging.domain.model.TongStatus.UNOCCUPIED;

@AllArgsConstructor
@Service
public class JubjubiService {
    private final JpaJubjubiRepository jubjubiRepository;
    private final UserRepository userRepository;
    private final TongRepository tongRepository;
    private final JubgingDataRepository jubgingDataRepository;
    private final S3Service s3Service;

    public List<JubjubiResponseDto> findByUserPosition(String userPosition){
        User currentUser = userRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER, "로그인 유저 정보가 없습니다."));

        ValidatorBucket validatorBucket = ValidatorBucket.of();
        validatorBucket
                .consistOf(UserStateValidator.of(currentUser.getState()))
                .consistOf(UserPositionValidator.of(userPosition));

        validatorBucket.validate();

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
        //로그인 유저 확인
        User user = userRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER, "로그인 유저 정보가 없습니다."));
        //집게id 확인
        Tong tong = tongRepository.findById(tongs_id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_TONG, "집게 정보가 없습니다."));
        //줍줍이 id 확인 및 봉투, 집게 카운트 --
        Jubjubi currentJubjubi = jubjubiRepository.findById(jubjubi_id)
                .map(Jubjubi::serve)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_JUBJUBI, "줍줍이 정보가 없습니다."));
        //로그인 중인 유저 status 확인
        validatorBucket
                .consistOf(UserStateValidator.of(user.getState()))          //유저 status 확인
                .consistOf(TongStatusValidator.of(tong.getStatus()))        //사용중인 집게인지 확인
                .consistOf(JubjubiStatusValidator.of(currentJubjubi.getStatus()));
        validatorBucket.validate();
        //새로운 줍깅 데이터 생성
        JubgingData newJubgingData = JubgingData.create(user,tong);
        jubgingDataRepository.create(newJubgingData);
        //집게 테이블 status 변경
        tong.updateStatus(OCCUPIED);
        tongRepository.update(tong);
        //줍줍이 변경 내용 저장
        jubjubiRepository.save(currentJubjubi);

        return new StartJubgingResponseDto(currentJubjubi.getTongs_unlock_key());
    }

    @Transactional
    public EndJubgingResponseDto endJubging(EndJubgingRequestDto endJubgingRequestDto) {
        int tongs_id = endJubgingRequestDto.getTongs_id();
        int jubjubi_id = endJubgingRequestDto.getJubjubi_id();

        ValidatorBucket validatorBucket = ValidatorBucket.of();

        //로그인 유저 확인
        User user = userRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER, "로그인 유저 정보가 없습니다."));

        Tong tong = tongRepository.findById(tongs_id)
               .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_TONG, "집게 정보가 없습니다."));

        Jubjubi jubjubi = jubjubiRepository.findById(jubjubi_id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_JUBJUBI, "줍줍이 정보가 없습니다."));

        JubgingData jubgingData = jubgingDataRepository.findJubgingDataInProgress(user.getId(), INPROGRESS)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_JUBGING_DATA, "진행 중인 줍깅 정보가 없습니다."));

        //집게 테이블에 반환 정보 update
        tong.updateStatus(UNOCCUPIED);
        tongRepository.update(tong);

        //줍깅데이타 테이블에 반환 정보 update
        jubgingData.update(
                endJubgingRequestDto.getWeight(),
                endJubgingRequestDto.getStep_cnt(),
                endJubgingRequestDto.getDistance(),
                endJubgingRequestDto.getCalorie(),
                user,
                tong,
                endJubgingRequestDto.isTongs_return(),
                FINISHED);
        jubgingDataRepository.create(jubgingData);

        return EndJubgingResponseDto.of(jubgingData);
    }

    public SendImageResponseDto sendImage(MultipartFile image){
        User user = userRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER, "로그인 유저 정보가 없습니다."));
        String userEmail = user.getEmail();
        String url;
        //사진 s3에 저장
        url = s3Service.uploadFile(image, userEmail);

        //url db에
        JubgingData jubgingData = jubgingDataRepository.findJubgingDataInProgress(user.getId(), FINISHED)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_JUBGING_DATA, "진행 중인 줍깅 정보가 없습니다."));

        jubgingData.updateImage(url);
        jubgingDataRepository.create(jubgingData);


        return new SendImageResponseDto(url);
    }
}
