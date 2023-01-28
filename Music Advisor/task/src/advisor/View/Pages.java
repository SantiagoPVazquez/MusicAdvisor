package advisor.View;

import advisor.Main;

import java.util.List;
import java.util.Scanner;

public class Pages {
    private static Pages instance;

    private static int numberOfPages;

    private static int currentPage = 0;
    private static List<String> inputData;

    private Pages () { }

    public static Pages getPages() {
        if (instance == null) {
            instance = new Pages();
        }
        return instance;
    }

    public static void setPages(List<String> commandOutput) {
        currentPage = 0;
        inputData = commandOutput;
        numberOfPages = commandOutput.size()/Main.pageEntries + ((commandOutput.size()%Main.pageEntries == 0) ? 0 : 1);
    }

    public static void printPage() {
        boolean exitPage = false;
        int index = currentPage * Main.pageEntries;
        for (String entry : inputData.subList(index, Math.min(index + Main.pageEntries, inputData.size()))) {
            System.out.println(entry);
        }

        System.out.println("---PAGE " + (currentPage + 1) + " OF " + numberOfPages + "---");
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();

            switch (userInput) {
                case "next":
                    nextPage();
                    printPage();
                    break;
                case "prev":
                    previousPage();
                    printPage();
                    break;
                default:
                    break;
            }

    }

    public static void nextPage() {
        if (currentPage < numberOfPages - 1) {
            currentPage++;
        } else {
            System.out.println("No more pages.");
        }
    }

    public static void previousPage() {
        if (currentPage > 0) {
            currentPage--;
        } else {
            System.out.println("No more pages.");
        }
    }
}
