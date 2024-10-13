package com.example.bitcoinpriceservice.service;


import com.example.bitcoinpriceservice.model.BitcoinPriceCache;
import com.example.bitcoinpriceservice.model.HistoricalPriceRequest;
import com.example.bitcoinpriceservice.model.HistoricalPriceResponse;
import com.example.bitcoinpriceservice.repository.BitcoinPriceCacheRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BitcoinPriceServiceTest {

    @InjectMocks
    private BitcoinPriceService bitcoinPriceService;

    @Mock
    private BitcoinPriceCacheRepository cacheRepository;

    @Mock
    private ExchangeRateService exchangeRateService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetHistoricalPricesWithCache() throws JsonProcessingException {
        // Arrange
        HistoricalPriceRequest request = new HistoricalPriceRequest();
        request.setStartDate("2023-01-01");
        request.setEndDate("2023-01-10");
        request.setCurrency("USD");

        LocalDate startDate = LocalDate.parse(request.getStartDate());
        LocalDate endDate = LocalDate.parse(request.getEndDate());

        BitcoinPriceCache cache = new BitcoinPriceCache();
        cache.setStartDate(startDate);
        cache.setEndDate(endDate);
        cache.setCurrency("USD");
        cache.setJsonData("{\"prices\":[],\"minPrice\":100.0,\"maxPrice\":200.0}");

        when(cacheRepository.findByStartDateAndEndDateAndCurrency(startDate, endDate, "USD")).thenReturn(Optional.of(cache));
        when(objectMapper.readValue(any(String.class), any(Class.class))).thenReturn(new HistoricalPriceResponse(null, 2.0, 5.0));

        // Act
        HistoricalPriceResponse response = bitcoinPriceService.getHistoricalPrices(request);

        // Assert
        assertNotNull(response);
    }

    @Test
    public void testGetHistoricalPricesOfflineModeWithoutCache() {
        // Arrange
        bitcoinPriceService.setOfflineMode(true);

        HistoricalPriceRequest request = new HistoricalPriceRequest();
        request.setStartDate("2023-01-01");
        request.setEndDate("2023-01-10");
        request.setCurrency("USD");

        LocalDate startDate = LocalDate.parse(request.getStartDate());
        LocalDate endDate = LocalDate.parse(request.getEndDate());

        when(cacheRepository.findByStartDateAndEndDateAndCurrency(startDate, endDate, "USD")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> bitcoinPriceService.getHistoricalPrices(request));
    }
}
