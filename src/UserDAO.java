import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserDAO implements AutoCloseable {

    private final HikariConnectionPool pool;

    public UserDAO() {
        pool = new HikariConnectionPool();
    }

    Connection conn;

    {
        try {
            conn = HikariConnectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private UserPOJO userFromResultSet(ResultSet rs) throws SQLException {
        return new UserPOJO(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4));
    }

    public void addUser(UserPOJO user) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO liza.user (username, email, password) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.executeUpdate();
            if (user.getId() == null) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    rs.next();
                    user.setId(rs.getInt(1));
                }
            }

        } finally {
            conn.close();
        }
    }


    public UserPOJO getUserByName(String username) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM liza.user WHERE username=?")) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return new UserPOJO(userFromResultSet(rs).getId(), userFromResultSet(rs).getUsername(), userFromResultSet(rs).getEmail(), userFromResultSet(rs).getPassword());
            }
        } finally {
            conn.close();
        }
    }


    public List<String> getAllUserNames() throws SQLException {

        try (PreparedStatement stmt = conn.prepareStatement("SELECT username FROM liza.user")) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<String> userNames = new ArrayList<>();
                while (rs.next()) {
                    userNames.add(rs.getString("username"));
                    System.out.println(rs.getString("username"));
                }
                return userNames;
            }
        } finally {
            conn.close();
        }

    }

    public List<UserPOJO> getAllUsers() throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("select * from liza.listcollection")) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<UserPOJO> collectionList = new ArrayList<>();
                while (rs.next()) {
                    collectionList.add(userFromResultSet(rs));
                }
                return collectionList;
            }
        } finally {
            conn.close();
        }
    }



            @Override
    public void close() throws SQLException {
        this.conn.close();
    }

}
