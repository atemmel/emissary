package emissarybackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class EmissaryBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmissaryBackendApplication.class, args);
	}

}
