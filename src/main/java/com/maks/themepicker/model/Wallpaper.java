package com.maks.themepicker.model;

import com.maks.themepicker.utils.Config;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class Wallpaper extends StackPane {
    private final String theme;
    private final Rectangle clip;
    private final ColorAdjust effect;
    private final Pane border = new Pane();

    public Wallpaper(ImageView imageView, String theme, Rectangle clip, ColorAdjust effect) {
        getChildren().add(imageView);
        this.theme = theme;
        this.clip = clip;
        this.effect = effect;

        border.prefWidthProperty().bind(widthProperty());
        border.prefHeightProperty().bind(heightProperty());
        getChildren().add(border);
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

    public void setSelectedBorderColor() {
        setBorderColor(Config.selectedWallpaperBorderColor());
    }

    public void setDefaultBorderColor() {
        setBorderColor("transparent");
    }

    private void setBorderColor(String color) {
        border.setStyle("""
                    -fx-border-width: 2px;
                    -fx-border-color: "%s";
                    -fx-border-radius: %s;
         """.formatted(color, ((Config.clipArc() / 2.0) - 2)));
    }
}
