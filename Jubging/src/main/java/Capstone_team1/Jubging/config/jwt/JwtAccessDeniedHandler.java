package Capstone_team1.Jubging.config.jwt;

import Capstone_team1.Jubging.config.exception.ErrorCode;
import Capstone_team1.Jubging.config.exception.NotFoundException;
import Capstone_team1.Jubging.config.exception.UnauthorizedException;
import Capstone_team1.Jubging.config.utils.SecurityUtil;
import Capstone_team1.Jubging.domain.User;
import Capstone_team1.Jubging.domain.model.UserState;
import Capstone_team1.Jubging.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final UserRepository userRepository;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        //필요한 권한이 없이 접근하려 할때 403
        User user = this.userRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER, "현재 접속한 유저를 찾을 수 없습니다."));

        if(user.getState() == UserState.REJECT){
            throw new UnauthorizedException(ErrorCode.REJECT_USER, "거부된 유저입니다.");
        }
        else if(user.getState() == UserState.INACTIVE){
            throw new UnauthorizedException(ErrorCode.INACTIVE_USER, "비활성화 된 유저입니다.");
        }
    }
}
