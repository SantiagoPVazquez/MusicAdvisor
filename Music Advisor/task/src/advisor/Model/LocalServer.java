package advisor.Model;

import advisor.Controller.Client;
import advisor.Main;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class LocalServer {
    protected static String clientId = "f4114a6723e34d8f84467a0c2a7b92c9";
    protected static String clientSecret = "aa835141e38f464c903c35349833acc9";
    protected static String redirectUri = "http://localhost:8080";
    private static String accessUrl = "https://accounts.spotify.com/authorize?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&response_type=code";
    protected static String code = "";
    public static void authorize() throws IOException, InterruptedException {
        // Creation of the server
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);
        // Creation of the server context
        server.createContext("/", new MyHandler());

        server.start();
        System.out.println("use this link to request the access code:");
        System.out.println(accessUrl);
        System.out.println("waiting for the code...");
        while (code.equals("")) {
            Thread.sleep(10);
        }
        server.stop(1);

        try {
            Client.requestToken();
            System.out.println("Success!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static class MyHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response;
            String query = exchange.getRequestURI().getQuery();
            code = query != null? query.split("=")[1] : "";
            if (query != null && query.contains("code")) {
                response = "Got the code. Return back to your program.";
                System.out.println("code received");
                exchange.sendResponseHeaders(200, response.length());
                Main.confirmedAccess = true;
            } else {
                response = "Authorization code not found. Try again.";
                exchange.sendResponseHeaders(400, response.length());
            }
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }
    }

    public static String getUserId() {
        return clientId;
    }

    public static String getUserSecret() {
        return clientSecret;
    }

    public static String getCode() {
        return code;
    }

    public static String getRedirectUri() {
        return redirectUri;
    }
}