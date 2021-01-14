package advisor;

import advisor.tools.Authorization;
import advisor.tools.Service;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length != 0) {
                if (args[0].equals("-access")) {
                    Authorization.setServerPoint(args[1]);
                }
                if (args[2].equals("-resource")) {
                    Service.setResource(args[3]);
                }
            }
      Scanner scanner = new Scanner(System.in);
      Service service = Service.getInstance();
      boolean keepOn = true;
      while (keepOn) {
           keepOn = service.showOption(scanner);
      }
    }

}
