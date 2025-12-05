package dao;

import util.DBConnection;
import models.ProductPriceHistory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductPriceHistoryDAO {
    
    public List<ProductPriceHistory> findAll() {
        List<ProductPriceHistory> histories = new ArrayList<>();
        String sql = "SELECT pph.*, p.name AS product_name " +
                     "FROM product_price_history pph " +
                     "LEFT JOIN products p ON pph.product_id = p.id " +
                     "ORDER BY pph.changed_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                ProductPriceHistory history = extractFromResultSet(rs);
                histories.add(history);
            }
            
        } catch (SQLException e) {
            System.err.println("Loi khi lay lich su gia: " + e.getMessage());
        }
        
        return histories;
    }
    
    public List<ProductPriceHistory> findByProductId(long productId) {
        List<ProductPriceHistory> histories = new ArrayList<>();
        String sql = "SELECT pph.*, p.name AS product_name " +
                     "FROM product_price_history pph " +
                     "LEFT JOIN products p ON pph.product_id = p.id " +
                     "WHERE pph.product_id = ? " +
                     "ORDER BY pph.changed_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, productId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ProductPriceHistory history = extractFromResultSet(rs);
                histories.add(history);
            }
            
        } catch (SQLException e) {
            System.err.println("Loi khi tim lich su gia theo product: " + e.getMessage());
        }
        
        return histories;
    }
    
    public boolean insert(ProductPriceHistory history) {
        String sql = "INSERT INTO product_price_history (product_id, old_price, new_price) " +
                     "VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, history.getProductId());
            pstmt.setBigDecimal(2, history.getOldPrice());
            pstmt.setBigDecimal(3, history.getNewPrice());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Loi khi them lich su gia: " + e.getMessage());
        }
        
        return false;
    }
    
    private ProductPriceHistory extractFromResultSet(ResultSet rs) throws SQLException {
        ProductPriceHistory history = new ProductPriceHistory();
        history.setId(rs.getLong("id"));
        history.setProductId(rs.getLong("product_id"));
        history.setOldPrice(rs.getBigDecimal("old_price"));
        history.setNewPrice(rs.getBigDecimal("new_price"));
        history.setChangedAt(rs.getTimestamp("changed_at"));
        
        try {
            history.setProductName(rs.getString("product_name"));
        } catch (SQLException e) {
        }
        
        return history;
    }
}
