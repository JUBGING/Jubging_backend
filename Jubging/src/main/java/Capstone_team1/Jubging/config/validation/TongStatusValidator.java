package Capstone_team1.Jubging.config.validation;


import Capstone_team1.Jubging.config.exception.ErrorCode;
import Capstone_team1.Jubging.config.exception.UnauthorizedException;
import Capstone_team1.Jubging.domain.model.TongStatus;
import Capstone_team1.Jubging.domain.model.UserState;

public class TongStatusValidator extends AbstractValidator {

    private final TongStatus tongStatus;

    private TongStatusValidator(TongStatus tongStatus) { this.tongStatus = tongStatus; }

    public static TongStatusValidator of(TongStatus tongStatus) {
        return new TongStatusValidator(tongStatus);
    }

    @Override
    public void validate() {
        if (this.tongStatus == TongStatus.OCCUPIED) {
            throw new UnauthorizedException(
                    ErrorCode.ALREADY_OCCUPIED_TONG,
                    "이미 사용중인 집게 입니다."
            );
        }
    }
}
