package com.example.bitcoinpriceservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class HistoricalPriceResponse {
    private List<PriceData> prices;
    private double minPrice;
    private double maxPrice;

    public HistoricalPriceResponse(List<PriceData> prices, double minPrice, double maxPrice) {
        this.prices = prices;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public List<PriceData> getPrices() {
        return prices;
    }

    public void setPrices(List<PriceData> prices) {
        this.prices = prices;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }
}

