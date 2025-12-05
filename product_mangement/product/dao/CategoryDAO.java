package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Category;
import util.DBConnection;

public class CategoryDAO {
    
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY name";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getLong("id"));
                category.setName(rs.getString("name"));
                category.setSlug(rs.getString("slug"));
                
                long parentId = rs.getLong("parent_id");
                if (!rs.wasNull()) {
                    category.setParentId(parentId);
                }
                
                category.setCreatedAt(rs.getTimestamp("created_at"));
                categories.add(category);
            }
            
        } catch (SQLException e) {
            System.err.println("Loi khi lay danh sach danh muc");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return categories;
    }
    
    public Category findById(Long id) {
        if (id == null) return null;
        
        String sql = "SELECT * FROM categories WHERE id = ?";
        
        Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Category category = new Category();
                category.setId(rs.getLong("id"));
                category.setName(rs.getString("name"));
                category.setSlug(rs.getString("slug"));
                
                long parentId = rs.getLong("parent_id");
                if (!rs.wasNull()) {
                    category.setParentId(parentId);
                }
                
                category.setCreatedAt(rs.getTimestamp("created_at"));
                category.setUpdatedAt(rs.getTimestamp("updated_at"));
                
                return category;
            }
            
        } catch (SQLException e) {
            System.err.println("Loi khi tim danh muc theo ID");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return null;
    }
    
    public boolean insert(Category category) {
        String sql = "INSERT INTO categories (name, slug, description, parent_id) VALUES (?, ?, ?, ?)";
        
        Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = null;
        
        try {
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, category.getName());
            pstmt.setString(2, category.getSlug());
            
            if (category.getParentId() != null) {
                pstmt.setLong(3, category.getParentId());
            } else {
                pstmt.setNull(3, Types.BIGINT);
            }
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Loi khi them danh muc");
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean update(Category category) {
        String sql = "UPDATE categories SET name = ?, slug = ?, description = ?, parent_id = ? WHERE id = ?";
        
        Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = null;
        
        try {
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, category.getName());
            pstmt.setString(2, category.getSlug());
        
            if (category.getParentId() != null) {
                pstmt.setLong(3, category.getParentId());
            } else {
                pstmt.setNull(3, Types.BIGINT);
            }
            
            pstmt.setLong(4, category.getId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Loi khi cap nhat danh muc");
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean delete(Long id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        
        Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = null;
        
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Loi khi xoa danh muc");
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
