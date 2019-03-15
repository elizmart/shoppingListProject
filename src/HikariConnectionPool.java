import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class HikariConnectionPool {
    private static HikariDataSource hds;

    static {

        hds = new HikariDataSource();
        hds.setJdbcUrl("jdbc:mysql://localhost:3306/liza");
        hds.setDriverClassName("com.mysql.jdbc.Driver");
        hds.setUsername("root");
        hds.setPassword("dreamfaller28");
        hds.setMaximumPoolSize(10);
    }

    public static Connection getConnection() throws SQLException {
        return hds.getConnection();
    }

    public static void close(Connection con) {
        try {
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(java.sql.PreparedStatement stmt) {
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

