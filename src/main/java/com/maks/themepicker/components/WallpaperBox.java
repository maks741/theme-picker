package com.maks.themepicker.components;

import com.maks.themepicker.model.Wallpaper;
import com.maks.themepicker.utils.Config;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class WallpaperBox extends HBox {

    private int selectedIndex = 0;

    public WallpaperBox() {
        setPrefWidth(Config.screenWidth());
        setPrefHeight(Config.screenHeight());
    }

    public void postInit() {
        instantSelectMiddleElement();

        boolean evenNumberOfWallpapers = count() % 2 == 0;
        if (evenNumberOfWallpapers) {
            setLayoutX(Config.croppedImageWidth() / 2.0);
        }
    }

    public void add(Wallpaper wallpaper) {
        getChildren().add(wallpaper);
    }

    public void selectNext() {
        int wallpapersCount = count();
        if (selectedIndex < (wallpapersCount - 1)) {
            updateSelection(selectedIndex + 1);
        }
    }

    public void selectPrevious() {
        if (selectedIndex > 0) {
            updateSelection(selectedIndex - 1);
        }
    }

    public Wallpaper selected() {
        return get(selectedIndex);
    }

    private void instantSelectMiddleElement() {
        selectedIndex = Math.ceilDiv(count(), 2) - 1;
        selectNew(selectedIndex, Duration.millis(1)).play();
    }

    private void updateSelection(int newIndex) {
        Timeline deselectOld = deselectOld();
        Timeline selectNew = selectNew(newIndex);

        new ParallelTransition(deselectOld, selectNew).play();

        selectedIndex = newIndex;
    }

    private Timeline selectNew(int newIndex) {
        return selectNew(newIndex, Config.animationDuration());
    }

    private Timeline selectNew(int newIndex, Duration animationDuration) {
        Wallpaper wallpaper = get(newIndex);
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
        Wallpaper wallpaper = selected();
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

    private Wallpaper get(int index) {
        Node node = getChildren().get(index);

        if (node instanceof Wallpaper wallpaper) {
            return wallpaper;
        } else {
            throw new IllegalArgumentException("Nodes other than Wallpaper are not supported");
        }
    }

    private int count() {
        return getChildren().size();
    }
}
