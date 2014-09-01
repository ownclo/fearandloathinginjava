import java.sql.*;

public class PersistentAccountService implements AccountService {
    Connection connection = null;

    public PersistentAccountService() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/kokocashable","kokouser", "qwerty");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            connection.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Long getAmount(Integer id) {
        Statement stmt = null;
        String query = "SELECT amount FROM kokoaccounts WHERE id = " + id;

        Long amount = 0L; 
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                amount = Long.valueOf(rs.getInt("amount"));
            }

            if (stmt != null) stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amount;
    }

    @Override
    public void addAmount(Integer id, Long value) {
        if (!idExists(id))
            createAccount(id);
        addAmountForExistingId(id, value);
    }

    boolean idExists(Integer id) {
        Statement stmt = null;
        String query = "SELECT amount FROM kokoaccounts WHERE id = " + id;

        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            boolean hasNext = rs.next();
            if (stmt != null) stmt.close();
            return hasNext;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    void createAccount(Integer id) {
        Statement stmt = null;
        String query = "INSERT INTO kokoaccounts (id, amount)"
                     + "VALUES (" + id + ", 0)";

        try {
            stmt = connection.createStatement();
            stmt.execute(query);
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void addAmountForExistingId(Integer id, Long value) {
        Statement stmt = null;
        String query = "UPDATE kokoaccounts SET amount = amount + " + value
                    + " WHERE id = " + id;

        Long amount = 0L; 
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(query);
            
            if (stmt != null) stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
