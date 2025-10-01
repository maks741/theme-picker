package com.maks.themepicker.main;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Start extends Application {
    private int selectedIndex = 0;
    private final List<ImageView> imageViews = new ArrayList<>();
    private final List<Rectangle> clips = new ArrayList<>();
    private final List<ColorAdjust> effects = new ArrayList<>();
    private boolean init = true;

    Screen screen = Screen.getPrimary();
    double screenWidth = screen.getBounds().getWidth();
    double screenHeight = screen.getBounds().getHeight();

    double imageHeight = screenHeight * 0.6;
    double imageWidth = imageHeight * (16.0 / 9.0);
    double croppedImageWidth = imageWidth * 0.2;
    double selectedImageWidth = imageWidth * 0.5;

    @Override
    public void start(Stage stage) {

        HBox box = new HBox();

        Scene scene = new Scene(box);
        scene.setFill(Color.rgb(30, 30, 30, 0.3));

        box.setSpacing(10);
        box.setPrefWidth(screenWidth);
        box.setPrefHeight(screenHeight);
        box.setAlignment(Pos.CENTER);

        Path[] paths = {
                Paths.get(System.getProperty("user.home"), ".config", "themes", "anime_girl", "wallpaper"),
                Paths.get(System.getProperty("user.home"), ".config", "themes", "katana", "wallpaper"),
                Paths.get(System.getProperty("user.home"), ".config", "themes", "ruins", "wallpaper"),
                Paths.get(System.getProperty("user.home"), ".config", "themes", "hyprland_kath", "wallpaper"),
                Paths.get(System.getProperty("user.home"), ".config", "themes", "mountain", "wallpaper"),
        };
        /*Path[] paths = {
                Paths.get(System.getProperty("user.home"), ".config", "themes", "anime_girl", "wallpaper"),
                Paths.get(System.getProperty("user.home"), ".config", "themes", "anime_girl", "wallpaper"),
                Paths.get(System.getProperty("user.home"), ".config", "themes", "anime_girl", "wallpaper"),
                Paths.get(System.getProperty("user.home"), ".config", "themes", "anime_girl", "wallpaper"),
        };*/

        for (Path path : paths) {
            ImageView iv = new ImageView(new Image(path.toUri().toString()));
            iv.setFitHeight(imageHeight);

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

            imageViews.add(iv);
            clips.add(clip);
            effects.add(effect);

            box.getChildren().add(wrapper);
        }

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.RIGHT) {
                if (selectedIndex < imageViews.size() - 1) {
                    updateSelection(selectedIndex + 1);
                }
            } else if (e.getCode() == KeyCode.LEFT) {
                if (selectedIndex > 0) {
                    updateSelection(selectedIndex - 1);
                }
            }
        });

        updateSelection(0);

        stage.setTitle("theme-picker");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setX(0);
        stage.setY(0);
        stage.setScene(scene);
        stage.show();
    }

    private void updateSelection(int newIndex) {
        if (newIndex == selectedIndex && !init) {
            return;
        }
        init = false;

        // animate deselection of old
        Rectangle oldClip = clips.get(selectedIndex);
        ColorAdjust oldEffect = effects.get(selectedIndex);

        Timeline oldAnim = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(oldClip.widthProperty(), oldClip.getWidth()),
                        new KeyValue(oldClip.xProperty(), oldClip.getX()),
                        new KeyValue(oldEffect.brightnessProperty(), oldEffect.getBrightness())
                ),
                new KeyFrame(Duration.seconds(0.1),
                        new KeyValue(oldClip.widthProperty(), croppedImageWidth),
                        new KeyValue(oldClip.xProperty(), (imageWidth - croppedImageWidth) / 2.0),
                        new KeyValue(oldEffect.brightnessProperty(), -0.5)
                )
        );
        oldAnim.play();

        // animate selection of new
        Rectangle newClip = clips.get(newIndex);
        ColorAdjust newEffect = effects.get(newIndex);

        Timeline newAnim = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(newClip.widthProperty(), newClip.getWidth()),
                        new KeyValue(newClip.xProperty(), newClip.getX()),
                        new KeyValue(newEffect.brightnessProperty(), newEffect.getBrightness())
                ),
                new KeyFrame(Duration.seconds(0.1),
                        new KeyValue(newClip.widthProperty(), selectedImageWidth),
                        new KeyValue(newClip.xProperty(), (imageWidth - selectedImageWidth) / 2.0),
                        new KeyValue(newEffect.brightnessProperty(), 0)
                )
        );
        newAnim.play();

        selectedIndex = newIndex;
    }
}
