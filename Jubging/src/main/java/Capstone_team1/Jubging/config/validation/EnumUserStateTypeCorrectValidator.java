package Capstone_team1.Jubging.config.validation;

import Capstone_team1.Jubging.config.exception.BadRequestException;
import Capstone_team1.Jubging.config.exception.ErrorCode;
import Capstone_team1.Jubging.domain.model.UserState;

public class EnumUserStateTypeCorrectValidator extends AbstractValidator{

    private final String status;

    public EnumUserStateTypeCorrectValidator(String status) {
        this.status = status;
    }

    public static EnumUserStateTypeCorrectValidator of(String status) {
        return new EnumUserStateTypeCorrectValidator(status);
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
