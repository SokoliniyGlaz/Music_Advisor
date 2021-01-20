package view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Pagination<E> {
    private final List<E> elements;
    public static int numberOfElemOnPage = 5;
    private final LinkedList<Page<E>> pages;
    private Page<E> currentPage;

    public static class Page<E> {

        public List<E> content;

        public Page(List<E> content) {
            this.content = content;
        }

        public List<E> getElements() {
            return new ArrayList<>(content);
        }
    }

    public Pagination(List<E> elements) {
        this.elements = elements;
        this.pages = new LinkedList<>();
    }

    private void setPages() {
        if (!elements.isEmpty()) {
            int numOfPages = elements.size() / numberOfElemOnPage;
            if (elements.size() % numberOfElemOnPage != 0) {
                numOfPages++;
            }
            int ind = 0;
            for (int i = 0; i < numOfPages; i++) {
                if (i == numOfPages - 1) {
                    pages.add(new Page<>(elements.subList(ind,elements.size())));
                } else {
                    pages.add(new Page<>(elements.subList(ind, ind + numberOfElemOnPage)));
                    ind += numberOfElemOnPage;
                }
            }
        }
    }
    public Page<E> getNext() {
        if (pages.isEmpty()) {
            setPages();
        }
        if (currentPage == null) {
            currentPage = pages.getFirst();
            return currentPage;
        }
         if (currentPage == pages.getLast()) {
             return null;
         }
         currentPage = pages.get(pages.indexOf(currentPage) + 1);
         return currentPage;

    }

    public Page<E> getPrev() {
        if (pages.isEmpty()) {
            setPages();
        }
        if (currentPage == null || currentPage == pages.getFirst()) {
            return null;
        }

        currentPage = pages.get(pages.indexOf(currentPage) - 1);
        return currentPage;
    }

    public int getPageInd(Page<E> page) {
        return pages.indexOf(page) + 1;
    }

    public int sizeOfPages() {
        return pages.size();
    }

}

