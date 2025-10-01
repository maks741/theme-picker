module com.maks.themepicker {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;


    opens com.maks.themepicker to javafx.fxml;
    exports com.maks.themepicker;
    exports com.maks.themepicker.main;
    opens com.maks.themepicker.main to javafx.fxml;
}