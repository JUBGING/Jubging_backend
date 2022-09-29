package Capstone_team1.Jubging.infrastructure;

import Capstone_team1.Jubging.config.exception.ConflictException;
import Capstone_team1.Jubging.config.exception.ErrorCode;
import Capstone_team1.Jubging.domain.User;
import Capstone_team1.Jubging.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverMailSender implements MailSender{

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public void sendPassword(User user){

        log.info("임시 비밀번호 발급 시작");

        String tempPassword = getTempPassword();
        user.updatePassword(passwordEncoder.encode(tempPassword));

        User updateUser = this.userRepository.update(user).orElseThrow(() -> {
                    throw new ConflictException(ErrorCode.UPDATE_FAIL, "임시 비밀번호 업데이트를 실패했습니다.");
                }
        );

        //메세지 생성하고 보낼 메일 설정 저장
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(updateUser.getEmail());
        message.setFrom(sender);

        message.setSubject(updateUser.getName()+" : New Temporary Password is here!");
        message.setText("Hello" + updateUser.getName() + "! We send your temporary password here. \nBut this is not secured so please change password once you sign into our site. \nPassword : " + tempPassword);
        javaMailSender.send(message);
        log.info("임시 비밀번호 발급 완료");
    }

    //임시 비밀번호 발급
    @Override
    public String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
                'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '!', '@', '#', '$', '%', '^', '&', '*', '(', ')'};

        char[] num = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        char[] alphabet = new char[] { 'a', 'b', 'c', 'd', 'e', 'f',
                'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        char[] special = new char[] {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')'};


        StringBuilder str = new StringBuilder();
        int idx = 0;

        //숫자
        idx = (int) (num.length * Math.random());
        str.append(num[idx]);
        //문자
        idx = (int) (alphabet.length * Math.random());
        str.append(alphabet[idx]);
        //특수문자
        idx = (int) (special.length * Math.random());
        str.append(special[idx]);

        for (int i = 3; i < 13; i++) {
            idx = (int) (charSet.length * Math.random());
            str.append(charSet[idx]);
        }
        return str.toString();
    }
}
