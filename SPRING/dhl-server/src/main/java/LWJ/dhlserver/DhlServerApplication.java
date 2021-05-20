package LWJ.dhlserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DhlServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DhlServerApplication.class, args);
	}

}
