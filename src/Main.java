import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            System.out.println("No command given");
            return;
        }

        String command = args[0];

        switch (command) {
            case "init":
                Repository.init();
                break;

            case "add":
                Repository.add(args[1]);
                break;

            case "commit":
                Repository.commit(args[1]);
                break;

            case "log":
                Repository.log();
                break;
            
            case "branch":
                Repository.branch(args[1]);
                break;
            case "checkout":
                Repository.checkout(args[1]);
                 break;

            case "merge":
                Repository.merge(args[1]);
                break;

            case "@ai":
                String query = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                Repository.ai(query);
                break;

            default:
                System.out.println("Unknown command");
        }


    }
}

