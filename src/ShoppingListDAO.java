import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ShoppingListDAO implements AutoCloseable {

    private final HikariConnectionPool pool;

    public ShoppingListDAO() {
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


    private ShoppingListPOJO shoppingListFromResultSet(ResultSet rs) throws SQLException {
        return new ShoppingListPOJO(
                rs.getInt(1),
                rs.getString(2),
                rs.getInt(3),
                rs.getString(4),
                rs.getInt(5));
    }



    public List<ShoppingListPOJO> getAllItemsByParentListId(int parentListId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM liza.shoppingList WHERE parentListId=? order by id desc")) {
            stmt.setInt(1, parentListId);
            try (ResultSet rs = stmt.executeQuery()) {
                List<ShoppingListPOJO> shoppingList = new ArrayList<>();
                while (rs.next()) {
                    shoppingList.add(shoppingListFromResultSet(rs));
                }
                return shoppingList;
            }
        } finally {
            conn.close();
        }
    }




    public void addItem(ShoppingListPOJO shoppingList) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO liza.shoppingList (product, amount, status, parentListId) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, shoppingList.getProduct());
            stmt.setInt(2, shoppingList.getAmount());
            stmt.setString(3, shoppingList.getStatus());
            stmt.setInt(4,shoppingList.getParentListId());
            stmt.executeUpdate();
            if (shoppingList.getId() == null) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    rs.next();
                    shoppingList.setId(rs.getInt(1));
                }
            }

        } finally {
            conn.close();
        }
    }


    public void updateItem(ShoppingListPOJO shoppingList) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE liza.shoppingList  SET product = ?, amount = ?, status=?, parentListId=? WHERE id = ?")) {
            stmt.setString(1, shoppingList.getProduct());
            stmt.setInt(2, shoppingList.getAmount());
            stmt.setString(3, shoppingList.getStatus());
            stmt.setInt(4, shoppingList.getParentListId());
            stmt.setInt(5, shoppingList.getId());
            stmt.executeUpdate();
        } finally {
            conn.close();
        }
    }

    public void deleteItemById(int id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM liza.shoppingList  WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } finally {
            conn.close();
        }
    }

    //revise this method if we need it
    public List<ShoppingListPOJO> deleteAllItems() throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM liza.shoppingList ")) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<ShoppingListPOJO> shoppingList = new ArrayList<>();
                while (rs.next()) {
                    shoppingList.remove(shoppingListFromResultSet(rs));
                }
                return shoppingList;
            }
        } finally {
            conn.close();
        }
    }

    public void deleteAllItemsByParentListId(int parentListId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM liza.shoppingList where parentListId=?")) {
          stmt.setInt(1, parentListId);
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