package com.maks.themepicker.model;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public record Wallpaper(StackPane wallpaperContainer, Rectangle clip, ColorAdjust effect) {}
