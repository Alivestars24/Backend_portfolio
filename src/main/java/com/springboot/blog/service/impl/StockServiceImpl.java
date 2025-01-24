package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Stock;
import com.springboot.blog.entity.User;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.StockDto;
import com.springboot.blog.payload.StockUpdateDto;
import com.springboot.blog.repository.StockRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.security.JwtTokenProvider;
import com.springboot.blog.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public StockDto addStock(String token, StockDto stockDto) {
        // Extract username from the token
        String usernameOrEmail = jwtTokenProvider.getUsername(token);

        // Fetch the user from the database using username or email
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username or email", usernameOrEmail));

        // Map StockDto to Stock entity
        Stock stock = new Stock();
        stock.setStockName(stockDto.getStockName());
        stock.setTicker(stockDto.getTicker());
        stock.setBuyingPrice(stockDto.getBuyingPrice());
        stock.setQuantity(stockDto.getQuantity());
        stock.setPurchaseDate(stockDto.getPurchaseDate());
        stock.setUser(user);

        // Save stock to the database
        Stock savedStock = stockRepository.save(stock);

        // Map the saved Stock entity back to StockDto
        StockDto response = new StockDto();
        response.setStockName(savedStock.getStockName());
        response.setTicker(savedStock.getTicker());
        response.setBuyingPrice(savedStock.getBuyingPrice());
        response.setQuantity(savedStock.getQuantity());
        response.setPurchaseDate(savedStock.getPurchaseDate());

        return response;
    }

    @Override
    public StockDto updateStock(String token, StockUpdateDto stockUpdateDto) {
        // Extract username from token
        String username = jwtTokenProvider.getUsername(token);

        // Fetch the user
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username or email", username));

        // Fetch the stock
        Stock stock = stockRepository.findByStockNameAndUser(stockUpdateDto.getStockName(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Stock", "stockName", stockUpdateDto.getStockName()));

        // Update fields only if provided
        if (stockUpdateDto.getTicker() != null) {
            stock.setTicker(stockUpdateDto.getTicker());
        }
        if (stockUpdateDto.getBuyingPrice() != null) {
            stock.setBuyingPrice(stockUpdateDto.getBuyingPrice().doubleValue());
        }
        if (stockUpdateDto.getQuantity() != null) {
            stock.setQuantity(stockUpdateDto.getQuantity());
        }
        if (stockUpdateDto.getPurchaseDate() != null) {
            stock.setPurchaseDate(stockUpdateDto.getPurchaseDate());
        }

        // Save updated stock
        Stock updatedStock = stockRepository.save(stock);

        // Convert to DTO and return
        return mapToDto(updatedStock);
    }

    private StockDto mapToDto(Stock stock) {
        StockDto stockDto = new StockDto();
        stockDto.setStockName(stock.getStockName());
        stockDto.setTicker(stock.getTicker());
        stockDto.setBuyingPrice(stock.getBuyingPrice());
        stockDto.setQuantity(stock.getQuantity());
        stockDto.setPurchaseDate(stock.getPurchaseDate());
        return stockDto;
    }

    @Override
    public void deleteStock(String token, String stockTicker) {
        // Extract username from the token
        String username = jwtTokenProvider.getUsername(token);

        // Fetch the user from the database
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username/email", username));

        // Find the stock by its ticker and ensure it belongs to the authenticated user
        Stock stock = stockRepository.findByTickerAndUser(stockTicker, user)
                .orElseThrow(() -> new ResourceNotFoundException("Stock", "ticker", stockTicker));

        // Delete the stock
        stockRepository.delete(stock);
    }

    @Override
    public List<StockDto> getAllStocks(String token) {
        // Extract username from the token
        String username = jwtTokenProvider.getUsername(token);

        // Fetch the user from the database
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username/email", username));

        // Fetch all stocks for the user
        List<Stock> stocks = stockRepository.findAllByUser(user);

        // Map stocks to StockDto
        return stocks.stream()
                .map(stock -> {
                    StockDto stockDto = new StockDto();
                    stockDto.setStockName(stock.getStockName());
                    stockDto.setTicker(stock.getTicker());
                    stockDto.setBuyingPrice(stock.getBuyingPrice());
                    stockDto.setQuantity(stock.getQuantity());
                    stockDto.setPurchaseDate(stock.getPurchaseDate());
                    return stockDto;
                })
                .collect(Collectors.toList());
    }


}
