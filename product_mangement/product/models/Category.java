package models;

import java.sql.Timestamp;

public class Category {
    private Long id;
    private String name;
    private String slug;
    private Long parentId;
    private Timestamp createdAt;
    
    public Category() {
    }
    
    public Category(Long id, String name, String slug, Long parentId, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.parentId = parentId;
        this.createdAt = createdAt;
    }
    
    public Category(String name, String slug, Long parentId) {
        this.name = name;
        this.slug = slug;
        this.parentId = parentId;
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
    
    public Long getParentId() {
        return parentId;
    }
    
    public void setParentId(Long parentId) {
        this.parentId = parentId;
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
