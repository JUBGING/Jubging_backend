package Capstone_team1.Jubging.repository;

import Capstone_team1.Jubging.config.exception.ConflictException;
import Capstone_team1.Jubging.config.exception.ErrorCode;
import Capstone_team1.Jubging.domain.Dibs;
import Capstone_team1.Jubging.domain.Product;
import Capstone_team1.Jubging.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DibsRepository {

    private final JpaDibsRepository jpaDibsRepository;

    public Optional<Dibs> findById(User user, Product product){
        return this.jpaDibsRepository.findByUser_idAndProduct_id(user.getId(), product.getId());
    }

    public List<Dibs> findByUserId(User user)
    {
        return this.jpaDibsRepository.findByUser_id(user.getId());
    }

    public Optional<Dibs> update(User user, Product product, Dibs updateDibs){
        return this.jpaDibsRepository.findByUser_idAndProduct_id(user.getId(), product.getId()).map(
                srcDibs ->{
                    srcDibs.update(updateDibs);
                    return this.jpaDibsRepository.save(srcDibs);
                }
        );
    }
    public void delete(User user, Product product) {
        try {
            this.jpaDibsRepository.deleteByUser_idAndProduct_id(user.getId(), product.getId());
        }
        catch (Exception e){
            throw new ConflictException(ErrorCode.DELETE_FAIL, "장바구니 삭제를 실패하셨습니다");
        }
    }

    public Dibs create(Dibs newDibs) {
        return this.jpaDibsRepository.save(newDibs);
    }

}
