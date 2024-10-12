package com.example.bitcoin_price_service.repository;

import com.example.bitcoin_price_service.model.BitcoinPriceCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BitcoinPriceCacheRepository extends JpaRepository<BitcoinPriceCache, Long> {

    Optional<BitcoinPriceCache> findByStartDateAndEndDateAndCurrency(LocalDate startDate, LocalDate endDate, String currency);
}

