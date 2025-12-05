package models;

public class ProductAttribute {
    private Long id;
    private Long productId;
    private String attributeName;
    private String attributeValue;
    
    public ProductAttribute() {
    }
    
    public ProductAttribute(Long id, Long productId, String attributeName, String attributeValue) {
        this.id = id;
        this.productId = productId;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
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
    
    @Override
    public String toString() {
        return "ProductAttribute{" +
                "id=" + id +
                ", productId=" + productId +
                ", attributeName='" + attributeName + '\'' +
                ", attributeValue='" + attributeValue + '\'' +
                '}';
    }
}
