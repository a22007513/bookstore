package rocky.com.example.virtualbookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
public class VirtualBookstoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualBookstoreApplication.class, args);
	}

}
