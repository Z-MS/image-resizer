module com.zms.image {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires imgscalr.lib;


    opens com.zms.image to javafx.fxml;
    exports com.zms.image;
}