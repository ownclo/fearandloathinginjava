public interface AccountService {
    Long getAmount(Integer id) throws Exception;
    void addAmount(Integer id, Long value) throws Exception;
}
