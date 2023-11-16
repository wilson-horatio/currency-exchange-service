package com.horatiowilson.currencyexchangeservice.controller;

import com.horatiowilson.currencyexchangeservice.dto.CurrencyExchangeDto;
import com.horatiowilson.currencyexchangeservice.repository.CurrencyExchangeRepository;
import com.horatiowilson.currencyexchangeservice.repository.CurrencyExchangeRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyExchangeController {

    @Autowired
    private CurrencyExchangeRepository currencyExchangeRepository;

    @Autowired
    private Environment environment;

    @GetMapping("currency-exchange/from/{from}/to/{to}")
    public CurrencyExchangeDto retrieveExchangeValue(
            @PathVariable String from,
            @PathVariable String to) {
        CurrencyExchangeRepositoryImpl currencyExchangeRepositoryImpl = currencyExchangeRepository.findByFromAndTo(from, to);
        if (currencyExchangeRepositoryImpl == null) {
            throw new RuntimeException("Nothing found in db");
        }
        CurrencyExchangeDto currencyExchange = entityToModel(currencyExchangeRepositoryImpl);
        String port = environment.getProperty("local.server.port");
        currencyExchange.setEnvironment(port);
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
