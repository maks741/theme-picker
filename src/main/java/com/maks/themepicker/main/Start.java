package com.maks.themepicker.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Start extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        long start = System.currentTimeMillis();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/themepicker/theme-picker.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        scene.setFill(Color.TRANSPARENT);

        stage.setTitle("theme-picker");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.setX(0);
        stage.setY(0);
        stage.show();

        System.out.println("STARTED IN: " + (System.currentTimeMillis() - start));
    }
}
