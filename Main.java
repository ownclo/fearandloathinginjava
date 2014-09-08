public class Main {
    public static void main(String[] args) {
        System.out.println("Starting magic account service...");

        try {
            //AccountService as = new PersistentAccountService();
            //AccountService as = new CachingAccountService();
            AccountService as = new CachingPersistentAccountService();
            int nThreads = 10;
            int PORT = 8000;

            new HTTPServer(as, nThreads, PORT).start();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
