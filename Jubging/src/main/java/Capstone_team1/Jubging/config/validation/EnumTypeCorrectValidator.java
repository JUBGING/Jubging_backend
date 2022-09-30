package Capstone_team1.Jubging.config.validation;

import Capstone_team1.Jubging.config.exception.BadRequestException;
import Capstone_team1.Jubging.config.exception.ErrorCode;
import Capstone_team1.Jubging.domain.model.UserState;
import org.springframework.security.crypto.password.PasswordEncoder;

public class EnumTypeCorrectValidator extends AbstractValidator{

    private final String status;

    public EnumTypeCorrectValidator(String status) {
        this.status = status;
    }

    public static EnumTypeCorrectValidator of(String status) {
        return new EnumTypeCorrectValidator(status);
    }

    @Override
    public void validate() {

        for (UserState u : UserState.values()) {
                if (u.name().equals(status)) {
                    return;
                }
        }
        throw new BadRequestException(ErrorCode.INCORRECT_STATUS, "잘못된 status를 보냈습니다.");
    }
}
