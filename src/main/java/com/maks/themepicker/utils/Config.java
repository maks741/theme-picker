package com.maks.themepicker.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.io.IOException;

public class Config {

    private static final double screenWidth;
    private static final double screenHeight;

    private static final double spacing;

    private static final double wallpaperWidth;
    private static final double wallpaperHeight;
    private static final double wallpaperResolutionWidth;
    private static final double wallpaperResolutionHeight;
    private static final double unselectedWallpaperBrightness;
    private static final String selectedWallpaperBorderColor;

    private static final double unselectedClipWidth;
    private static final double selectedClipWidth;
    private static final double unselectedClipHeight;
    private static final double selectedClipHeight;
    private static final double clipArc;

    private static final Duration animationDuration;

    static {
        var mapper = new ObjectMapper(new YAMLFactory());
        YAMLConfig yamlConfig;
        try {
            yamlConfig = mapper.readValue(ResourceUtils.configFilePath().toFile(), YAMLConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Screen screen = Screen.getPrimary();
        screenWidth = screen.getBounds().getWidth();
        screenHeight = screen.getBounds().getHeight();

        spacing = yamlConfig.app.spacing;

        Size wallpaperResolution = new Size(yamlConfig.wallpaper.resolution);
        wallpaperHeight = screenHeight * yamlConfig.clip.selectedHeight;
        wallpaperWidth = wallpaperHeight * (wallpaperResolution.width / wallpaperResolution.height);
        wallpaperResolutionWidth = wallpaperResolution.width;
        wallpaperResolutionHeight = wallpaperResolution.height;
        unselectedWallpaperBrightness = yamlConfig.wallpaper.unselectedBrightness;
        selectedWallpaperBorderColor = yamlConfig.wallpaper.selectedBorderColor;

        unselectedClipWidth = screenWidth * yamlConfig.clip.unselectedWidth;
        selectedClipWidth = screenWidth * yamlConfig.clip.selectedWidth;
        unselectedClipHeight = screenHeight * yamlConfig.clip.unselectedHeight;
        selectedClipHeight = screenHeight * yamlConfig.clip.selectedHeight;
        clipArc = yamlConfig.clip.arc;

        animationDuration = Duration.millis(yamlConfig.animation.durationMillis);
    }

    public static double screenWidth() {
        return screenWidth;
    }

    public static double screenHeight() {
        return screenHeight;
    }

    public static double spacing() {
        return spacing;
    }

    public static double wallpaperWidth() {
        return wallpaperWidth;
    }

    public static double wallpaperHeight() {
        return wallpaperHeight;
    }

    public static double wallpaperResolutionWidth() {
        return wallpaperResolutionWidth;
    }

    public static double wallpaperResolutionHeight() {
        return wallpaperResolutionHeight;
    }

    public static double unselectedWallpaperBrightness() {
        return unselectedWallpaperBrightness;
    }

    public static String selectedWallpaperBorderColor() {
        return selectedWallpaperBorderColor;
    }

    public static double unselectedClipWidth() {
        return unselectedClipWidth;
    }

    public static double selectedClipWidth() {
        return selectedClipWidth;
    }

    public static double unselectedClipHeight() {
        return unselectedClipHeight;
    }

    public static double selectedClipHeight() {
        return selectedClipHeight;
    }

    public static double clipArc() {
        return clipArc;
    }

    public static Duration animationDuration() {
        return animationDuration;
    }

    private record YAMLConfig(
            App app,
            Wallpaper wallpaper,
            Clip clip,
            Animation animation
    ) {
    }

    private record App(
            double spacing
    ) {}

    private record Wallpaper(
            String resolution,
            double unselectedBrightness,
            String selectedBorderColor
    ) {}

    private record Clip(
            double unselectedWidth,
            double selectedWidth,
            double unselectedHeight,
            double selectedHeight,
            int arc
    ) {}

    private record Animation(
            int durationMillis
    ) {}

    private record Size(
            double width,
            double height
    ) {
        public Size(String s) {
            this(
                    Double.parseDouble(s.split("x")[0]),
                    Double.parseDouble(s.split("x")[1])
            );
        }
    }
}
