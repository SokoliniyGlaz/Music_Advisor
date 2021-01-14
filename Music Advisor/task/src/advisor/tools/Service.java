package advisor.tools;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class Service {
    private Map<String, String> categories = new HashMap<>();
    private boolean auth;
    private static Service service;
    private static String resource = "https://api.spotify.com";
    private final HttpClient client = HttpClient.newBuilder().build();

    private Service() {
    }

    public static Service getInstance() {
        if (service == null) {
            service = new Service();
        }
        return service;
    }

    public static void setResource(String resource) {
        Service.resource = resource;
    }

    private void getNewSongs() {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Authorization.ACCESS_TOKEN)
                .uri(URI.create(resource + "/v1/browse/new-releases"))
                .GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonArray jsonArray = jo.get("albums").getAsJsonObject()
                    .get("items").getAsJsonArray();
            for (JsonElement item : jsonArray) {
                List<String> artist_names = new ArrayList<>();
                String name = item.getAsJsonObject().get("name").getAsString().replaceAll("\"", "");
                item.getAsJsonObject().get("artists")
                        .getAsJsonArray()
                        .forEach(art -> artist_names.add(art.getAsJsonObject().get("name").getAsString()));
                String url = item.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString();
                System.out.println(name + "\n" + artist_names + "\n" + url + "\n");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getFeaturedPlayList() {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Authorization.ACCESS_TOKEN)
                .uri(URI.create(resource + "/v1/browse/featured-playlists"))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonArray items = jo.get("playlists").getAsJsonObject().get("items").getAsJsonArray();
            for (JsonElement item : items) {
                String name = item.getAsJsonObject().get("name").getAsString().replaceAll("\"", "");
                String url = item.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString();
                System.out.println(name + "\n" + url + "\n");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void getCategories() {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Authorization.ACCESS_TOKEN)
                .uri(URI.create(resource + "/v1/browse/categories"))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonArray items = jo.get("categories").getAsJsonObject().get("items").getAsJsonArray();
            for (JsonElement item : items) {
                String name = item.getAsJsonObject().get("name").getAsString();
                String key = item.getAsJsonObject().get("id").getAsString();
                categories.put(key, name);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getPlayLists(String category) {
        if (categories.isEmpty()) {
            getCategories();
        }
        Map.Entry<String, String> findCategory = categories.entrySet().stream()
                .filter(entry -> entry.getValue().equals(category))
                .findFirst().get();
        String category_id = findCategory.getKey();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Authorization.ACCESS_TOKEN)
                .uri(URI.create(resource + "/v1/browse/categories/" + category_id + "/playlists"))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            System.out.println(jo);
            if (jo.get("error") != null) {
                System.out.println("Test unpredictable error message");
            } else {
                JsonArray playlists = jo.get("playlists").getAsJsonObject().get("items").getAsJsonArray();
                for (JsonElement item : playlists) {
                    String url = item.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString();
                    String name = item.getAsJsonObject().get("name").getAsString();
                    System.out.println(name + "\n" + url + "\n");
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean showOption(Scanner scanner) {
        String input = scanner.nextLine();
        if (!auth && !input.equals("auth") && !input.equals("exit")) {
            System.out.println("Please, provide access for application.");
            return true;
        } else if ("new".equals(input)) {
            getNewSongs();
        } else if ("featured".equals(input)) {
            getFeaturedPlayList();
        } else if ("categories".equals(input)) {
            if (categories.isEmpty()) {
                getCategories();
            }
            categories.forEach((key, value) -> System.out.println(value));
        } else if (input.startsWith("playlists")) {
            String category = input.substring(10);
            getPlayLists(category);
        } else if ("auth".equals(input)) {
            Authorization authorization = new Authorization();
            this.auth = authorization.authorize();
        } else if ("exit".equals(input)) {
            return false;
        } else {
            System.out.println("Unsupported command");
        }
        return true;
    }


}


