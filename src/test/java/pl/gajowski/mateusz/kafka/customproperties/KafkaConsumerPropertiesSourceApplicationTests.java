package pl.gajowski.mateusz.kafka.customproperties;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.gajowski.mateusz.kafka.customproperties.KafkaExampleTopics.TEST_TOPIC;
import static pl.gajowski.mateusz.kafka.customproperties.KafkaExampleTopics.TEST_TOPIC_CUSTOM_PROPERTIES;

@EmbeddedKafka(topics = {TEST_TOPIC_CUSTOM_PROPERTIES, TEST_TOPIC})
@DirtiesContext
@SpringBootTest
@RunWith(SpringRunner.class)
class KafkaConsumerPropertiesSourceApplicationTests {

    static {
        System.setProperty(EmbeddedKafkaBroker.BROKER_LIST_PROPERTY,
                "spring.kafka.bootstrap-servers");
    }

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    
    @Test
    void givenTopicWithCustomPropertiesSetup_customConfigLoaded() {
        Integer testTopicCustomPropertiesMaxPollRecords = getMaxPollRecordsConfig(TEST_TOPIC_CUSTOM_PROPERTIES);

        assertThat(testTopicCustomPropertiesMaxPollRecords).isEqualTo(5);
    }

    @Test
    void givenTopicWithoutCustomPropertiesSetup_defaultConfigLoaded() {
        Integer testTopicMaxPollRecords = getMaxPollRecordsConfig(TEST_TOPIC);

        assertThat(testTopicMaxPollRecords).isNull();
    }

    private Integer getMaxPollRecordsConfig(String topicName) {
        return (Integer) findProperties(topicName).getKafkaConsumerProperties().get("max.poll.records");
    }

    private ContainerProperties findProperties(String topicName) {
        return kafkaListenerEndpointRegistry.getListenerContainers().stream()
                .map(MessageListenerContainer::getContainerProperties)
                .filter(it -> Arrays.asList(it.getTopics()).contains(topicName))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }
}
