package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.ProductAttribute;
import util.DBConnection;

public class ProductAttributeDAO {
    
    public List<ProductAttribute> findAll() {
        List<ProductAttribute> attributes = new ArrayList<>();
        String sql = "SELECT pa.*, p.name AS product_name " +
                     "FROM product_attributes pa " +
                     "LEFT JOIN products p ON pa.product_id = p.id " +
                     "ORDER BY p.name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                ProductAttribute attr = extractFromResultSet(rs);
                attributes.add(attr);
            }
            
        } catch (SQLException e) {
            System.err.println("Loi khi lay danh sach attribute: " + e.getMessage());
        }
        
        return attributes;
    }
    
    public List<ProductAttribute> findByProductId(long productId) {
        List<ProductAttribute> attributes = new ArrayList<>();
        String sql = " SELECT pa.*, p.name AS product_name " +
                     "FROM product_attributes pa " +
                     "LEFT JOIN products p ON pa.product_id = p.id " +
                     "WHERE product_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, productId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ProductAttribute attr = extractFromResultSet(rs);
                attributes.add(attr);
            }
            
        } catch (SQLException e) {
            System.err.println("Loi khi tim attribute theo product: " + e.getMessage());
        }
        
        return attributes;
    }
    
    public ProductAttribute findById(long id) {
        String sql = "SELECT pa.*, p.name AS product_name " +
                     "FROM product_attributes pa " +
                     "LEFT JOIN products p ON pa.product_id = p.id " +
                     "WHERE pa.id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Loi khi tim attribute theo ID: " + e.getMessage());
        }
        
        return null;
    }
    
    public boolean insert(ProductAttribute attribute) {
        String sql = "INSERT INTO product_attributes (product_id, attribute_name, attribute_value) " +
                     "VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, attribute.getProductId());
            pstmt.setString(2, attribute.getAttributeName());
            pstmt.setString(3, attribute.getAttributeValue());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Loi khi them attribute: " + e.getMessage());
        }
        
        return false;
    }
    
    public boolean update(ProductAttribute attribute) {
        String sql = "UPDATE product_attributes SET product_id = ?, attribute_name = ?, " +
                     "attribute_value = ?, updated_at = NOW() WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, attribute.getProductId());
            pstmt.setString(2, attribute.getAttributeName());
            pstmt.setString(3, attribute.getAttributeValue());
            pstmt.setLong(4, attribute.getId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Loi khi cap nhat attribute: " + e.getMessage());
        }
        
        return false;
    }
    
    public boolean delete(long id) {
        String sql = "DELETE FROM product_attributes WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Loi khi xoa attribute: " + e.getMessage());
        }
        
        return false;
    }
    
    private ProductAttribute extractFromResultSet(ResultSet rs) throws SQLException {
        ProductAttribute attr = new ProductAttribute();
        attr.setId(rs.getLong("id"));
        attr.setProductId(rs.getLong("product_id"));
        attr.setAttributeName(rs.getString("attribute_name"));
        attr.setAttributeValue(rs.getString("attribute_value"));
        // attr.setCreatedAt(rs.getTimestamp("created_at"));
        // attr.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        try {
            attr.setProductName(rs.getString("product_name"));
        } catch (SQLException e) {
        }
        
        return attr;
    }
}
