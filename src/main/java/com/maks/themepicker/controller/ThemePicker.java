package com.maks.themepicker.controller;

import com.maks.themepicker.components.WallpaperBox;
import com.maks.themepicker.model.Wallpaper;
import com.maks.themepicker.model.NullWallpaper;
import com.maks.themepicker.service.WallpaperService;
import com.maks.themepicker.utils.Config;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;

public class ThemePicker implements Initializable {

    @FXML
    private StackPane root;

    @FXML
    private WallpaperBox wallpapers;

    private int selectedIndex = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root.sceneProperty().addListener((o, v, scene) ->
            scene.setOnKeyPressed(e -> {
                switch (e.getCode()) {
                    case RIGHT -> {
                        int wallpapersCount = wallpapers.count();
                        if (selectedIndex < (wallpapersCount - 1)) {
                            updateSelection(selectedIndex + 1);
                        }
                    }
                    case LEFT -> {
                        if (selectedIndex > 0) {
                            updateSelection(selectedIndex - 1);
                        }
                    }
                    case ESCAPE -> exit();
                }
            })
        );

        try {
            loadWallpapers();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadWallpapers() throws InterruptedException {
        var service = new WallpaperService();
        BlockingQueue<Wallpaper> wallpaperQueue = service.loadWallpapers();

        Wallpaper wallpaper;
        while (!((wallpaper = wallpaperQueue.take()) instanceof NullWallpaper)) {
            wallpapers.add(wallpaper);
        }

        instantSelectFirst();
    }

    private void updateSelection(int newIndex) {
        Timeline deselectOld = deselectOld();
        Timeline selectNew = selectNew(newIndex);

        new ParallelTransition(deselectOld, selectNew).play();

        selectedIndex = newIndex;
    }

    private void instantSelectFirst() {
        selectNew(0, Duration.millis(1)).play();
    }

    private Timeline selectNew(int newIndex) {
        return selectNew(newIndex, Config.animationDuration());
    }

    private Timeline selectNew(int newIndex, Duration animationDuration) {
        Wallpaper wallpaper = wallpapers.get(newIndex);
        Rectangle newClip = wallpaper.clip();
        ColorAdjust newEffect = wallpaper.effect();

        return new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(newClip.widthProperty(), newClip.getWidth()),
                        new KeyValue(newClip.xProperty(), newClip.getX()),
                        new KeyValue(newEffect.brightnessProperty(), newEffect.getBrightness())
                ),
                new KeyFrame(animationDuration,
                        new KeyValue(newClip.widthProperty(), Config.selectedImageWidth()),
                        new KeyValue(newClip.xProperty(), (Config.imageWidth() - Config.selectedImageWidth()) / 2.0),
                        new KeyValue(newEffect.brightnessProperty(), 0)
                )
        );
    }

    private Timeline deselectOld() {
        Wallpaper wallpaper = wallpapers.get(selectedIndex);
        Rectangle oldClip = wallpaper.clip();
        ColorAdjust oldEffect = wallpaper.effect();

        return new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(oldClip.widthProperty(), oldClip.getWidth()),
                        new KeyValue(oldClip.xProperty(), oldClip.getX()),
                        new KeyValue(oldEffect.brightnessProperty(), oldEffect.getBrightness())
                ),
                new KeyFrame(Config.animationDuration(),
                        new KeyValue(oldClip.widthProperty(), Config.croppedImageWidth()),
                        new KeyValue(oldClip.xProperty(), (Config.imageWidth() - Config.croppedImageWidth()) / 2.0),
                        new KeyValue(oldEffect.brightnessProperty(), -0.5)
                )
        );
    }

    private void exit() {
        System.exit(0);
    }
}
