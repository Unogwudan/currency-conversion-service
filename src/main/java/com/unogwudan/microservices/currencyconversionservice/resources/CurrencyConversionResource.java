package com.unogwudan.microservices.currencyconversionservice.resources;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.unogwudan.microservices.currencyconversionservice.CurrencyExchangeServiceProxy;
import com.unogwudan.microservices.currencyconversionservice.models.CurrencyConversionBean;

@RestController
@EnableDiscoveryClient
@EnableFeignClients("com.unogwudan.microservices.currencyconversionservice")
public class CurrencyConversionResource {

	@Autowired
	private CurrencyExchangeServiceProxy currencyExchangeServiceProxy;
	
	Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

	@GetMapping("currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrency(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("to", to);
		uriVariables.put("from", from);

		ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity("http://localhost:8001/currency-exchange/from/{from}/to/{to}",
				CurrencyConversionBean.class, uriVariables);

		CurrencyConversionBean response = responseEntity.getBody();
		
		return new CurrencyConversionBean(response.getId(), from, to, response.getConversionMultiple(), quantity,
				quantity.multiply(response.getConversionMultiple()), response.getPort());

	}
	
	@GetMapping("currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {

		CurrencyConversionBean response = currencyExchangeServiceProxy.convertCurrency(from, to);

		logger.info("{}", response);
		return new CurrencyConversionBean(response.getId(), from, to, response.getConversionMultiple(), quantity,
				quantity.multiply(response.getConversionMultiple()), response.getPort());

	}
}
