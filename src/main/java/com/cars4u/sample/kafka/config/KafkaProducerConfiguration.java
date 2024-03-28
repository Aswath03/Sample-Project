package com.cars4u.sample.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@ConditionalOnProperty(prefix="service.kafka" , name="enabled" , matchIfMissing = false)
public class KafkaProducerConfiguration {
	
	@Value("${kafka.bootstrap-servers}")
	private String bootstrapServers;
	
	@Bean
	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		props.put(ProducerConfig.ACKS_CONFIG, "1");
		return props;
	}
	@Bean 
	public ProducerFactory<String, String> producerFactoryInfo(){
		return new DefaultKafkaProducerFactory<>(producerConfigs());
		
	}
	
	@Bean 
	public KafkaTemplate<String, String> kafkaTemplateForInfo(){
		return new KafkaTemplate<>(producerFactoryInfo());
		
	}

}
