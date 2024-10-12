package com.example.bitcoin_price_service.service;

import com.example.bitcoin_price_service.model.*;
import com.example.bitcoin_price_service.repository.BitcoinPriceCacheRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

@Service
public class BitcoinPriceService {

    @Autowired
    private BitcoinPriceCacheRepository cacheRepository;

    @Autowired
    private ExchangeRateService exchangeRateService;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ObjectMapper objectMapper;

    private boolean offlineMode = false; // Toggle for offline mode

    public HistoricalPriceResponse getHistoricalPrices(HistoricalPriceRequest request) throws JsonProcessingException {
        LocalDate startDate = LocalDate.parse(request.getStartDate());
        LocalDate endDate = LocalDate.parse(request.getEndDate());
        String currency = request.getCurrency().toUpperCase();

        // Check cache first
        Optional<BitcoinPriceCache> cachedData = cacheRepository.findByStartDateAndEndDateAndCurrency(startDate, endDate, currency);

        if (cachedData.isPresent()) {
            String jsonData = cachedData.get().getJsonData();
            return objectMapper.readValue(jsonData, HistoricalPriceResponse.class);
        }

        // If in offline mode and no cache, throw exception
        if (offlineMode) {
            throw new RuntimeException("Offline mode is enabled and no cached data is available.");
        }

        // Fetch historical Bitcoin prices from CoinDesk API
        List<PriceData> prices = fetchBitcoinPricesFromCoinDeskApi(startDate, endDate, currency);

        // Calculate min and max prices
        double minPrice = prices.stream().mapToDouble(PriceData::getPrice).min().orElse(0);
        double maxPrice = prices.stream().mapToDouble(PriceData::getPrice).max().orElse(0);

        HistoricalPriceResponse response = new HistoricalPriceResponse(prices, minPrice, maxPrice);

        // Cache the data
        BitcoinPriceCache cache = new BitcoinPriceCache();
        cache.setStartDate(startDate);
        cache.setEndDate(endDate);
        cache.setCurrency(currency);
        cache.setJsonData(objectMapper.writeValueAsString(response));
        cache.setCachedAt(LocalDate.now());

        cacheRepository.save(cache);

        return response;
    }

    // Fetch Bitcoin historical prices from CoinDesk API
    private List<PriceData> fetchBitcoinPricesFromCoinDeskApi(LocalDate startDate, LocalDate endDate, String currency) throws JsonProcessingException {
        String apiUrl = String.format(
                "https://api.coindesk.com/v1/bpi/historical/close.json?start=%s&end=%s&currency=%s",
                startDate.toString(),
                endDate.toString(),
                currency
        );

        String jsonResponse = restTemplate.getForObject(apiUrl, String.class);
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode bpiNode = rootNode.path("bpi");

        // Convert the response JSON into a list of PriceData objects
        List<PriceData> prices = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> fields = bpiNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            LocalDate date = LocalDate.parse(field.getKey());
            double price = field.getValue().asDouble();
            prices.add(new PriceData(date, price));
        }

        return prices;
    }

    public void setOfflineMode(boolean offlineMode) {
        this.offlineMode = offlineMode;
    }
}
