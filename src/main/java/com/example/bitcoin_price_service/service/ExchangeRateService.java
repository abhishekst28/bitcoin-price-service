package com.example.bitcoin_price_service.service;

import com.example.bitcoin_price_service.model.ExchangeRateResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExchangeRateService {

    private final RestTemplate restTemplate = new RestTemplate();

    public ExchangeRateResponse fetchCurrentRates() {
        String apiUrl = "https://api.exchangerate-api.com/v4/latest/USD"; // Or any other reliable API

        ExchangeRateResponse response = restTemplate.getForObject(apiUrl, ExchangeRateResponse.class);

        return response;
    }
}

