package models;

import java.sql.Timestamp;

public class ProductAttribute {
    private Long id;
    private Long productId;
    private String attributeName;
    private String attributeValue;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String productName;
    
    public ProductAttribute() {
    }
    
    public ProductAttribute(Long id, Long productId, String attributeName, String attributeValue, 
                           Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.productId = productId;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public ProductAttribute(Long productId, String attributeName, String attributeValue) {
        this.productId = productId;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
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
    
    public String getAttributeName() {
        return attributeName;
    }
    
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
    
    public String getAttributeValue() {
        return attributeValue;
    }
    
    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    @Override
    public String toString() {
        return attributeName + ": " + attributeValue;
    }
}
