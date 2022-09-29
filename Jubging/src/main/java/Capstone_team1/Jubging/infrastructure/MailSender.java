package Capstone_team1.Jubging.infrastructure;


import Capstone_team1.Jubging.domain.User;

public interface MailSender {

     void sendPassword(User user);
     String getTempPassword();
}
