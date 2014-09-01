import java.sql.*;

public class PersistentAccountService implements AccountService {
    Connection connection = null;

    public PersistentAccountService() throws Exception {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/kokocashable","kokouser", "qwerty");
    }

    public void close() throws SQLException {
        connection.close();
    }

    @Override
    public Long getAmount(Integer id) throws SQLException {
        Statement stmt = null;
        String query = "SELECT amount FROM kokoaccounts WHERE id = " + id;

        Long amount = 0L; 
        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            amount = Long.valueOf(rs.getInt("amount"));
        }

        if (stmt != null) stmt.close();

        return amount;
    }

    @Override
    public void addAmount(Integer id, Long value) throws SQLException {
        if (!idExists(id))
            createAccount(id);
        addAmountForExistingId(id, value);
    }

    boolean idExists(Integer id) throws SQLException {
        Statement stmt = null;
        String query = "SELECT amount FROM kokoaccounts WHERE id = " + id;

        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        boolean hasNext = rs.next();
        if (stmt != null) stmt.close();
        return hasNext;
    }

    void createAccount(Integer id) throws SQLException {
        Statement stmt = null;
        String query = "INSERT INTO kokoaccounts (id, amount)"
                     + "VALUES (" + id + ", 0)";

        stmt = connection.createStatement();
        stmt.execute(query);
        if (stmt != null) stmt.close();
    }

    void addAmountForExistingId(Integer id, Long value) throws SQLException {
        Statement stmt = null;
        String query = "UPDATE kokoaccounts SET amount = amount + " + value
                    + " WHERE id = " + id;

        Long amount = 0L; 
        stmt = connection.createStatement();
        stmt.executeUpdate(query);

        if (stmt != null) stmt.close();
    }
}
