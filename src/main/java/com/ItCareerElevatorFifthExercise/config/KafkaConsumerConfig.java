package com.ItCareerElevatorFifthExercise.config;

import com.ItCareerElevatorFifthExercise.DTOs.RegisterUserEmailDTO;
import com.ItCareerElevatorFifthExercise.DTOs.SendMailMessageDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.send-email-message-group-id}")
    private String sendEmailMessageGroupId;

    @Value("${spring.kafka.consumer.send-registration-email-group-id}")
    private String sendRegistrationEmailMessageGroupId;

    @Bean
    public ConsumerFactory<String, SendMailMessageDTO> sendMailMessageConsumerFactory() {
        Map<String, Object> properties = new HashMap<>();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, sendEmailMessageGroupId);

        StringDeserializer keyDeserializer = new StringDeserializer();
        var valueDeserializer = new JacksonJsonDeserializer<>(SendMailMessageDTO.class);
        valueDeserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                properties,
                keyDeserializer,
                valueDeserializer
        );
    }

    @Bean
    public ConsumerFactory<String, RegisterUserEmailDTO> registrationEmailConsumerFactory() {
        Map<String, Object> properties = new HashMap<>();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, sendRegistrationEmailMessageGroupId);

        StringDeserializer keyDeserializer = new StringDeserializer();
        var valueDeserializer = new JacksonJsonDeserializer<>(RegisterUserEmailDTO.class);
        valueDeserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                properties,
                keyDeserializer,
                valueDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SendMailMessageDTO> emailMessageKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, SendMailMessageDTO>();
        factory.setConsumerFactory(sendMailMessageConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RegisterUserEmailDTO> registrationMessageKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, RegisterUserEmailDTO>();
        factory.setConsumerFactory(registrationEmailConsumerFactory());
        return factory;
    }
}
