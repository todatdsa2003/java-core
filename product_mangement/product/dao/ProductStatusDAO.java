package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.ProductStatus;
import util.DBConnection;

public class ProductStatusDAO {
    
    public List<ProductStatus> findAll() {
        List<ProductStatus> statusList = new ArrayList<>();
        String sql = "SELECT * FROM product_status";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ProductStatus status = new ProductStatus();
                status.setId(rs.getLong("id"));
                status.setCode(rs.getString("code"));
                status.setLabel(rs.getString("label"));
                status.setDescription(rs.getString("description"));
                statusList.add(status);
            }
            
        } catch (SQLException e) {
            System.err.println("Lay danh sach trang thai san pham khong thanh cong");
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return statusList;
    }
    
    private void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
