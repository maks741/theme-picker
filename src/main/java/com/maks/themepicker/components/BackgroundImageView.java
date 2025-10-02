package com.maks.themepicker.components;

import com.maks.themepicker.utils.Config;
import com.maks.themepicker.utils.ResourceUtils;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BackgroundImageView extends ImageView {
    public BackgroundImageView() {
        super(new Image(ResourceUtils.currentWallpaperPath().toUri().toString(), 640, 360, false, true));

        setFitWidth(Config.screenWidth());
        setFitHeight(Config.screenHeight());

        ColorAdjust dim = new ColorAdjust();
        dim.setBrightness(-0.75);
        setEffect(dim);
    }
}
