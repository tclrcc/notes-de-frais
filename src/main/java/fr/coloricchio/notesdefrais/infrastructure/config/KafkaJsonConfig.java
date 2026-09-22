package fr.coloricchio.notesdefrais.infrastructure.config;

import fr.coloricchio.notesdefrais.infrastructure.out.messaging.EvenementNoteMessage;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;

/**
 * Sérialisation JSON pour Kafka
 */
@Configuration
public class KafkaJsonConfig {

    private final JsonMapper mapper;

    public KafkaJsonConfig(JsonMapper mapper) {
        this.mapper = mapper;
    }

    static class JacksonSerializer<T> implements Serializer<T> {
        private final JsonMapper mapper;
        JacksonSerializer(JsonMapper mapper) {this.mapper = mapper;}

        @Override
        public byte[] serialize(String topic, T donnee) {
            return donnee == null ? null : mapper.writeValueAsBytes(donnee);
        }
    }

    static class JacksonDeserializer<T> implements Deserializer<T> {
        private final JsonMapper mapper;
        private final Class<T> type;
        JacksonDeserializer(JsonMapper mapper, Class<T> type) {
            this.mapper = mapper;
            this.type = type;
        }

        @Override
        public T deserialize(String topic, byte[] donnees) {
            return donnees == null ? null : mapper.readValue(donnees, type);
        }
    }

    @Bean
    public ProducerFactory<String, EvenementNoteMessage> producerFactory(KafkaProperties properties) {
        Map<String, Object> config = properties.buildProducerProperties();
        return new DefaultKafkaProducerFactory<>(
                config, new StringSerializer(), new JacksonSerializer<>(mapper));
    }

    @Bean
    public KafkaTemplate<String, EvenementNoteMessage> kafkaTemplate(
            ProducerFactory<String, EvenementNoteMessage> factory) {
        return new KafkaTemplate<>(factory);
    }

    @Bean
    public ConsumerFactory<String, EvenementNoteMessage> consumerFactory(KafkaProperties properties) {
        Map<String, Object> config = properties.buildConsumerProperties();
        return new DefaultKafkaConsumerFactory<>(
                config, new StringDeserializer(),
                new JacksonDeserializer<>(mapper, EvenementNoteMessage.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EvenementNoteMessage> kafkaListenerContainerFactory(
            ConsumerFactory<String, EvenementNoteMessage> consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, EvenementNoteMessage>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
