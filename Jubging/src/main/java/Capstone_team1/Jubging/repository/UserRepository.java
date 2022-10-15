package Capstone_team1.Jubging.repository;

import Capstone_team1.Jubging.config.exception.ConflictException;
import Capstone_team1.Jubging.config.exception.ErrorCode;
import Capstone_team1.Jubging.domain.User;
import Capstone_team1.Jubging.dto.user.FindUserInfoDto;
import Capstone_team1.Jubging.dto.user.UserPurchaseDetailsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepository {
    private final JpaUserRepository jpaUserRepository;

    public List<User> findEmail(String name) {
        return this.jpaUserRepository.findByName(name);
    }

    public Optional<User> findForPassword(String email, String name) {
        return this.jpaUserRepository.findByEmailAndName(email, name).stream().findFirst();
    }

    public Optional<User> findById(String id) {
        return this.jpaUserRepository.findById(id).stream().findFirst();
    }

    public List<User> findByName(String name) {
        return this.jpaUserRepository.findByName(name);
    }

    public Optional<User> findByEmail(String email) {
        return this.jpaUserRepository.findByEmail(email);
    }

    public User create(User newUser) {
        return this.jpaUserRepository.save(newUser);
    }

    public Optional<User> update(User updateUser) {

        return this.jpaUserRepository.findById(updateUser.getId()).map(
                srcUser -> {
                    srcUser.update(updateUser.getName(),
                            updateUser.getPassword(),
                            updateUser.getProfileImageUrl(),
                            updateUser.getRole(),
                            updateUser.getState(),
                            updateUser.getPoints()
                    );
                    return this.jpaUserRepository.save(srcUser);
                }
        );
    }

    public void delete(User user) {
        try {
            this.jpaUserRepository.deleteById(user.getId());
        }
        catch (Exception e){
            throw new ConflictException(ErrorCode.DELETE_FAIL, "회원 탈퇴를 실패하셨습니다");
        }
    }

    public Optional<FindUserInfoDto> findUserInfo(String email)
    {
        return this.jpaUserRepository.findUserInfo(email);
    }

    public List<UserPurchaseDetailsResponseDto> findOrderInfo(String email) {return this.jpaUserRepository.findOrderInfo(email);}
}
