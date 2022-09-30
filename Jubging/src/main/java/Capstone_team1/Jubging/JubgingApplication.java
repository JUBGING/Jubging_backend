package Capstone_team1.Jubging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JubgingApplication {

	public static void main(String[] args) {
		SpringApplication.run(JubgingApplication.class, args);
	}

}
