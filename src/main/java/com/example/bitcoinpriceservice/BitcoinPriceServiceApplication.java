package com.example.bitcoinpriceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class BitcoinPriceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BitcoinPriceServiceApplication.class, args);
	}

}
