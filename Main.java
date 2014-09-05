public class Main {
    public static void main(String[] args) {
        System.out.println("Starting magic account service...");

        try {
            //AccountService as = new PersistentAccountService();
            //AccountService as = new CachingAccountService();
            AccountService as = new CachingPersistentAccountService();
            HTTPServer.start(as);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
