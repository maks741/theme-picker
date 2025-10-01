module com.maks.themepicker {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.maks.themepicker to javafx.fxml;
    exports com.maks.themepicker;
}