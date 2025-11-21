package utilities;

import java.util.Random;
import java.util.Scanner;

public class UserInterface {
    // ansi color codes 

    public static final String RESET = "\u001B[0m";
    public static final String DARK_RED = "\u001B[31m";
    public static final String RED = "\u001B[91m";
    public static final String BLUE = "\u001B[94m";
    public static final String GREEN = "\u001B[92m";
    public static final String PURPLE = "\u001B[95m";
    public static final String PURPLE_BOLD = "\033[1;35m";
    public static final String ORANGE = "\u001B[38;5;208m";
    public static final String YELLOW = "\u001B[33m";
    public static final String GOLD_YELLOW = "\u001B[38;2;255;215;0m";
    public static final String WHITE = "\u001B[37m";
    public static final String TAN = "\u001B[38;5;180m";


    public static void enterReturn(Scanner scanner) {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public static void delay() {
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
    }

    public static void delay(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) {}
    }

    public static void randomDelay(int bound) {
        Random random = new Random();
        try { Thread.sleep(1000 + random.nextInt(bound)); } catch (InterruptedException e) {}
    }

    public static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Error clearing console.");
        }
    }
}
