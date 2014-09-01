public class Main {
    public static void main(String[] args) {
        System.out.println("Starting AZAZAZ...\n");

        Integer id = 1488;
        AccountService as = new PersistentAccountService();
        Long amount = as.getAmount(id);
        System.out.println("amount = " + amount);
    }
}
