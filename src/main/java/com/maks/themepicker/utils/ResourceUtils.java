package com.maks.themepicker.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceUtils {

    public static Path themesDirPath() {
        return basePath().resolve("themes");
    }

    public static Path configFilePath() {
        Path relativePath = Paths.get("theme-picker", "config.yaml");
        return basePath().resolve(relativePath);
    }

    private static Path basePath() {
        return Paths.get(System.getProperty("user.home"), ".config");
    }
}
