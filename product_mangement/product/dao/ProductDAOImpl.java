package dao;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Product;
import models.ProductPriceHistory;
import util.DBConnection;

public class ProductDAOImpl implements ProductDAO {
    
    private ProductPriceHistoryDAO priceHistoryDAO = new ProductPriceHistoryDAO();
    
    @Override
    public List<Product> findAll() {
        return findWithPagination(0, Integer.MAX_VALUE, null, null);
    }
    
    public List<Product> findWithPagination(int page, int pageSize, BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.id, p.name, p.slug, p.description, p.price, p.availability, ");
        sql.append("p.status_id, p.category_id, p.brand_id, p.created_at, p.updated_at, ");
        sql.append("c.name AS category_name, b.name AS brand_name, s.label AS status_label ");
        sql.append("FROM products p ");
        sql.append("LEFT JOIN categories c ON p.category_id = c.id ");
        sql.append("LEFT JOIN brands b ON p.brand_id = b.id ");
        sql.append("LEFT JOIN product_status s ON p.status_id = s.id ");
        sql.append("WHERE 1=1 ");
        
        if (minPrice != null) {
            sql.append("AND p.price >= ? ");
        }
        if (maxPrice != null) {
            sql.append("AND p.price <= ? ");
        }
        
        sql.append("ORDER BY p.id DESC ");
        sql.append("LIMIT ? OFFSET ?");
        
        Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = conn.prepareStatement(sql.toString());
            
            int paramIndex = 1;
            if (minPrice != null) {
                pstmt.setBigDecimal(paramIndex++, minPrice);
            }
            if (maxPrice != null) {
                pstmt.setBigDecimal(paramIndex++, maxPrice);
            }
            pstmt.setInt(paramIndex++, pageSize);
            pstmt.setInt(paramIndex, page * pageSize);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Product product = extractProductFromResultSet(rs);
                products.add(product);
            }
            
        } catch (SQLException e) {
            System.err.println("Loi khi lay danh sach san pham: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return products;
    }
    
    public int countProducts(BigDecimal minPrice, BigDecimal maxPrice) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM products p WHERE 1=1 ");
        
        if (minPrice != null) {
            sql.append("AND p.price >= ? ");
        }
        if (maxPrice != null) {
            sql.append("AND p.price <= ? ");
        }
        
        Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = conn.prepareStatement(sql.toString());
            
            int paramIndex = 1;
            if (minPrice != null) {
                pstmt.setBigDecimal(paramIndex++, minPrice);
            }
            if (maxPrice != null) {
                pstmt.setBigDecimal(paramIndex, maxPrice);
            }
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Loi khi dem san pham: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return 0;
    }
    
    @Override
    public Product findById(long id) {
        String sql = "SELECT p.id, p.name, p.slug, p.description, p.price, p.availability, " +
                     "p.status_id, p.category_id, p.brand_id, p.created_at, p.updated_at, " +
                     "c.name AS category_name, b.name AS brand_name, s.label AS status_label " +
                     "FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "LEFT JOIN brands b ON p.brand_id = b.id " +
                     "LEFT JOIN product_status s ON p.status_id = s.id " +
                     "WHERE p.id = ?";
        
        Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractProductFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Loi khi tim san pham theo ID: " + e.getMessage());
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
        
        Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + keyword + "%");
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Product product = extractProductFromResultSet(rs);
                products.add(product);
            }
            
        } catch (SQLException e) {
            System.err.println("Loi khi tim kiem san pham: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return products;
    }
    
    @Override
    public boolean insert(Product product) {
        String sql = "INSERT INTO products (name, slug, description, price, availability, " +
                     "status_id, category_id, brand_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = null;
        
        try {
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
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                System.out.println("Them san pham thanh cong!");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Loi khi them san pham: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return false;
    }
    
    @Override
    public boolean update(Product product) {
        Product oldProduct = findById(product.getId());
        
        if (oldProduct != null && !oldProduct.getPrice().equals(product.getPrice())) {
            ProductPriceHistory history = new ProductPriceHistory();
            history.setProductId(product.getId());
            history.setOldPrice(oldProduct.getPrice());
            history.setNewPrice(product.getPrice());
            priceHistoryDAO.insert(history);
            System.out.println("Da luu lich su thay doi gia");
        }
        
        String sql = "UPDATE products SET name = ?, slug = ?, description = ?, price = ?, " +
                     "availability = ?, status_id = ?, category_id = ?, brand_id = ?, " +
                     "updated_at = NOW() " +
                     "WHERE id = ?";
        
        Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = null;
        
        try {
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
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                System.out.println("Cap nhat san pham thanh cong!");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Loi khi cap nhat san pham: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return false;
    }
    
    @Override
    public boolean delete(long id) {
        String sql = "DELETE FROM products WHERE id = ?";
        
        Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = null;
        
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                System.out.println("Xoa san pham thanh cong!");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Loi khi xoa san pham: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        
        long categoryId = rs.getLong("category_id");
        if (!rs.wasNull()) {
            product.setCategoryId(categoryId);
        }
        
        long brandId = rs.getLong("brand_id");
        if (!rs.wasNull()) {
            product.setBrandId(brandId);
        }
        
        product.setCreatedAt(rs.getTimestamp("created_at"));
        product.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        product.setCategoryName(rs.getString("category_name"));
        product.setBrandName(rs.getString("brand_name"));
        product.setStatusLabel(rs.getString("status_label"));
        
        return product;
    }
}
