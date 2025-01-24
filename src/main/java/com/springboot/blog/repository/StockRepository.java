package com.springboot.blog.repository;

import com.springboot.blog.entity.Stock;
import com.springboot.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByStockNameAndUser(String stockName, User user);

    Optional<Stock> findByTickerAndUser(String ticker, User user);

    List<Stock> findAllByUser(User user);

}
