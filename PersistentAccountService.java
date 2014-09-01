public class PersistentAccountService implements AccountService {
    @Override
    public Long getAmount(Integer id) {
        return Long.valueOf(42);
    }

    @Override
    public void addAmount(Integer id, Long value) {
    }
}
