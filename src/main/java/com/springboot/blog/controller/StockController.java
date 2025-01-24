package com.springboot.blog.controller;

import com.springboot.blog.payload.StockDto;
import com.springboot.blog.payload.StockUpdateDto;
import com.springboot.blog.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping
    public ResponseEntity<StockDto> addStock(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                             @RequestBody StockDto stockDto) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        StockDto savedStock = stockService.addStock(jwtToken, stockDto);
        return new ResponseEntity<>(savedStock, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<StockDto> updateStock(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                @RequestBody StockUpdateDto stockUpdateDto) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        StockDto updatedStock = stockService.updateStock(jwtToken, stockUpdateDto);
        return new ResponseEntity<>(updatedStock, HttpStatus.OK);
    }

    @DeleteMapping("/{stockTicker}")
    public ResponseEntity<String> deleteStock(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                              @PathVariable String stockTicker) {
        // Remove "Bearer " prefix from the token if present
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        stockService.deleteStock(jwtToken, stockTicker);
        return new ResponseEntity<>("Stock deleted successfully", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<StockDto>> getAllStocks(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        // Remove "Bearer " prefix from the token if present
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        List<StockDto> stocks = stockService.getAllStocks(jwtToken);
        return ResponseEntity.ok(stocks);
    }

}
