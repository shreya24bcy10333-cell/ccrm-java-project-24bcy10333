package edu.ccrm.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AppConfig {
    private static final AppConfig INSTANCE = new AppConfig();
    private Path dataFolder = Paths.get("./data");

    private AppConfig() {}

    public static AppConfig getInstance() { return INSTANCE; }

    public void loadDefaults() {
        System.out.println("AppConfig loaded. Data folder: " + dataFolder.toAbsolutePath());
    }

    public Path getDataFolder() { return dataFolder; }
}
