package com.cars4u.sample.kafka.consumer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.cars4u.sample.common.KafkaConstants;

import lombok.extern.slf4j.Slf4j;

@Component
@ConditionalOnProperty(prefix = "service.kafka", name = "enabled", matchIfMissing = false)
@Slf4j
public class KafkaConsumer {
	
	@KafkaListener(topics = KafkaConstants.TOPIC_INFO, containerFactory = "kafkaListenerContainerFactoryInfo")
	public void listenKafkaMessageUser(String message, Acknowledgment ack) {
		log.info("INFO ID: {} Listening to INFO Event on topic '{}'", message);
		ack.acknowledge();
	}
	
}
