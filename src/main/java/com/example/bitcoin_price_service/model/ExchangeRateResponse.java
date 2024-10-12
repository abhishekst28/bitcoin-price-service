package com.example.bitcoin_price_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateResponse {
    private String base;
    private String date;
    private Map<String, Double> rates;
}

