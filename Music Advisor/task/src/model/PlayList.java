package model;

public class PlayList {
    private String name;
    private String url;

    public PlayList(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
