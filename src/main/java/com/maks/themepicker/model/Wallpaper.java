package com.maks.themepicker.model;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class Wallpaper extends StackPane {
    private final String theme;
    private final Rectangle clip;
    private final ColorAdjust effect;

    public Wallpaper(ImageView imageView, String theme, Rectangle clip, ColorAdjust effect) {
        getChildren().add(imageView);
        this.theme = theme;
        this.clip = clip;
        this.effect = effect;
    }

    public String theme() {
        return theme;
    }

    public Rectangle clip() {
        return clip;
    }

    public ColorAdjust effect() {
        return effect;
    }
}
