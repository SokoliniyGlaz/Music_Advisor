package advisor.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Service {
    private boolean auth;
    private static Service service;
    public List<String> releases = new ArrayList<>(Arrays.asList("Mountains [Sia, Diplo, Labrinth]",
            "Runaway [Lil Peep]", "The Greatest Show [Panic! At The Disco]", "All Out Life [Slipknot]"));
    public List<String> featured = new ArrayList<>(Arrays.asList("Mellow Morning", "Wake Up and Smell the Coffee",
            "Monday Motivation", "Songs to Sing in the Shower"));
    public List<Category> categories = new ArrayList<>(Arrays.asList(new Category("Top Lists"),
            new Category("Pop"), new Category("Mood"), new Category("Latin")));

    private Service() {
    }

    public static Service getInstance() {
        if (service == null) {
            service = new Service();
        }
        return service;
    }

    public List<String> getReleases() {
        return releases;
    }

    public void setReleases(List<String> releases) {
        this.releases = releases;
    }

    public List<String> getFeatured() {
        return featured;
    }

    public void setFeatured(List<String> featured) {
        this.featured = featured;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Category getPlaylist(String categoryName) {
        Category fullCategory = categories.stream()
                .filter(category -> category.getName().equals(categoryName))
                .findFirst()
                .orElse(null);
        if (fullCategory != null) {
           fullCategory.setTracks(Arrays.asList("Walk Like A Badass", "Rage Beats", "Arab Mood Booster", "Sunday Stroll"));
        }
        return fullCategory;

    }

    public boolean showOption(Scanner scanner) {
        String input = scanner.nextLine();
        if (!auth && !input.equals("auth") && !input.equals("exit")) {
            System.out.println("Please, provide access for application.");
            return true;
        }
        switch (input) {
            case "new": {
                System.out.println("---NEW RELEASES---");
                releases.forEach(System.out::println);
                break;
            }
            case "featured": {
                System.out.println("---FEATURED---");
                featured.forEach(System.out::println);
                break;
            }
            case "categories": {
                System.out.println("---CATEGORIES---");
                categories.forEach(s -> System.out.println(s.getName()));
                break;
            }
            case "exit": {
                System.out.println("---GOODBYE!---");
                return false;
            }
            case "auth": {
                Authorization authorization = new Authorization();
                this.auth = authorization.authorize();
                break;
            }
            case "mood": {
                System.out.println("---MOOD PLAYLISTS---");
                String[] category = input.split(" ");
                getPlaylist(category[1]).getTracks().forEach(System.out::println);
            }
            default: {
                break;
            }
        }
        return true;
    }


}


