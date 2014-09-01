public class Main {
    public static void main(String[] args) {
        System.out.println("Starting magic account service...\n");

        Integer id = 1488;
        PersistentAccountService as = new PersistentAccountService();
        Long amount = as.getAmount(id);
        System.out.println("amount = " + amount);

        as.addAmount(id, 200L);
        Long newAmount = as.getAmount(id);
        System.out.println("amount = " + newAmount);

        as.close();
    }
}
