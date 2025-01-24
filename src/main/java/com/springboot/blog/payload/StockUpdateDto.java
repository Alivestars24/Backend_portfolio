package com.springboot.blog.payload;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class StockUpdateDto {
    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public BigDecimal getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(BigDecimal buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    private String stockName; // Required for identifying the stock
    private String ticker;    // Optional for update
    private BigDecimal buyingPrice; // Optional for update
    private Integer quantity; // Optional for update
    private LocalDate purchaseDate; // Optional for update
}
