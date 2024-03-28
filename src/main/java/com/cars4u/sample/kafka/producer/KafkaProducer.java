package com.cars4u.sample.kafka.producer;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.cars4u.sample.common.KafkaConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaProducer {
	
	@Autowired
	KafkaTemplate<String, String> kafkaTemplateForInfo;
	
	public void publishKafkaMessageInfo(String message) {
		String userIdentifier = message.toString();
		
		log.info("INFOID: {} Publishing INFO Event to topic '{}'" , userIdentifier,KafkaConstants.TOPIC_INFO);
		try {
			CompletableFuture<SendResult<String, String>> future = CompletableFuture.supplyAsync(() -> {
			    try {
			        return kafkaTemplateForInfo.send(KafkaConstants.TOPIC_INFO, userIdentifier, message).get();
			    } catch (Exception e) {
			        throw new RuntimeException(e);
			    }
			});

			future.thenAccept(result -> {
			    log.info("INFOID: {} Publishing INFO Event to topic '{}'. Status: SUCCESS.",
			            userIdentifier, KafkaConstants.TOPIC_INFO);
			});

			future.exceptionally(ex -> {
			    log.error("INFOID: {} Publishing INFO Event to topic '{}'. Status: FAILURE. Error Message: {}",
			            userIdentifier, KafkaConstants.TOPIC_INFO, ex);
			    return null;
			});

		} catch (Exception e) {
			log.error("INFOID: {} Publishing INFO Event to topic '{}'. Status: EXCEPTION. Error Message: {}",
					userIdentifier, KafkaConstants.TOPIC_INFO, e);
		}
		
	}
}
