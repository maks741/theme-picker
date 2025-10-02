package com.maks.themepicker.model;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class Wallpaper extends StackPane {
    private final String themeName;
    private final Rectangle clip;
    private final ColorAdjust effect;

    public Wallpaper(ImageView imageView, String themeName, Rectangle clip, ColorAdjust effect) {
        getChildren().add(imageView);
        this.themeName = themeName;
        this.clip = clip;
        this.effect = effect;
    }

    public String themeName() {
        return themeName;
    }

    public Rectangle clip() {
        return clip;
    }

    public ColorAdjust effect() {
        return effect;
    }
}
