package pl.gajowski.mateusz.kafka.customproperties;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaExampleTopics {
    public static final String TEST_TOPIC_CUSTOM_PROPERTIES = "TEST-TOPIC-CUSTOM-PROPERTIES";
    public static final String TEST_TOPIC = "TEST-TOPIC";

    @KafkaListener(topics = TEST_TOPIC_CUSTOM_PROPERTIES, groupId = "test-group")
    @KafkaConsumerPropertiesSource(prefix = "example.prefix")
    public void handleTestEventWithCustomProperties(String event) {
    }

    @KafkaListener(topics = TEST_TOPIC, groupId = "test-group")
    public void handleTestEvent(String event) {
    }
}
