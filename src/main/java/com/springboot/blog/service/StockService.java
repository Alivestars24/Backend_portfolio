package com.springboot.blog.service;

import com.springboot.blog.payload.StockDto;
import com.springboot.blog.payload.StockUpdateDto;

import java.util.List;

public interface StockService {
    StockDto addStock(String token, StockDto stockDto);
    StockDto updateStock(String token, StockUpdateDto stockUpdateDto);

    void deleteStock(String token, String stockTicker);

    List<StockDto> getAllStocks(String token);
}
