import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class CachingAccountService implements AccountService {
    ConcurrentHashMap<Integer,Long> cache;

    public CachingAccountService() {
        cache = new ConcurrentHashMap<Integer,Long>();
    }

    public CachingAccountService(Map<Integer,Long> init) {
        cache = new ConcurrentHashMap<Integer, Long>(init);
    }

    @Override
    public Long getAmount(Integer id) {
        Long amount = cache.get(id);
        if (amount == null)
            return 0L;
        return amount;
    }

    @Override
    public synchronized void addAmount(Integer id, Long value) throws Exception {
        if (cache.containsKey(id)) {
            Long oldAmount = cache.get(id);
            Long newAmount = oldAmount + value;
            cache.replace(id, newAmount);
        } else {
            cache.put(id, value);
        }
    }
}
