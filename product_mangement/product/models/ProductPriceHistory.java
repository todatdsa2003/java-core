package models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ProductPriceHistory {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private Timestamp changedAt;
    
    public ProductPriceHistory() {
    }
    
    public ProductPriceHistory(Long id, Long productId, BigDecimal oldPrice, 
                              BigDecimal newPrice, Timestamp changedAt) {
        this.id = id;
        this.productId = productId;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.changedAt = changedAt;
    }
    
    public ProductPriceHistory(Long productId, BigDecimal oldPrice, BigDecimal newPrice) {
        this.productId = productId;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public BigDecimal getOldPrice() {
        return oldPrice;
    }
    
    public void setOldPrice(BigDecimal oldPrice) {
        this.oldPrice = oldPrice;
    }
    
    public BigDecimal getNewPrice() {
        return newPrice;
    }
    
    public void setNewPrice(BigDecimal newPrice) {
        this.newPrice = newPrice;
    }
    
    public Timestamp getChangedAt() {
        return changedAt;
    }
    
    public void setChangedAt(Timestamp changedAt) {
        this.changedAt = changedAt;
    }
    
    @Override
    public String toString() {
        return "Gia cu: " + oldPrice + " -> Gia moi: " + newPrice;
    }
}
