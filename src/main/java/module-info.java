module com.example.gogame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gogame to javafx.fxml;
    exports com.example.gogame;
}