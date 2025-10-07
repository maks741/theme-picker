package com.maks.themepicker.components;

import com.maks.themepicker.model.Wallpaper;
import com.maks.themepicker.utils.Config;
import com.maks.themepicker.utils.MathUtils;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.function.BiFunction;

public class WallpaperBox extends HBox {

    private int selectedIndex = 0;

    public WallpaperBox() {
        setPrefWidth(Config.screenWidth());
        setPrefHeight(Config.screenHeight());
        setSpacing(Config.spacing());
        setAlignment(Pos.CENTER_LEFT);
    }

    public void postInit() {
        instantSelectMiddleElement();

        double layoutX = (Config.screenWidth() / 2.0) - (currentWidth() / 2.0);
        boolean evenNumberOfWallpapers = count() % 2 == 0;
        if (evenNumberOfWallpapers) {
            layoutX += Config.unselectedClipWidth() / 2.0;
        }

        setLayoutX(layoutX);
    }

    public void add(Wallpaper wallpaper) {
        getChildren().add(wallpaper);
    }

    public void selectNext() {
        int wallpapersCount = count();
        if (selectedIndex < (wallpapersCount - 1)) {
            updateSelection(selectedIndex + 1, scrollTo(MathUtils::subtract));
        }
    }

    public void selectPrevious() {
        if (selectedIndex > 0) {
            updateSelection(selectedIndex - 1, scrollTo(MathUtils::add));
        }
    }

    public Wallpaper selected() {
        return get(selectedIndex);
    }

    private void instantSelectMiddleElement() {
        selectedIndex = Math.ceilDiv(count(), 2) - 1;

        Wallpaper wallpaper = get(selectedIndex);
        Rectangle newClip = wallpaper.clip();
        ColorAdjust newEffect = wallpaper.effect();
        wallpaper.setSelectedBorderColor();

        newClip.setWidth(Config.selectedClipWidth());
        newClip.setHeight(Config.selectedClipHeight());
        newClip.setX((Config.wallpaperWidth() - Config.selectedClipWidth()) / 2.0);
        newClip.setY((Config.wallpaperHeight() - Config.selectedClipHeight()) / 2.0);
        newEffect.setBrightness(0);
    }

    private void updateSelection(int newIndex, Timeline scroll) {
        Timeline deselectOld = deselectOld();
        Timeline selectNew = selectNew(newIndex);

        new ParallelTransition(deselectOld, selectNew, scroll).play();

        selectedIndex = newIndex;
    }

    private Timeline selectNew(int newIndex) {
        Wallpaper wallpaper = get(newIndex);
        Rectangle newClip = wallpaper.clip();
        ColorAdjust newEffect = wallpaper.effect();
        wallpaper.setSelectedBorderColor();

        return new Timeline(
                new KeyFrame(Config.animationDuration(),
                        new KeyValue(newClip.widthProperty(), Config.selectedClipWidth()),
                        new KeyValue(newClip.heightProperty(), Config.selectedClipHeight()),
                        new KeyValue(newClip.xProperty(), (Config.wallpaperWidth() - Config.selectedClipWidth()) / 2.0),
                        new KeyValue(newClip.yProperty(), (Config.wallpaperHeight() - Config.selectedClipHeight()) / 2.0),
                        new KeyValue(newEffect.brightnessProperty(), 0)
                )
        );
    }

    private Timeline deselectOld() {
        Wallpaper wallpaper = selected();
        Rectangle oldClip = wallpaper.clip();
        ColorAdjust oldEffect = wallpaper.effect();
        wallpaper.setDefaultBorderColor();

        return new Timeline(
                new KeyFrame(Config.animationDuration(),
                        new KeyValue(oldClip.widthProperty(), Config.unselectedClipWidth()),
                        new KeyValue(oldClip.heightProperty(), Config.unselectedClipHeight()),
                        new KeyValue(oldClip.xProperty(), (Config.wallpaperWidth() - Config.unselectedClipWidth()) / 2.0),
                        new KeyValue(oldClip.yProperty(), (Config.wallpaperHeight() - Config.unselectedClipHeight()) / 2.0),
                        new KeyValue(oldEffect.brightnessProperty(), -0.5)
                )
        );
    }

    private Timeline scrollTo(BiFunction<Double, Double, Double> function) {
        Duration duration = Config.animationDuration();
        double shift = Config.unselectedClipWidth() + getSpacing();
        double layoutX = getLayoutX();

        return  new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(layoutXProperty(), layoutX)
                ),
                new KeyFrame(duration,
                        new KeyValue(layoutXProperty(), function.apply(layoutX, shift))
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

    private double currentWidth() {
        int wallpapers = count();

        double unselectedWallpapersWidth = (wallpapers - 1) * Config.unselectedClipWidth();
        double allWallpapersWidth = unselectedWallpapersWidth + Config.selectedClipWidth();
        double totalSpacingWidth = (wallpapers - 1) * getSpacing();

        return allWallpapersWidth + totalSpacingWidth;
    }

    private int count() {
        return getChildren().size();
    }
}
