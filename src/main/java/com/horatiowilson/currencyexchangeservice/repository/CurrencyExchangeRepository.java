package com.horatiowilson.currencyexchangeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchangeRepositoryImpl, Long> {

    CurrencyExchangeRepositoryImpl findByFromAndTo(String from, String to);

}
