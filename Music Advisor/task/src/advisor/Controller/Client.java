package advisor.Controller;

import advisor.Main;
import advisor.Model.LocalServer;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class Client {
    public static final HttpClient client = HttpClient.newBuilder().build();
    public static String accessToken = "";

    public static void requestToken() throws IOException, InterruptedException {
        String clientIdSecret = LocalServer.getUserId() + ":" + LocalServer.getUserSecret();
        String encodedIdSecret = Base64.getEncoder().encodeToString(clientIdSecret.getBytes());

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic " + encodedIdSecret)
                .uri(URI.create(Main.authServerPath + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=authorization_code"
                        + "&code=" + LocalServer.getCode()
                        + "&redirect_uri=" + LocalServer.getRedirectUri()))
                .build();

        System.out.println("Making http request for access_token...");
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        accessToken = JsonParser.parseString(response.body()).getAsJsonObject().get("access_token").getAsString();
    }

    public static String getRequestResponse(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(path))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}
