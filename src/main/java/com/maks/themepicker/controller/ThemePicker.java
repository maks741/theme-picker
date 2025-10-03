package com.maks.themepicker.controller;

import com.maks.themepicker.components.WallpaperBox;
import com.maks.themepicker.model.NullWallpaper;
import com.maks.themepicker.model.Wallpaper;
import com.maks.themepicker.service.WallpaperService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;

public class ThemePicker implements Initializable {

    @FXML
    private Pane root;

    @FXML
    private WallpaperBox wallpapers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root.sceneProperty().addListener((o, v, scene) ->
            scene.setOnKeyPressed(e -> {
                switch (e.getCode()) {
                    case RIGHT -> wallpapers.selectNext();
                    case LEFT -> wallpapers.selectPrevious();
                    case ENTER -> {
                        setTheme(wallpapers.selected().theme());
                        exit();
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

        wallpapers.postInit();
    }

    private void setTheme(String newTheme) {
        String command = "sleep 0.5 && set-theme " + newTheme;
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
        processBuilder.environment().putAll(System.getenv());

        try {
            processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void exit() {
        System.exit(0);
    }
}
