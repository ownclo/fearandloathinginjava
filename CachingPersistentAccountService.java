
class CachingPersistentAccountService implements AccountService {
    CachingAccountService cachingAS;
    PersistentAccountService persistentAS;

    public CachingPersistentAccountService() throws Exception {
        persistentAS = new PersistentAccountService();
        cachingAS = new CachingAccountService(persistentAS.getAllAccounts());
    }

    @Override
    public Long getAmount(Integer id) throws Exception {
        return cachingAS.getAmount(id);
    }

    @Override
    public synchronized void addAmount(Integer id, Long value) throws Exception {
        cachingAS.addAmount(id, value);
        persistentAS.addAmount(id, value);
    }
}
