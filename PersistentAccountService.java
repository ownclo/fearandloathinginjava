import java.sql.*;
import org.postgresql.ds.PGPoolingDataSource;

public class PersistentAccountService implements AccountService {
    PGPoolingDataSource source;

    public PersistentAccountService() throws Exception {
        source = new PGPoolingDataSource();
        source.setServerName("localhost");
        source.setDatabaseName("kokocashable");
        source.setUser("kokouser");
        source.setPassword("qwerty");
        source.setMaxConnections(10);
    }

    @Override
    public Long getAmount(Integer id) throws SQLException {
        Statement stmt = null;
        String query = "SELECT amount FROM kokoaccounts WHERE id = " + id;

        Connection connection = null;
        try {
            connection = source.getConnection();
            Long amount = 0L; 
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                amount = Long.valueOf(rs.getInt("amount"));
            }

            if (stmt != null) stmt.close();

            return amount;
        } finally {
            if (connection != null) {
                try { connection.close(); } catch (SQLException e) {}
            }
        }
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

        Connection connection = null;
        try {
            connection = source.getConnection();
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            boolean hasNext = rs.next();
            if (stmt != null) stmt.close();
            return hasNext;

        } finally {
            if (connection != null) {
                try { connection.close(); } catch (SQLException e) {}
            }
        }
    }

    void createAccount(Integer id) throws SQLException {
        Statement stmt = null;
        String query = "INSERT INTO kokoaccounts (id, amount)"
                     + "VALUES (" + id + ", 0)";

        Connection connection = null;
        try {
            connection = source.getConnection();
            stmt = connection.createStatement();
            stmt.execute(query);
            if (stmt != null) stmt.close();

        } finally {
            if (connection != null) {
                try { connection.close(); } catch (SQLException e) {}
            }
        }
    }

    void addAmountForExistingId(Integer id, Long value) throws SQLException {
        Statement stmt = null;
        String query = "UPDATE kokoaccounts SET amount = amount + " + value
                    + " WHERE id = " + id;

        Connection connection = null;
        try {
            connection = source.getConnection();
            Long amount = 0L; 
            stmt = connection.createStatement();
            stmt.executeUpdate(query);

            if (stmt != null) stmt.close();

        } finally {
            if (connection != null) {
                try { connection.close(); } catch (SQLException e) {}
            }
        }
    }
}
