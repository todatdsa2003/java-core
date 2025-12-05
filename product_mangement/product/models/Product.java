package models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Product {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private BigDecimal price;
    private Integer availability;
    private Long statusId;
    private Long categoryId;
    private Long brandId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    private String categoryName;
    private String brandName;
    private String statusLabel;
    
    public Product() {
    }
    
    public Product(Long id, String name, String slug, String description, 
                  BigDecimal price, Integer availability, Long statusId, 
                  Long categoryId, Long brandId, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.price = price;
        this.availability = availability;
        this.statusId = statusId;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public Product(String name, String slug, String description, 
                  BigDecimal price, Integer availability, Long statusId, 
                  Long categoryId, Long brandId) {
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.price = price;
        this.availability = availability;
        this.statusId = statusId;
        this.categoryId = categoryId;
        this.brandId = brandId;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSlug() {
        return slug;
    }
    
    public void setSlug(String slug) {
        this.slug = slug;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public Integer getAvailability() {
        return availability;
    }
    
    public void setAvailability(Integer availability) {
        this.availability = availability;
    }
    
    public Long getStatusId() {
        return statusId;
    }
    
    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }
    
    public Long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    public Long getBrandId() {
        return brandId;
    }
    
    public void setBrandId(Long brandId) {
        this.brandId = brandId;
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
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getBrandName() {
        return brandName;
    }
    
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
    
    public String getStatusLabel() {
        return statusLabel;
    }
    
    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", availability=" + availability +
                ", categoryName='" + categoryName + '\'' +
                ", brandName='" + brandName + '\'' +
                ", statusLabel='" + statusLabel + '\'' +
                '}';
    }
}
