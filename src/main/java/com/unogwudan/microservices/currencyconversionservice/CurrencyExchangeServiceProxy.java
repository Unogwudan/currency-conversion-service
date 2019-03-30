package com.unogwudan.microservices.currencyconversionservice;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.unogwudan.microservices.currencyconversionservice.models.CurrencyConversionBean;

//@FeignClient(name = "currency-exchange-service")
@RibbonClient(name = "currency-exchange-service")
@FeignClient(name = "netflix-zuul-api-gateway-server")
public interface CurrencyExchangeServiceProxy {

	@GetMapping("currency-exchange-service/currency-exchange/from/{from}/to/{to}")
	public CurrencyConversionBean convertCurrency(@PathVariable String from, @PathVariable String to);
}
