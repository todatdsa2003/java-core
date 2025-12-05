package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.TimeZone;

public class DBConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/product_service_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "datdev";
    private static final String DRIVER = "org.postgresql.Driver";
    
    private static Connection connection = null;

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        System.out.println("JVM TimeZone = " + TimeZone.getDefault().getID());
        
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Ket noi db thanh cong (Singleton)");
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
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Loi khi kiem tra/tao lai ket noi: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Loi khi dong ket noi!");
                e.printStackTrace();
            }
        }
    }
}
