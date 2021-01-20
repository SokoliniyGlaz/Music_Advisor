package advisor.tools;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Category;
import model.Novelty;
import model.PlayList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Service {
    private Map<String, Category> categories = new HashMap<>();
    public static Service service;
    public static String resource = "https://api.spotify.com";


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
    public Map<String, Category> getCategories() {
        return categories;
    }

    public List<Novelty> getListSongs(String body) {
        List<Novelty> newSongs = new ArrayList<>();
        JsonObject jo = JsonParser.parseString(body).getAsJsonObject();
        JsonArray jsonArray = jo.get("albums").getAsJsonObject()
                .get("items").getAsJsonArray();

        for (JsonElement item : jsonArray) {
            List<String> artist_names = new ArrayList<>();
            String name = item.getAsJsonObject().get("name").getAsString().replaceAll("\"", "");
            item.getAsJsonObject().get("artists")
                    .getAsJsonArray()
                    .forEach(art -> artist_names.add(art.getAsJsonObject().get("name").getAsString()));
            String url = item.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString();
            newSongs.add(new Novelty(name, artist_names, url));
        }
        return newSongs;
    }

    public List<Category> getCategoriesList(String body) {
        List<Category> categories = new ArrayList<>();
        JsonObject jo = JsonParser.parseString(body).getAsJsonObject();
        JsonArray items = jo.get("categories").getAsJsonObject().get("items").getAsJsonArray();
        for (JsonElement item : items) {
            String name = item.getAsJsonObject().get("name").getAsString();
            String key = item.getAsJsonObject().get("id").getAsString();
            Category category = new Category(key, name);
            categories.add(category);
            this.categories.put(category.getName(), category);
        }
        return categories;
    }

    public List<PlayList> getPlayLists(String body) {
        List<PlayList> playLists = new ArrayList<>();
        JsonObject jo = JsonParser.parseString(body).getAsJsonObject();
        JsonArray items = jo.get("playlists").getAsJsonObject().get("items").getAsJsonArray();
        if (jo.get("error") != null) {
            System.out.println("Test unpredictable error message");
            return null;
        }
        for (JsonElement item : items) {
            String name = item.getAsJsonObject().get("name").getAsString().replaceAll("\"", "");
            String url = item.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString();
            playLists.add(new PlayList(name, url));
        }
        return playLists;
    }
    public String findCategoryByName(String name) {
        Map.Entry<String, Category> findCategory = service.getCategories().entrySet().stream()
                .filter(entry -> entry.getKey().equals(name))
                .findFirst().orElse(null);
        if (findCategory == null) {
            return null;
        }
       return findCategory.getValue().getId();
    }
}


