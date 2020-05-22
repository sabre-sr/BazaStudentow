import java.sql.*;

public class BazaDanych {
    public static void connect() {
        Connection conn = null;
        try {
            String path = "jdbc:sqlite:baza.db";
            conn = DriverManager.getConnection(path);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        connect();
    }
}
