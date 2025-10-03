package com.maks.themepicker.utils;

import javafx.stage.Screen;
import javafx.util.Duration;

public class Config {

    private static final double screenWidth;
    private static final double screenHeight;
    private static final double imageWidth;
    private static final double imageHeight;
    private static final double croppedImageWidth;
    private static final double selectedImageWidth;
    private static final Duration animationDuration = Duration.millis(100);
    private static final double arcSize = 35;
    private static final double dimmedWallpaperBrightness = -0.5;
    private static final double dimmedBackgroundBrightness = -0.75;
    private static final double backgroundBlur = 0;

    static {
        Screen screen = Screen.getPrimary();
        screenWidth = screen.getBounds().getWidth();
        screenHeight = screen.getBounds().getHeight();
        imageHeight = screenHeight * 0.6;
        imageWidth = imageHeight * (16.0 / 9.0);
        croppedImageWidth = imageWidth * 0.2;
        selectedImageWidth = imageWidth * 0.5;
    }

    public static double screenWidth() {
        return screenWidth;
    }

    public static double screenHeight() {
        return screenHeight;
    }

    public static double imageWidth() {
        return imageWidth;
    }

    public static double imageHeight() {
        return imageHeight;
    }

    public static double croppedImageWidth() {
        return croppedImageWidth;
    }

    public static double selectedImageWidth() {
        return selectedImageWidth;
    }

    public static Duration animationDuration() {
        return animationDuration;
    }

    public static double arcSize() {
        return arcSize;
    }

    public static double dimmedWallpaperBrightness() {
        return dimmedWallpaperBrightness;
    }

    public static double dimmedBackgroundBrightness() {
        return dimmedBackgroundBrightness;
    }

    public static double backgroundBlur() {
        return backgroundBlur;
    }
}
