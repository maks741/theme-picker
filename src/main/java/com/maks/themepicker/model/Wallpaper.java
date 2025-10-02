package com.maks.themepicker.model;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class Wallpaper extends StackPane {
    private final Rectangle clip;
    private final ColorAdjust effect;

    public Wallpaper(ImageView imageView, Rectangle clip, ColorAdjust effect) {
        getChildren().add(imageView);
        this.clip = clip;
        this.effect = effect;
    }

    public Rectangle clip() {
        return clip;
    }

    public ColorAdjust effect() {
        return effect;
    }
}
