module com.maks.themepicker {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;

    exports com.maks.themepicker.main;
    opens com.maks.themepicker.main to javafx.fxml;
    exports com.maks.themepicker.controller;
    opens com.maks.themepicker.controller to javafx.fxml;
    exports com.maks.themepicker.components;
    opens com.maks.themepicker.components to javafx.fxml;
    exports com.maks.themepicker.model;
}