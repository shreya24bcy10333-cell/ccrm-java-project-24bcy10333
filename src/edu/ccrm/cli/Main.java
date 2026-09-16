package edu.ccrm.cli;

import edu.ccrm.config.AppConfig;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Load default config
        AppConfig.getInstance().loadDefaults();

        // Create scanner for user input
        try (Scanner sc = new Scanner(System.in)) {
            ConsoleMenu menu = new ConsoleMenu(sc);
            menu.runLoop();  // Start CLI loop
        }
    }
}
