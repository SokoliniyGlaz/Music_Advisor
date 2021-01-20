package controllers;

import advisor.tools.Service;
import model.Category;
import model.Novelty;
import model.PlayList;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static advisor.tools.Service.resource;
import static controllers.AuthorizationController.ACCESS_TOKEN;

public class MainController {
    private static MainController mainController;
    private static HttpClient client;
    private Service service;

    private MainController() {
    }

    public static MainController getMainController() {
        if (mainController == null) {
            mainController = new MainController();
            mainController.service = Service.getInstance();
            client = HttpClient.newBuilder().build();
        }
        return mainController;
    }

    public List<Novelty> getNewSongs() {
        List<Novelty> newSongs = null;
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .uri(URI.create(resource + "/v1/browse/new-releases"))
                .GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            newSongs = service.getListSongs(response.body());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return newSongs;
    }

    public List<PlayList> getFeaturedPlayList() {
        List<PlayList> playLists = new ArrayList<>();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .uri(URI.create(resource + "/v1/browse/featured-playlists"))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            playLists = service.getPlayLists(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return playLists;
    }

    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .uri(URI.create(resource + "/v1/browse/categories"))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            categories = service.getCategoriesList(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public List<PlayList> findPlaylistByCategoryId(String category_name) {
        List<PlayList> playLists = new ArrayList<>();
        if (service.getCategories().isEmpty()) {
            getCategories();
        }
        String category_id = service.findCategoryByName(category_name);
        if (category_id == null) {
            System.out.println("Such category doesn't exist!");
            return null;
        }
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .uri(URI.create(resource + "/v1/browse/categories/" + category_id + "/playlists"))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            playLists = service.getPlayLists(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return playLists;
    }
}
