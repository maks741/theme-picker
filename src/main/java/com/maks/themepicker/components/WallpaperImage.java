package com.maks.themepicker.components;

import javafx.scene.image.Image;

import java.nio.file.Path;

public class WallpaperImage extends Image {
    public WallpaperImage(Path path) {
        super(path.toUri().toString(), 1280, 720, false, true, true);
    }
}
