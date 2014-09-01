public class Main {
    public static void main(String[] args) {
        System.out.println("Starting magic account service...\n");

        try {
            PersistentAccountService as = new PersistentAccountService();
            HTTPServer.start(as);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
