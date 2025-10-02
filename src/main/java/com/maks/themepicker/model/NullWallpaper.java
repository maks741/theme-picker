package com.maks.themepicker.model;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class NullWallpaper extends Wallpaper {
    public NullWallpaper() {
        super(new ImageView(), new Rectangle(), new ColorAdjust());
    }
}
