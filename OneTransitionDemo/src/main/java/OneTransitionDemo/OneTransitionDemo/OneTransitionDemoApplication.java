package OneTransitionDemo.OneTransitionDemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OneTransitionDemoApplication {

	private final ObjectMapper objectMapper;

	public OneTransitionDemoApplication(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@PostConstruct
	public void setup() {
		objectMapper.registerModule(new JavaTimeModule());
	}
	public static void main(String[] args) {
		SpringApplication.run(OneTransitionDemoApplication.class, args);
	}

}
