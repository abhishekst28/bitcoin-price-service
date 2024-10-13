package com.example.bitcoinpriceservice.controller;

import com.example.bitcoinpriceservice.model.HistoricalPriceRequest;
import com.example.bitcoinpriceservice.model.HistoricalPriceResponse;
import com.example.bitcoinpriceservice.service.BitcoinPriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class BitcoinPriceControllerTest {

    @InjectMocks
    private BitcoinPriceController bitcoinPriceController;

    @Mock
    private BitcoinPriceService bitcoinPriceService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bitcoinPriceController).build();
    }

    @Test
    public void testGetHistoricalPrices() throws Exception {
        // Arrange
        HistoricalPriceRequest request = new HistoricalPriceRequest();
        request.setStartDate("2023-01-01");
        request.setEndDate("2023-01-10");
        request.setCurrency("USD");

        HistoricalPriceResponse response = new HistoricalPriceResponse(null, 1.0,  20.0);
        response.setMinPrice(100.0);
        response.setMaxPrice(200.0);

        when(bitcoinPriceService.getHistoricalPrices(request)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/bitcoin/historical-prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"startDate\": \"2023-01-01\", \"endDate\": \"2023-01-10\", \"currency\": \"USD\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.minPrice").value(100.0))
                .andExpect(jsonPath("$.maxPrice").value(200.0));
    }
}
