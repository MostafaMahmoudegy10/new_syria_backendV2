package freelance.new_syria_v2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NewSyriaV2Application {

	public static void main(String[] args) {
		SpringApplication.run(NewSyriaV2Application.class, args);
	}

}
