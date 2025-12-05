package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.TimeZone;

public class DBConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/product_service_db";

    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "";

    private static final String DRIVER = "org.postgresql.Driver";

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        System.out.println("JVM TimeZone = " + TimeZone.getDefault().getID());
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Ket noi db thanh cong");

        } catch (ClassNotFoundException e) {
            System.err.println("Loi cai thu vien driver");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Loi ket noi database");
            System.err.println("URL: " + URL);
            System.err.println("Username: " + USERNAME);
            System.err.println("Chi tiet loi: " + e.getMessage());
            e.printStackTrace();
        }

        return conn;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Da dong ket noi database!");
            } catch (SQLException e) {
                System.err.println("Loi khi dong ket noi!");
                e.printStackTrace();
            }
        }
    }
}
