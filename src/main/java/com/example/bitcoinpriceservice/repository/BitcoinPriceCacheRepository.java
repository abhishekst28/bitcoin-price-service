package com.example.bitcoinpriceservice.repository;

import com.example.bitcoinpriceservice.model.BitcoinPriceCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BitcoinPriceCacheRepository extends JpaRepository<BitcoinPriceCache, Long> {

    Optional<BitcoinPriceCache> findByStartDateAndEndDateAndCurrency(LocalDate startDate, LocalDate endDate, String currency);
}

