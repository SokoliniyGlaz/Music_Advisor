package view;

import model.Category;
import model.Novelty;
import model.PlayList;

import static view.Pagination.Page;

public class Viewer {

    public <E> void showItems(String command, Pagination<E> pagination) {
        Page<E> page;
        if (command.equals("next")) {
            page = pagination.getNext();
        } else {
            page = pagination.getPrev();
        }
        if (page != null && page.getElements() != null) {
            if (page.getElements().get(0) instanceof Category) {
                page.getElements().stream()
                        .map(item -> (Category) item)
                        .forEach(playList -> System.out.println(playList.getName() + "\n"));
            }
           else if (page.getElements().get(0) instanceof PlayList) {
                    page.getElements().stream()
                            .map(item -> (PlayList) item)
                            .forEach(playList -> System.out.println(playList.getName() + "\n" + playList.getUrl() + "\n"));
                }
             else if (page.getElements().get(0) instanceof Novelty) {
                page.getElements().stream()
                        .map(item -> (Novelty)item)
                        .forEach(nov -> System.out.println(nov.getName() + "\n" + nov.getArtist() + "\n" + nov.getUrl() + "\n"));
            }
            System.out.println("---PAGE " + pagination.getPageInd(page) + " OF " + pagination.sizeOfPages() + "---");
        } else {
            System.out.println("No more pages");
        }

    }
}
