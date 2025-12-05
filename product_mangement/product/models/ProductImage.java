package models;

import java.sql.Timestamp;

public class ProductImage {
    private Long id;
    private Long productId;
    private String imageUrl;
    private Boolean isThumbnail;
    private Timestamp createdAt;
    
    public ProductImage() {
    }
    
    public ProductImage(Long id, Long productId, String imageUrl, 
                       Boolean isThumbnail, Timestamp createdAt) {
        this.id = id;
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.isThumbnail = isThumbnail;
        this.createdAt = createdAt;
    }
    
    public ProductImage(Long productId, String imageUrl, Boolean isThumbnail) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.isThumbnail = isThumbnail;
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
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public Boolean getIsThumbnail() {
        return isThumbnail;
    }
    
    public void setIsThumbnail(Boolean isThumbnail) {
        this.isThumbnail = isThumbnail;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "ProductImage{" +
                "id=" + id +
                ", productId=" + productId +
                ", imageUrl='" + imageUrl + '\'' +
                ", isThumbnail=" + isThumbnail +
                '}';
    }
}
