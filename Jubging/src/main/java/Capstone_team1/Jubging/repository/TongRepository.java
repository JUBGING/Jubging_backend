package Capstone_team1.Jubging.repository;

import Capstone_team1.Jubging.domain.Tong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TongRepository {
    private final JpaTongRepository jpaTongRepository;

    public Optional<Tong> findById(int tongs_id){return this.jpaTongRepository.findById(tongs_id);}

    public Optional<Tong> update(Tong updateTong){
        return this.jpaTongRepository.findById(updateTong.getTongs_id()).map(
            srcTong ->{
                srcTong.updateStatus(updateTong.getStatus());
                return this.jpaTongRepository.save(srcTong);
            }
        );
    }
}
