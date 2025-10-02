package com.maks.themepicker.components;

import com.maks.themepicker.model.Wallpaper;
import com.maks.themepicker.utils.Config;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class WallpaperBox extends HBox {

    public WallpaperBox() {
        setPrefWidth(Config.screenWidth());
        setPrefHeight(Config.screenHeight());
    }

    public void add(Wallpaper wallpaper) {
        getChildren().add(wallpaper);
    }

    public Wallpaper get(int index) {
        Node node = getChildren().get(index);

        if (node instanceof Wallpaper wallpaper) {
            return wallpaper;
        } else {
            throw new IllegalArgumentException("Nodes other than Wallpaper are not supported");
        }
    }

    public int count() {
        return getChildren().size();
    }
}
