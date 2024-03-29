package com.horatiowilson.currencyexchangeservice.controller;

import com.horatiowilson.currencyexchangeservice.dto.CurrencyExchangeDto;
import com.horatiowilson.currencyexchangeservice.repository.CurrencyExchangeRepositoryImpl;
import com.horatiowilson.currencyexchangeservice.repository.CurrencyExchangeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyExchangeController {
	
	private Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);
	
	@Autowired
	private CurrencyExchangeRepository repository;
	
	@Autowired
	private Environment environment;
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyExchangeDto retrieveExchangeValue(
			@PathVariable String from,
			@PathVariable String to) {
		logger.info("retrieveExchangeValue called with {} to {}", from, to);
		CurrencyExchangeRepositoryImpl currencyExchangeRepositoryImpl = repository.findByFromAndTo(from, to);
		if(currencyExchangeRepositoryImpl ==null) {
			throw new RuntimeException
				("Unable to Find data for " + from + " to " + to);
		}
		CurrencyExchangeDto currencyExchange = entityToModel(currencyExchangeRepositoryImpl);
		String port = environment.getProperty("local.server.port");
		String host = environment.getProperty("HOSTNAME");
		String version = "v0.0.2";
		currencyExchange.setEnvironment(port + " " + version + " " + host);
		return currencyExchange;
	}

	private CurrencyExchangeDto entityToModel(CurrencyExchangeRepositoryImpl currencyExchangeRepositoryImpl) {
		CurrencyExchangeDto currencyExchangeDto = new CurrencyExchangeDto();
		currencyExchangeDto.setId(currencyExchangeRepositoryImpl.getId());
		currencyExchangeDto.setFrom(currencyExchangeRepositoryImpl.getFrom());
		currencyExchangeDto.setTo(currencyExchangeRepositoryImpl.getTo());
		currencyExchangeDto.setConversionMultiple(currencyExchangeRepositoryImpl.getConversionMultiple());
		currencyExchangeDto.setEnvironment(currencyExchangeRepositoryImpl.getEnvironment());
		return currencyExchangeDto;
	}
}
