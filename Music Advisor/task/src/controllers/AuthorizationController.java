package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class AuthorizationController {
    public static String CODE;
    public static String ACCESS_TOKEN;
    private static AuthorizationController authorizationController;
    public static String SERVER_POINT = "https://accounts.spotify.com";
    public static final String CLIENT_ID = "eb4d6c645ce240008149a4e5145a817c";
    public static final String REDIRECT_URI = "http://localhost:8080";
    public static final String GRANT_TYPE = "authorization_code";
    public static final String CLIENT_SECRET = "167c56d016cf420f8f91fee4e09e3437";

    private AuthorizationController() {
    }

    public static AuthorizationController getAuthorizationController() {
        if (authorizationController == null) {
            authorizationController = new AuthorizationController();
        }
        return authorizationController;
    }


    public String authorize() {
        System.out.println("use this link to request the access code:");
        System.out.println(SERVER_POINT + "/authorize?"
                + "client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=code");
        getAuthCode();
        HttpResponse<String> response = getResponseBody();
        if (response != null) {
            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            ACCESS_TOKEN = jo.get("access_token").getAsString();
            return "Success";
        }
        return "Not authorized";

    }

    public void getAuthCode() {
        try {
            HttpServer server = HttpServer.create();
            server.bind(new InetSocketAddress(8080), 0);
            server.start();
            System.out.println("waiting for code...");

            server.createContext("/", httpExchange -> {
                String query = httpExchange.getRequestURI().getQuery();
                String res = null;
                if (query == null || !query.contains("code")) {
                    res = "Authorization code not found. Try again.";
                }
                else  {
                    CODE = query.substring(5);
                    res = "Got the code. Return back to your program.";
                    System.out.println("code received");
                    System.out.println("making http request for access_token...");
                }
                httpExchange.sendResponseHeaders(200, res.length());
                httpExchange.getResponseBody().write(res.getBytes());
                httpExchange.getResponseBody().close();
            });
            while (CODE == null) {
                Thread.sleep(100);
            }
            server.stop(5);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public HttpResponse<String> getResponseBody() {
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
