package com.maks.themepicker.components;

import com.maks.themepicker.utils.Config;
import com.maks.themepicker.utils.ResourceUtils;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BackgroundImageView extends ImageView {
    public BackgroundImageView() {
        super(new Image(ResourceUtils.currentWallpaperPath().toUri().toString(), 640, 360, false, true));

        setFitWidth(Config.screenWidth());
        setFitHeight(Config.screenHeight());

        ColorAdjust dim = new ColorAdjust();
        dim.setBrightness(Config.dimmedBackgroundBrightness());

        GaussianBlur dimmedBlur = new GaussianBlur(Config.backgroundBlur());
        dimmedBlur.setInput(dim);

        setEffect(dimmedBlur);
    }
}
