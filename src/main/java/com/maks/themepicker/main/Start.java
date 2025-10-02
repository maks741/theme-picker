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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/themepicker/theme-picker.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        scene.setFill(Color.rgb(30, 30, 30, 0.3));

        stage.setTitle("theme-picker");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.setX(0);
        stage.setY(0);
        stage.show();
    }
}
