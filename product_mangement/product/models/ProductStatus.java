package models;

public class ProductStatus {
    private Long id;
    private String code;
    private String label;
    private String description;
    
    public ProductStatus() {
    }
    
    public ProductStatus(Long id, String code, String label, String description
                       ) {
        this.id = id;
        this.code = code;
        this.label = label;
        this.description = description;

    }
    
    public ProductStatus(String code, String label, String description) {
        this.code = code;
        this.label = label;
        this.description = description;
    }
    
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
    
    
    @Override
    public String toString() {
        return label; 
    }
}
