package com.example.bitcoinpriceservice.controller;

import com.example.bitcoinpriceservice.model.HistoricalPriceRequest;
import com.example.bitcoinpriceservice.model.HistoricalPriceResponse;
import com.example.bitcoinpriceservice.service.BitcoinPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/bitcoin")
public class BitcoinPriceController {

    @Autowired
    private BitcoinPriceService bitcoinPriceService;

    @PostMapping("/historical-prices")
    public HistoricalPriceResponse getHistoricalPrices(@RequestBody HistoricalPriceRequest request) {
        try {
            return bitcoinPriceService.getHistoricalPrices(request);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching historical prices: " + e.getMessage());
        }
    }

    @PostMapping("/offline-mode")
    public ResponseEntity<String> setOfflineMode(@RequestParam boolean offlineMode) {
        bitcoinPriceService.setOfflineMode(offlineMode);
        return ResponseEntity.ok("Offline mode set to " + offlineMode);
    }

    @GetMapping("/offline-mode")
    public ResponseEntity<Boolean> getOfflineMode() {
        return ResponseEntity.ok(bitcoinPriceService.getOfflineMode());
    }
}

