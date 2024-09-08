package com.zms.image;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import net.coobird.thumbnailator.*;
import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.name.Rename;

public class ImageResizer extends Application {
    @Override
    public void start(Stage stage) {
        VBox vbox = new VBox();

        Scene scene = new Scene(vbox, 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("C:/Users/zayya/Pictures"));

        Button pickFolderButton = new Button("Pick folder");
        pickFolderButton.setOnMouseClicked(e -> {
            folder = directoryChooser.showDialog(stage);
        });

        Button resizeButton = new Button("Resize");
        resizeButton.setOnMouseClicked(e -> {
            try {
                resizeImage(folder);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        vbox.getChildren().addAll(pickFolderButton, resizeButton);
        vbox.setAlignment(Pos.CENTER);
    }

    private static File folder;
    public static void main(String[] args) {
        launch();
    }

    public static void resizeImage(File directory) throws IOException {
        Thumbnails.of(directory.listFiles())
                .size(1000, 300)
                .outputFormat("jpg")
                .toFiles(Rename.SUFFIX_DOT_THUMBNAIL);
    }
}