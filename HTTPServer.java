import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import java.net.*;
import com.sun.net.httpserver.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class HTTPServer {
    final AccountService as;
    final HttpServer server;
    final ExecutorService executor;
    final StatsSuite stats;

    public HTTPServer(AccountService accountService,
            int nThreads,
            int port)
        throws IOException {

        as = accountService;
        server = HttpServer.create(new InetSocketAddress(port), 0);
        executor = Executors.newFixedThreadPool(nThreads);

        stats = new StatsSuite();
        server.createContext("/accounts", new AmountsHandler(as, stats));
        server.setExecutor(executor);
    }

    public void start() {
        stats.start();

        try { 
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class StatsSuite {
        public TimedCounter addAmountCounter;
        public TimedCounter getAmountCounter;

        public StatsSuite() {
            addAmountCounter = new TimedCounter();
            getAmountCounter = new TimedCounter();
        }

        public void start() {
            addAmountCounter.start();
            getAmountCounter.start();
        }

        public void reset() {
            // NOTE: No need for synchronization, assuming that
            // uber-strict stats are not required.
            addAmountCounter.reset();
            getAmountCounter.reset();
        }
    }

    static class AmountsHandler implements HttpHandler {
        AccountService accountService;
        StatsSuite statsSuite;

        public AmountsHandler(AccountService as, StatsSuite stats) {
            accountService = as;
            statsSuite = stats;
        }

        public void handle(HttpExchange t) throws IOException {
            String reqMethod = t.getRequestMethod();
            String response = "";

            try {
                switch(reqMethod) {
                    case "GET":  response = handleGET(t);  break;
                    case "POST": response = handlePOST(t); break;
                    default: throw new IOException("Unsupported request method");
                }
            }
            catch (IOException e) { throw e; }
            catch (Exception e) { e.printStackTrace(); };

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        String handleGET(HttpExchange t) throws Exception {
            String[] uriPieces = t.getRequestURI().toString().split("/");

            String response = "";
            if (uriPieces[2].equals("statistics")) {
                if (uriPieces[3].equals("getamount")) {
                    int totalCalls = statsSuite.getAmountCounter.getCounter();
                    float callRate = statsSuite.getAmountCounter.ticksPerSecond();
                    response = "" + totalCalls + "; " + callRate;
                } else if (uriPieces[3].equals("addamount")) {
                    int totalCalls = statsSuite.addAmountCounter.getCounter();
                    float callRate = statsSuite.addAmountCounter.ticksPerSecond();
                    response = "" + totalCalls + "; " + callRate;
                }
            } else { /* /accounts/:ID */
                Integer id = getIdFromURI(uriPieces);
                response = accountService.getAmount(id).toString();
                statsSuite.getAmountCounter.tick();
            }
            return response;
        }

        String handlePOST(HttpExchange t) throws Exception {
            String[] uriPieces = t.getRequestURI().toString().split("/");
            String response = "";
            if (uriPieces[2].equals("resetstats")) {
                statsSuite.reset();
            } else { /* POST /accounts/:ID, :VAL is in body */
                Integer id = getIdFromURI(uriPieces);
                String body = convertStreamToString(t.getRequestBody());
                Long amount = getAmountFromBody(body);

                accountService.addAmount(id, amount);
                statsSuite.addAmountCounter.tick();
            }
            return response;
        }

        Integer getIdFromURI(String[] pieces) {
            return Integer.parseInt(pieces[pieces.length - 1], 10);
        }

        Long getAmountFromBody(String body) {
            return Long.parseLong(body, 10);
        }

        static String convertStreamToString(java.io.InputStream is) {
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
    }
}
