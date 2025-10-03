package com.maks.themepicker.components;

import com.maks.themepicker.utils.Config;
import javafx.scene.image.Image;

import java.nio.file.Path;

public class WallpaperImage extends Image {
    public WallpaperImage(Path path) {
        super(
                path.toUri().toString(),
                Config.wallpaperResolutionWidth(),
                Config.wallpaperResolutionHeight(),
                false,
                true,
                true
        );
    }
}
