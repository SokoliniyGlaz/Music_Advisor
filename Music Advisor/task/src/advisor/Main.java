package advisor;

import advisor.tools.Service;
import controllers.AuthorizationController;
import controllers.MainController;
import model.Category;
import model.Novelty;
import model.PlayList;
import view.Pagination;
import view.Viewer;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length != 0) {
            if (args[0].equals("-access")) {
                AuthorizationController.SERVER_POINT = args[1];
            }
            if(args.length > 2) {
                if (args[2].equals("-resource")) {
                    Service.setResource(args[3]);
                }
            }
            if (args.length == 6) {
                if (args[4].equals("-page")) {
                    Pagination.numberOfElemOnPage = Integer.parseInt(args[5]);
                }
            }
        }
        Scanner scanner = new Scanner(System.in);
        boolean keepOn = true;
        while (keepOn) {
            keepOn = showOption(scanner);
        }
    }

    public static boolean showOption(Scanner scanner) {
        String input = scanner.nextLine();
        if (AuthorizationController.ACCESS_TOKEN == null && !input.equals("auth") && !input.equals("exit")) {
            System.out.println("Please, provide access for application.");
            return true;
        }
        if (AuthorizationController.ACCESS_TOKEN != null) {
            MainController controller = MainController.getMainController();
            Viewer viewer = new Viewer();
            while (!input.equals("exit")) {
                if ("new".equals(input)) {
                    List<Novelty> newSongs = controller.getNewSongs();
                    Pagination<Novelty> novelties = new Pagination<>(newSongs);
                    viewer.showItems("next", novelties);
                    input = scanner.nextLine();
                    while (input.equals("next") || input.equals("prev")) {
                        viewer.showItems(input, novelties);
                        input = scanner.nextLine();
                    }
                }
                if ("featured".equals(input)) {
                    List<PlayList> featuredPlayList = controller.getFeaturedPlayList();
                    Pagination<PlayList> playlists = new Pagination<>(featuredPlayList);
                    viewer.showItems("next",playlists);
                    input = scanner.nextLine();
                    while(input.equals("next") || input.equals("prev")) {
                        viewer.showItems(input, playlists);
                        input = scanner.nextLine();
                    }
                }
                if ("categories".equals(input)) {
                    List<Category> categories = controller.getCategories();
                    Pagination<Category> categoriesList = new Pagination<>(categories);
                    viewer.showItems("next",categoriesList);
                    input = scanner.nextLine();
                    while(input.equals("next") || input.equals("prev")) {
                        viewer.showItems(input, categoriesList);
                        input = scanner.nextLine();
                    }
                }
                if (input.startsWith("playlists")) {
                    String category = input.substring(10);
                    List<PlayList> playlistByCategoryId = controller.findPlaylistByCategoryId(category);
                    Pagination<PlayList> playlists = new Pagination<>(playlistByCategoryId);
                    viewer.showItems("next",playlists);
                    input = scanner.nextLine();
                    while(input.equals("next") || input.equals("prev")) {
                        viewer.showItems(input, playlists);
                        input = scanner.nextLine();
                    }
                }
            }
        }
        if ("auth".equals(input)) {
            AuthorizationController authController = AuthorizationController.getAuthorizationController();
            String authorize = authController.authorize();
            System.out.println(authorize);
            return true;
        } else {
            return false;
        }
    }
}
