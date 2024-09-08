module com.zms.image {
    requires javafx.controls;
    requires javafx.fxml;
    requires net.coobird.thumbnailator;


    opens com.zms.image to javafx.fxml;
    exports com.zms.image;
}