package com.maks.themepicker.main;

import com.maks.themepicker.model.Wallpaper;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class Start extends Application {
    private int selectedIndex = 0;
    private final List<Wallpaper> wallpapers = new ArrayList<>();
    private final Duration animationDuration = Duration.millis(100);

    Screen screen = Screen.getPrimary();
    double screenWidth = screen.getBounds().getWidth();
    double screenHeight = screen.getBounds().getHeight();

    double imageHeight = screenHeight * 0.6;
    double imageWidth = imageHeight * (16.0 / 9.0);
    double croppedImageWidth = imageWidth * 0.2;
    double selectedImageWidth = imageWidth * 0.5;

    @Override
    public void start(Stage stage) throws Exception {
        Path currentWallpaperPath = Paths.get(System.getProperty("user.home"), ".config", "wallpapers", "current");
        ImageView currentWallpaper = new ImageView(new Image(currentWallpaperPath.toUri().toString(), 640, 360, false, true));
        currentWallpaper.setFitWidth(screenWidth);
        currentWallpaper.setFitHeight(screenHeight);
        ColorAdjust dim = new ColorAdjust();
        dim.setBrightness(-0.75);
        currentWallpaper.setEffect(dim);

        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(currentWallpaper);

        Scene scene = new Scene(root);
        scene.setFill(Color.rgb(30, 30, 30, 0.3));

        stage.setTitle("theme-picker");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.setX(0);
        stage.setY(0);
        stage.show();

        HBox hBox = new HBox();
        hBox.setPrefWidth(screenWidth);
        hBox.setPrefHeight(screenHeight);

        root.getChildren().add(hBox);

        hBox.setSpacing(10);
        hBox.setPrefWidth(screenWidth);
        hBox.setPrefHeight(screenHeight);
        hBox.setAlignment(Pos.CENTER);

        int numberOfThemes;
        try (Stream<Path> themeDirs = Files.list(Paths.get(System.getProperty("user.home"), ".config", "themes"))) {
            numberOfThemes = (int) themeDirs.count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BlockingQueue<Wallpaper> blockingQueue = new ArrayBlockingQueue<>(numberOfThemes);

        int processors = Runtime.getRuntime().availableProcessors();
        try (ExecutorService executorService = Executors.newFixedThreadPool(Math.min(processors, numberOfThemes))) {
            try (Stream<Path> themeDirs = Files.list(Paths.get(System.getProperty("user.home"), ".config", "themes"))) {
                themeDirs
                        .map(themeDir -> themeDir.resolve("wallpaper"))
                        .forEach(path -> executorService.submit(() -> loadWallpaper(path, blockingQueue)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Wallpaper wallpaper;
        int count = 0;
        while (count < numberOfThemes) {
            wallpaper = blockingQueue.take();
            hBox.getChildren().add(wallpaper.wallpaperContainer());
            wallpapers.add(wallpaper);
            count++;
        }
        instantSelectFirst();

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case RIGHT -> {
                    if (selectedIndex < (numberOfThemes - 1)) {
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
        });
    }

    private void loadWallpaper(Path path, BlockingQueue<Wallpaper> blockingQueue) {
        ImageView iv = new ImageView(new Image(path.toUri().toString(), 1280, 720, false, true, true));
        iv.setFitHeight(imageHeight);
        iv.setPreserveRatio(true);

        Rectangle clip = new Rectangle(croppedImageWidth, imageHeight);
        clip.setX((imageWidth - croppedImageWidth) / 2.0);
        iv.setClip(clip);

        ColorAdjust effect = new ColorAdjust();
        effect.setBrightness(-0.5);
        iv.setEffect(effect);

        StackPane wrapper = new StackPane(iv);
        wrapper.minWidthProperty().bind(clip.widthProperty());
        wrapper.prefWidthProperty().bind(clip.widthProperty());
        wrapper.maxWidthProperty().bind(clip.widthProperty());

        try {
            Wallpaper wallpaper = new Wallpaper(
                    wrapper,
                    clip,
                    effect
            );
            blockingQueue.put(wallpaper);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateSelection(int newIndex) {
        deselectOld();
        selectNew(newIndex);

        selectedIndex = newIndex;
    }

    private void instantSelectFirst() {
        selectNew(0, Duration.millis(1));
    }

    private void selectNew(int newIndex) {
        selectNew(newIndex, animationDuration);
    }

    private void selectNew(int newIndex, Duration animationDuration) {
        Rectangle newClip = wallpapers.get(newIndex).clip();
        ColorAdjust newEffect = wallpapers.get(newIndex).effect();

        Timeline newAnim = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(newClip.widthProperty(), newClip.getWidth()),
                        new KeyValue(newClip.xProperty(), newClip.getX()),
                        new KeyValue(newEffect.brightnessProperty(), newEffect.getBrightness())
                ),
                new KeyFrame(animationDuration,
                        new KeyValue(newClip.widthProperty(), selectedImageWidth),
                        new KeyValue(newClip.xProperty(), (imageWidth - selectedImageWidth) / 2.0),
                        new KeyValue(newEffect.brightnessProperty(), 0)
                )
        );
        newAnim.play();
    }

    private void deselectOld() {
        Rectangle oldClip = wallpapers.get(selectedIndex).clip();
        ColorAdjust oldEffect = wallpapers.get(selectedIndex).effect();

        Timeline oldAnim = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(oldClip.widthProperty(), oldClip.getWidth()),
                        new KeyValue(oldClip.xProperty(), oldClip.getX()),
                        new KeyValue(oldEffect.brightnessProperty(), oldEffect.getBrightness())
                ),
                new KeyFrame(animationDuration,
                        new KeyValue(oldClip.widthProperty(), croppedImageWidth),
                        new KeyValue(oldClip.xProperty(), (imageWidth - croppedImageWidth) / 2.0),
                        new KeyValue(oldEffect.brightnessProperty(), -0.5)
                )
        );
        oldAnim.play();
    }

    private void exit() {
        System.exit(0);
    }
}
