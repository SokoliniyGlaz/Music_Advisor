package advisor;

import advisor.tools.Authorization;
import advisor.tools.Service;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length != 0 && "-access".equals(args[0])) {
            Authorization.setServerPoint(args[1]);
        }
      Scanner scanner = new Scanner(System.in);
      Service service = Service.getInstance();
      boolean keepOn = true;
      while (keepOn) {
           keepOn = service.showOption(scanner);
      }
    }
}
