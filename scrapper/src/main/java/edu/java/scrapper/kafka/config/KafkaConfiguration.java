package edu.java.scrapper.kafka.config;

import edu.java.dto.LinkUpdate;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaConfiguration {

    @Bean
    public ProducerFactory<String, LinkUpdate> producerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.bootstrapServers());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProperties.clientId());
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProperties.acksMode());
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, (int) kafkaProperties.deliveryTimeout().toMillis());
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProperties.lingerMs());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProperties.batchSize());
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, kafkaProperties.maxInFlightPerConnection());
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, kafkaProperties.enableIdempotence());
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, LinkUpdate> kafkaTemplate(ProducerFactory<String, LinkUpdate> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
