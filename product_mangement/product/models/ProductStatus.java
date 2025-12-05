package models;

/**
 * Model ProductStatus - Đại diện cho bảng product_status
 */
public class ProductStatus {
    private Long id;
    private String code;
    private String label;
    private String description;
    private Boolean isActive;
    private Integer displayOrder;
    

    public ProductStatus() {
    }
    
    // Constructor đầy đủ
    public ProductStatus(Long id, String code, String label, String description, 
                        Boolean isActive, Integer displayOrder) {
        this.id = id;
        this.code = code;
        this.label = label;
        this.description = description;
        this.isActive = isActive;
        this.displayOrder = displayOrder;
    }
    
    // Constructor không có id (để insert)
    public ProductStatus(String code, String label, String description, 
                        Boolean isActive, Integer displayOrder) {
        this.code = code;
        this.label = label;
        this.description = description;
        this.isActive = isActive;
        this.displayOrder = displayOrder;
    }
    
    // Getters và Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    @Override
    public String toString() {
        return label; // Hiển thị label trong ComboBox
    }
}
