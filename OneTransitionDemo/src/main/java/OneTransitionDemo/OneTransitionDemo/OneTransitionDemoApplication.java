package OneTransitionDemo.OneTransitionDemo;

import OneTransitionDemo.OneTransitionDemo.Services.AutoTimer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EntityScan("OneTransitionDemo.OneTransitionDemo.Models")
@EnableJpaAuditing
public class OneTransitionDemoApplication {

	private final ObjectMapper objectMapper;
	private final AutoTimer autoTimer; // inject your service

	public OneTransitionDemoApplication(ObjectMapper objectMapper, AutoTimer autoTimer) {
		this.objectMapper = objectMapper;
		this.autoTimer = autoTimer;
	}

	@PostConstruct
	public void setup() {
		objectMapper.registerModule(new JavaTimeModule());

		// Call scheduler manually once at startup
		autoTimer.updateExamStatuses();
	}

	public static void main(String[] args) {
		SpringApplication.run(OneTransitionDemoApplication.class, args);
	}
}

