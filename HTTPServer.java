import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import java.net.*;
import com.sun.net.httpserver.*;

public class HTTPServer {

    public static void start(AccountService as) {
        try { 
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/amounts", new MyHandler(as));
            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class MyHandler implements HttpHandler {
        AccountService accountService;

        public MyHandler(AccountService as) {
            accountService = as;
        }

        public void handle(HttpExchange t) throws IOException {
            URI uri = t.getRequestURI();
            String[] pieces = uri.toString().split("/");

            String reqMethod = t.getRequestMethod();
            String response = "";

            Integer id;
            switch(reqMethod) {
                case "GET":
                    id = getIdFromURI(pieces);
                    try {
                        response = accountService.getAmount(id).toString();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case "POST":
                    id = getIdFromURI(pieces);
                    String body = convertStreamToString(t.getRequestBody());
                    Long amount = getAmountFromBody(body);
                    try {
                        accountService.addAmount(id, amount);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    throw new IOException("Unsupported request method");
            }

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
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