package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Brand;
import util.DBConnection;

public class BrandDAO {

    public List<Brand> findAll() {
        List<Brand> brands = new ArrayList<>();
        String sql = "SELECT * FROM brands ORDER BY name";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Brand brand = new Brand();
                brand.setId(rs.getLong("id"));
                brand.setName(rs.getString("name"));
                brand.setCreatedAt(rs.getTimestamp("created_at"));
                brands.add(brand);
            }

        } catch (SQLException e) {
            System.err.println("Loi khi lay danh sach thuong hieu: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return brands;
    }
}
