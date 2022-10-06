package Capstone_team1.Jubging.repository;

import Capstone_team1.Jubging.config.exception.ConflictException;
import Capstone_team1.Jubging.config.exception.ErrorCode;
import Capstone_team1.Jubging.domain.Points;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PointsRepository {
    private final JpaPointsRepository jpaPointsRepository;

    public Optional<Points> findById(String id) {
        return this.jpaPointsRepository.findById(id);
    }

    public Points create(Points newPoints) {
        return this.jpaPointsRepository.save(newPoints);
    }

    public Optional<Points> update(Points updatePoints) {

        return this.jpaPointsRepository.findById(updatePoints.getId()).map(
                srcPoints -> {
                    srcPoints.update(srcPoints.getCurrent_points(),
                            srcPoints.getTotal_points());
                    return this.jpaPointsRepository.save(srcPoints);
                }
        );
    }

    public void delete(Points points) {
        try {
            this.jpaPointsRepository.deleteById(points.getId());
        }
        catch (Exception e){
            throw new ConflictException(ErrorCode.DELETE_FAIL, "회원 탈퇴를 실패하셨습니다");
        }
    }
}

