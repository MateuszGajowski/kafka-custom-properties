package pl.gajowski.mateusz.kafka.customproperties;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Properties;

public class CustomPropertiesKafkaListenerAnnotationBeanPostProcessor<K, V> extends KafkaListenerAnnotationBeanPostProcessor<K, V> implements EnvironmentAware {

    private Environment environment;

    @Override
    protected void processListener(MethodKafkaListenerEndpoint endpoint, KafkaListener kafkaListener, Object bean, Object adminTarget, String beanName) {
        if (adminTarget instanceof Method) {
            Optional.ofNullable(((Method) adminTarget).getAnnotation(KafkaConsumerPropertiesSource.class))
                    .map(it -> Binder.get(environment).bind(it.prefix(), Bindable.of(KafkaProperties.class)).orElse(null))
                    .map(KafkaProperties::getConsumer)
                    .map(KafkaProperties.Consumer::buildProperties)
                    .map(it -> {
                        Properties properties = new Properties();
                        properties.putAll(it);
                        return properties;
                    }).ifPresent(endpoint::setConsumerProperties);
        }

        super.processListener(endpoint, kafkaListener, bean, adminTarget, beanName);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
