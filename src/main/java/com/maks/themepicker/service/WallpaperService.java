package com.maks.themepicker.service;

import com.maks.themepicker.components.WallpaperImage;
import com.maks.themepicker.model.NullWallpaper;
import com.maks.themepicker.model.Wallpaper;
import com.maks.themepicker.utils.Config;
import com.maks.themepicker.utils.ResourceUtils;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class WallpaperService {

    public BlockingQueue<Wallpaper> loadWallpapers() throws InterruptedException {
        int numberOfThemes = countThemes();
        BlockingQueue<Wallpaper> blockingQueue = new ArrayBlockingQueue<>(numberOfThemes + 1); // + 1 for NullWallpaper at the end

        int processors = Runtime.getRuntime().availableProcessors();
        int initialNumberOfThreads = Math.min(processors, numberOfThemes);

        try (ExecutorService executorService = Executors.newFixedThreadPool(initialNumberOfThreads)) {
            try (Stream<Path> themeDirs = Files.list(ResourceUtils.themesDirPath())) {
                themeDirs.forEach(themeDir -> {
                    String themeName = themeDir.getFileName().toString();
                    Path wallpaperPath = themeDir.resolve("wallpaper");
                    executorService.submit(() -> loadWallpaper(wallpaperPath, themeName, blockingQueue));
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } finally {
            blockingQueue.put(new NullWallpaper());
        }

        return blockingQueue;
    }

    private void loadWallpaper(Path path, String themeName, BlockingQueue<Wallpaper> blockingQueue) {
        ImageView imageView = new ImageView(new WallpaperImage(path));
        imageView.setFitHeight(Config.wallpaperHeight());
        imageView.setFitWidth(Config.wallpaperWidth());

        Rectangle clip = new Rectangle(Config.unselectedClipWidth(), Config.unselectedClipHeight());
        clip.setArcWidth(Config.clipArc());
        clip.setArcHeight(Config.clipArc());
        clip.setX((Config.wallpaperWidth() - Config.unselectedClipWidth()) / 2.0);
        clip.setY((Config.wallpaperHeight() - Config.unselectedClipHeight()) / 2.0);
        imageView.setClip(clip);

        ColorAdjust effect = new ColorAdjust();
        effect.setBrightness(Config.unselectedWallpaperBrightness());
        imageView.setEffect(effect);

        Wallpaper wallpaper = new Wallpaper(imageView, themeName, clip, effect);
        wallpaper.minWidthProperty().bind(clip.widthProperty());
        wallpaper.maxWidthProperty().bind(clip.widthProperty());

        try {
            blockingQueue.put(wallpaper);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private int countThemes() {
        try (Stream<Path> themeDirs = Files.list(ResourceUtils.themesDirPath())) {
            return (int) themeDirs.count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
