package advisor;

import advisor.Controller.Commands;
import advisor.Model.LocalServer;
import advisor.View.*;
import java.io.IOException;
import java.util.Scanner;
public class Main {
    public static boolean confirmedAccess = false;
    public static String authServerPath = "https://accounts.spotify.com";

    public static String resourceServerPath = "https://api.spotify.com";

    private static String arguments = "";

    public static int pageEntries = 5;

    public static Pages display = Pages.getPages();

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        // Read initial arguments to modify access
        if (args.length > 1) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-access")) {
                    authServerPath = args[i+1];
                } else if (args[i].equals("-resource")) {
                    resourceServerPath = args[i + 1];
                } else if (args[i].equals("-page")) {
                    pageEntries = Integer.parseInt(args[i + 1]);
                }
            }
        }

        // Start of the program loop
        while (!exit) {

            // Set the scanner to read user input
            String[] userInput = scanner.nextLine().split(" ");

            // Reads the arguments that are not a command
            for (int i = 1; i < userInput.length; i++) {
                arguments = arguments + userInput[i] + " ";
            }

            // Authorization to access all the commands
            if(userInput[0].equals("auth")) {
                if(!confirmedAccess) {
                    LocalServer.authorize();
                    continue;
                } else {
                    System.out.println("Already authorized.");
                    continue;
                }
            } else if(userInput[0].equals("exit")){
                System.out.println("---GOODBYE!---");
                exit = true;
                continue;
            }
            // When authorized, the other commands can be used
            if (confirmedAccess) {
                switch (userInput[0]) {
                    case "new":
                        display.setPages(Commands.newCommand());
                        display.printPage();
                        break;
                    case "featured":
                        display.setPages(Commands.featuredCommand());
                        display.printPage();
                        break;
                    case "categories":
                        display.setPages(Commands.categoriesCommand());
                        display.printPage();
                        break;
                    case "playlists":
                        display.setPages(Commands.playlistCommand(arguments));
                        display.printPage();
                        break;
                    default:
                        System.out.println("Unknown command");
                        break;
                }
            } else {
                System.out.println("Please, provide access for application.");
            }

        }
    }
}
