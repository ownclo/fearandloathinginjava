import java.util.Random;

public class TestClient {
    public static void main(String[] args) {
        System.out.println("WOW! MANY TEST CLIENT! SUCH HIGHLOAD!");

        String basename = "";
        int nWriters = 0;
        int nReaders = 0;
        Integer idMin = 0;
        Integer idMax = 0;

        try {
            basename = args[0];
            nReaders = Integer.parseInt(args[1]);
            nWriters = Integer.parseInt(args[2]);
            idMin = Integer.parseInt(args[3]);
            idMax = Integer.parseInt(args[4]);
        } catch(Exception e) {
            System.out.println("Usage: TestClient BNAME NREADERS NWRITERS IDMIN IDMAX");
            e.printStackTrace();
        }

        final HttpClientAccountService as = new HttpClientAccountService(basename);

        startWriterThreads(nWriters, as, idMin, idMax);
        startReaderThreads(nReaders, as, idMin, idMax);
    }

    static void startWriterThreads(int nWriters,
            final HttpClientAccountService as,
            final Integer idMin,
            final Integer idMax) {

        for (int i = 0; i < nWriters; i++) {
            Thread writer = new Thread(new Runnable() {
                public void run() {
                    try {
                        Long amount = 1L;
                        testAddAmount(as, idMin, idMax, amount);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            writer.start();
        }
    }


    static void startReaderThreads(int nReaders,
            final HttpClientAccountService as,
            final Integer idMin,
            final Integer idMax) {

        for (int i = 0; i < nReaders; i++) {
            Thread reader = new Thread(new Runnable() {
                public void run() {
                    testGetAmount(as, idMin, idMax);
                }
            });
            reader.start();
        }
    }


    static void testGetAmount(HttpClientAccountService as,
            Integer idMin,
            Integer idMax) {
        while (true) {
            Integer id = randInt(idMin, idMax);
            Long amount = as.getAmount(id);
            System.out.println("Amount for Id #" + id + " is " + amount);
        }
    }


    static void testAddAmount(
            HttpClientAccountService as,
            Integer idMin,
            Integer idMax,
            Long value)
        throws Exception {

        while (true) {
            Integer id = randInt(idMin, idMax);
            as.addAmount(id, value);
            System.out.println("Added " + value + " ubercoins for account #" + id);
        }
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
