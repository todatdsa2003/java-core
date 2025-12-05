package models;

import java.sql.Timestamp;
public class Brand {
    private Long id;
    private String name;
    private Timestamp createdAt;
    
    public Brand() {
    }
    
    public Brand(Long id, String name, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }
    
    public Brand(String name) {
        this.name = name;
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
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
