package Capstone_team1.Jubging.config.validation;


import Capstone_team1.Jubging.config.exception.BadRequestException;
import Capstone_team1.Jubging.config.exception.ErrorCode;
import Capstone_team1.Jubging.config.exception.UnauthorizedException;
import Capstone_team1.Jubging.domain.model.TongStatus;

public class UserPositionValidator extends AbstractValidator {

    private final String userPosition;

    private UserPositionValidator(String userPosition) { this.userPosition = userPosition; }

    public static UserPositionValidator of(String userPosition) {
        return new UserPositionValidator(userPosition);
    }

    @Override
    public void validate() {
        String pattern = "^\\d*.\\d*,\\d*.\\d*\\s\\d*.\\d*,\\d*.\\d*";
        if(!userPosition.matches(pattern)){
            throw new BadRequestException(ErrorCode.INVALID_INPUT_VALUE, "userPosition의 형식이 맞지 않습니다.");
        }
    }
}
