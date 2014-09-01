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
        String query = "select amount from kokoaccounts where id = " + id;

        Long amount = 0L; 
        //"select COF_NAME, SUP_ID, PRICE, " + "SALES, TOTAL " + "from " + dbName + ".COFFEES";
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                amount = Long.valueOf(rs.getInt("amount"));

                //String coffeeName = rs.getString("COF_NAME");
                //int supplierID = rs.getInt("SUP_ID");
            }
            
            if (stmt != null) { stmt.close(); }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        }
        return amount;

    }

    @Override
    public void addAmount(Integer id, Long value) {
    }
}
