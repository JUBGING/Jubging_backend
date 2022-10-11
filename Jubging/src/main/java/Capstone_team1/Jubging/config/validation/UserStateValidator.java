package Capstone_team1.Jubging.config.validation;


import Capstone_team1.Jubging.config.exception.ErrorCode;
import Capstone_team1.Jubging.config.exception.UnauthorizedException;
import Capstone_team1.Jubging.domain.model.UserState;

public class UserStateValidator extends AbstractValidator {

    private final UserState userState;

    private UserStateValidator(UserState userState) {
        this.userState = userState;
    }

    public static UserStateValidator of(UserState userState) {
        return new UserStateValidator(userState);
    }

    @Override
    public void validate() {
        if (this.userState == UserState.INACTIVE) {
            throw new UnauthorizedException(
                    ErrorCode.INACTIVE_USER,
                    "비활성화된 사용자 입니다."
            );
        }

        if (this.userState == UserState.REJECT) {
            throw new UnauthorizedException(
                    ErrorCode.REJECT_USER,
                    "거부된 사용자 입니다."
            );
        }
        if(this.userState == UserState.LEAVE)
        {
            throw new UnauthorizedException(
                    ErrorCode.LEAVE_USER,
                    "회원탈퇴한 사용자 입니다."
            );
        }
    }
}
