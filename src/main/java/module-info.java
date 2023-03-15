module com.gogame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.gogame to javafx.fxml;
    exports com.gogame.view;
    opens com.gogame.view to javafx.fxml;
}