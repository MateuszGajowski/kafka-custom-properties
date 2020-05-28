package pl.gajowski.mateusz.kafka.customproperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Configuration
@Import(KafkaCustomPropertiesConfig.class)
public class KafkaCustomPropertiesApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaCustomPropertiesApplication.class, args);
    }
}
