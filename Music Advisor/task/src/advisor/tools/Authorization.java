package advisor.tools;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Authorization {
    private static String CODE;
    private static String SERVER_POINT = "https://accounts.spotify.com";
    private static final String CLIENT_ID = "eb4d6c645ce240008149a4e5145a817c";
    private static final String REDIRECT_URI = "http://localhost:8080";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String CLIENT_SECRET = "167c56d016cf420f8f91fee4e09e3437";


    public static void setServerPoint(String arg) {
        SERVER_POINT = arg;
    }

    public boolean authorize() {
        System.out.println("use this link to request the access code:");
        System.out.println(SERVER_POINT + "/authorize?"
                + "client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=code");
        getAuthCode();
        HttpResponse<String> response = getResponseBody();
        if (response != null) {
            System.out.println("response:");
            System.out.println(response.body());
            System.out.println("---SUCCESS---");
            return true;
        }
        return false;
    }

    private void getAuthCode() {
        try {
            HttpServer server = HttpServer.create();
            server.bind(new InetSocketAddress(8080), 0);
            server.start();
            System.out.println("waiting for code...");
            server.createContext("/", httpExchange -> {
                String query = httpExchange.getRequestURI().getQuery();
                String res;
                if (query.contains("code")) {
                    CODE = query.substring(5);
                    res = "Got the code. Return back to your program.";
                    System.out.println("code received");
                    System.out.println("making http request for access_token...");
                } else {
                    res = "Authorization code not found. Try again.";
                }
                httpExchange.sendResponseHeaders(200, res.length());
                httpExchange.getResponseBody().write(res.getBytes());
                httpExchange.getResponseBody().close();
            });
            while(CODE == null) {
                Thread.sleep(100);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private HttpResponse<String> getResponseBody() {
        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest requestAccess = HttpRequest.newBuilder()
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .uri(URI.create(SERVER_POINT + "/api/token"))
                    .POST(HttpRequest.BodyPublishers.ofString("grant_type=" + GRANT_TYPE
                            + "&code=" + CODE
                            + "&redirect_uri=" + REDIRECT_URI
                            + "&client_id=" + CLIENT_ID
                            + "&client_secret=" + CLIENT_SECRET))
                    .build();
            return client.send(requestAccess, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.getCause();
        }
        return null;
    }
}
