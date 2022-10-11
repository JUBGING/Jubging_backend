package Capstone_team1.Jubging.config.validation;


import Capstone_team1.Jubging.config.exception.ErrorCode;
import Capstone_team1.Jubging.config.exception.UnauthorizedException;
import Capstone_team1.Jubging.domain.model.JubjubiStatus;
import Capstone_team1.Jubging.domain.model.TongStatus;

public class JubjubiStatusValidator extends AbstractValidator {

    private final JubjubiStatus jubjubiStatus;

    private JubjubiStatusValidator(JubjubiStatus jubjubiStatus) { this.jubjubiStatus = jubjubiStatus; }

    public static JubjubiStatusValidator of(JubjubiStatus jubjubiStatus) { return new JubjubiStatusValidator(jubjubiStatus); }

    @Override
    public void validate() {
        if (this.jubjubiStatus == JubjubiStatus.INACTIVE) {
            throw new UnauthorizedException(
                    ErrorCode.UNAVAILABLE_JUBJUBI,
                    "사용 할 수 없는 줍줍이 입니다."
            );
        }
    }
}
