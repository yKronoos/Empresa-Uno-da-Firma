package br.edu.ifgoiano.unodafirma.consumer;

import java.util.Date;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.ifgoiano.unodafirma.service.RabbitMQService;
import constants.RabbitMQConstants;
import dto.ProductDto;
import dto.ReportDto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ProductConsumer {

	@Autowired
	private RabbitMQService rabbitMQService;

	@RabbitListener(queues = RabbitMQConstants.QUEUE_LIGHT_PRODUCST)
	private void consumeHeavyProducts(String message) throws JsonMappingException, JsonProcessingException {
		ProductDto productDto = new ObjectMapper().readValue(message, ProductDto.class);
		
		System.out.println(productDto.id + ", " + productDto.name + ", " + productDto.sender + ", "
				+ productDto.receiver + ", " + productDto.weight);
		
		ReportDto reportDto = new ReportDto();
		reportDto.id = UUID.randomUUID().toString();
		reportDto.productId = productDto.id;
		reportDto.enterprise = "Uno da Firma";
		reportDto.deliveryDate = new Date().getTime() + "";
		
		this.rabbitMQService.sendMessage(RabbitMQConstants.QUEUE_RESPONSE, reportDto);
	}
	
}
