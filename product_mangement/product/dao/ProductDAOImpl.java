package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Product;
import util.DBConnection;

public class ProductDAOImpl implements ProductDAO {
    
    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.id, p.name, p.slug, p.description, p.price, p.availability, " +
                     "p.status_id, p.category_id, p.brand_id, p.created_at, p.updated_at, " +
                     "c.name AS category_name, b.name AS brand_name, s.label AS status_label " +
                     "FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "LEFT JOIN brands b ON p.brand_id = b.id " +
                     "LEFT JOIN product_status s ON p.status_id = s.id " +
                     "ORDER BY p.id DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Product product = extractProductFromResultSet(rs);
                products.add(product);
            }
            
        } catch (SQLException e) {
            System.err.println("Lay danh sach san pham khong thanh cong");
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return products;
    }
    
    @Override
    public Product findById(long id) {
        Product product = null;
        String sql = "SELECT p.id, p.name, p.slug, p.description, p.price, p.availability, " +
                     "p.status_id, p.category_id, p.brand_id, p.created_at, p.updated_at, " +
                     "c.name AS category_name, b.name AS brand_name, s.label AS status_label " +
                     "FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "LEFT JOIN brands b ON p.brand_id = b.id " +
                     "LEFT JOIN product_status s ON p.status_id = s.id " +
                     "WHERE p.id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                product = extractProductFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Tim san pham theo ID khong thanh cong");
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return product;
    }

    @Override
    public List<Product> searchByName(String keyword) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.id, p.name, p.slug, p.description, p.price, p.availability, " +
                     "p.status_id, p.category_id, p.brand_id, p.created_at, p.updated_at, " +
                     "c.name AS category_name, b.name AS brand_name, s.label AS status_label " +
                     "FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "LEFT JOIN brands b ON p.brand_id = b.id " +
                     "LEFT JOIN product_status s ON p.status_id = s.id " +
                     "WHERE LOWER(p.name) LIKE LOWER(?) " +
                     "ORDER BY p.id DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + keyword + "%");
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Product product = extractProductFromResultSet(rs);
                products.add(product);
            }
            
        } catch (SQLException e) {
            System.err.println("Tim kiem san pham khong thanh cong");
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return products;
    }
    
    @Override
    public boolean insert(Product product) {
        String sql = "INSERT INTO products (name, slug, description, price, availability, " +
                     "status_id, category_id, brand_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getSlug());
            pstmt.setString(3, product.getDescription());
            pstmt.setBigDecimal(4, product.getPrice());
            pstmt.setInt(5, product.getAvailability());
            pstmt.setLong(6, product.getStatusId());
            
            if (product.getCategoryId() != null) {
                pstmt.setLong(7, product.getCategoryId());
            } else {
                pstmt.setNull(7, Types.BIGINT);
            }
            
            if (product.getBrandId() != null) {
                pstmt.setLong(8, product.getBrandId());
            } else {
                pstmt.setNull(8, Types.BIGINT);
            }
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Them san pham thanh cong");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Loi khi them san pham");
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }
        
        return false;
    }
    
    @Override
    public boolean update(Product product) {
        String sql = "UPDATE products SET name = ?, slug = ?, description = ?, price = ?, " +
                     "availability = ?, status_id = ?, category_id = ?, brand_id = ?, " +
                     "updated_at = NOW() " +
                     "WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getSlug());
            pstmt.setString(3, product.getDescription());
            pstmt.setBigDecimal(4, product.getPrice());
            pstmt.setInt(5, product.getAvailability());
            pstmt.setLong(6, product.getStatusId());
            
            if (product.getCategoryId() != null) {
                pstmt.setLong(7, product.getCategoryId());
            } else {
                pstmt.setNull(7, Types.BIGINT);
            }
            
            if (product.getBrandId() != null) {
                pstmt.setLong(8, product.getBrandId());
            } else {
                pstmt.setNull(8, Types.BIGINT);
            }
            
            pstmt.setLong(9, product.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Cap nhat san pham thanh cong");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Loi khi cap nhat san pham");
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }
        
        return false;
    }
    

    @Override
    public boolean delete(long id) {
        String sql = "DELETE FROM products WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Xoa san pham thanh cong");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Loi khi xoa san pham");
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }
        
        return false;
    }
    

    private Product extractProductFromResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();
        
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setSlug(rs.getString("slug"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setAvailability(rs.getInt("availability"));
        product.setStatusId(rs.getLong("status_id"));
        
        // Xử lý category_id có thể null
        long categoryId = rs.getLong("category_id");
        if (!rs.wasNull()) {
            product.setCategoryId(categoryId);
        }
        
        // Xử lý brand_id có thể null
        long brandId = rs.getLong("brand_id");
        if (!rs.wasNull()) {
            product.setBrandId(brandId);
        }
        
        product.setCreatedAt(rs.getTimestamp("created_at"));
        product.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        // Thông tin bổ sung
        product.setCategoryName(rs.getString("category_name"));
        product.setBrandName(rs.getString("brand_name"));
        product.setStatusLabel(rs.getString("status_label"));
        
        return product;
    }
    

    private void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Loi khi đong ket noi");
            e.printStackTrace();
        }
    }
}
