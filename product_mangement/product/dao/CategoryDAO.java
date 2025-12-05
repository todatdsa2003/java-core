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
            closeResources(conn, pstmt, rs);
        }
        
        return categories;
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
