package model;

import java.util.List;

public class Novelty{
    private String name;
    private List<String> artist;
    private String url;

    public Novelty(String name, List<String> artist, String url) {
        this.name = name;
        this.artist = artist;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public List<String> getArtist() {
        return artist;
    }

    public String getUrl() {
        return url;
    }
}
