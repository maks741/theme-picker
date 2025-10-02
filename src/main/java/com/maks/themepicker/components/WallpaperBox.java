package com.maks.themepicker.components;

import com.maks.themepicker.model.Wallpaper;
import com.maks.themepicker.utils.Config;
import javafx.scene.layout.HBox;

public class WallpaperBox extends HBox {

    public WallpaperBox() {
        setPrefWidth(Config.screenWidth());
        setPrefHeight(Config.screenHeight());
    }

    public void add(Wallpaper wallpaper) {
        super.getChildren().add(wallpaper);
    }

    public Wallpaper get(int index) {
        return ((Wallpaper) super.getChildren().get(index));
    }

    public int count() {
        return super.getChildren().size();
    }
}
