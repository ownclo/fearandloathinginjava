public class TestClient {
    public static void main(String[] args) {
        System.out.println("WOW! MANY TEST CLIENT! SUCH HIGHLOAD!");
        String basename = "http://localhost:8000/accounts/";

        final HttpClientAccountService as = new HttpClientAccountService(basename);
        final Integer id = 1488;

        Thread adds = new Thread(new Runnable() {
            public void run() {
                try {
                    testAddAmount(as, id, 10L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread gets = new Thread(new Runnable() {
            public void run() {
                testGetAmount(as, id);
            }
        });

        adds.start();
        gets.start();

        try {
            adds.join();
            gets.join();
        } catch (InterruptedException e) {}
    }

    public static void testGetAmount(HttpClientAccountService as, Integer id) {
        while (true) {
            Long amount = as.getAmount(id);
            System.out.println("Amount for Id #" + id + " is " + amount);
        }
    }

    public static void testAddAmount(HttpClientAccountService as, Integer id, Long value)
        throws Exception {

        while (true) {
            as.addAmount(id, value);
            System.out.println("Added " + value + " ubercoins for account #" + id);
        }
    }
}
