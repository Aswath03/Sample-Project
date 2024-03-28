package com.cars4u.sample.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@ConditionalOnProperty(prefix="service.kafka" , name="enabled" , matchIfMissing = false)
public class KafkaConsumerConfiguration {

	@Value("${kafka.bootstrap-servers}")
	private String bootstrapServers;

	private Map<String, Object> consumerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 200);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 3000);
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 60000);
		return props;
	}
	
	public ConsumerFactory<String, String> consumerFactoryInfo(){
		JsonDeserializer<String> deserializer = new JsonDeserializer<>(String.class);
		deserializer.setRemoveTypeHeaders(false);
		deserializer.addTrustedPackages("*");
		deserializer.setUseTypeMapperForKey(true);

		ErrorHandlingDeserializer<String> keyDeserializer = new ErrorHandlingDeserializer<>(new StringDeserializer());
		ErrorHandlingDeserializer<String> valueDeserializer = new ErrorHandlingDeserializer<>(deserializer);

		Map<String, Object> props = consumerConfigs();
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "INFO_USER");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
		
		return new DefaultKafkaConsumerFactory<>(props, keyDeserializer, valueDeserializer);
		
	}
	
	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactoryInfo() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactoryInfo());
		factory.setCommonErrorHandler(new DefaultErrorHandler());
		factory.getContainerProperties().setAckMode(AckMode.MANUAL_IMMEDIATE);
		factory.getContainerProperties().setSyncCommits(false);
		return factory;
	}
	
}
