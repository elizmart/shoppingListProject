import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ListCollectionDAO implements AutoCloseable {

    private final HikariConnectionPool pool;

    public ListCollectionDAO() {
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


    private ListCollectionPOJO listCollectionFromResultSet(ResultSet rs) throws SQLException {
        return new ListCollectionPOJO(
                rs.getInt(1),
                rs.getString(2));
    }


    public List<ListCollectionPOJO> getAllLists() throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("select * from liza.listcollection order by id desc")) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<ListCollectionPOJO> collectionList = new ArrayList<>();
                while (rs.next()) {
                    collectionList.add(listCollectionFromResultSet(rs));
                }
                return collectionList;
            }
        } finally {
            conn.close();
        }
    }


    public void addList(ListCollectionPOJO collectionList) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO liza.listcollection (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, collectionList.getName());
            stmt.executeUpdate();
            if (collectionList.getId() == null) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    rs.next();
                    collectionList.setId(rs.getInt(1));
                }
            }
        } finally {
            conn.close();
        }
    }


    public void updateListName(ListCollectionPOJO collectionList) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE liza.listcollection  SET name = ?  WHERE id = ?")) {
            stmt.setString(1, collectionList.getName());
            stmt.setInt(2, collectionList.getId());
            stmt.executeUpdate();
        } finally {
            conn.close();
        }
    }

    public void deleteListById(int id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM liza.listcollection  WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } finally {
            conn.close();
        }
    }


    @Override
    public void close() throws SQLException {
        this.conn.close();
    }

}
