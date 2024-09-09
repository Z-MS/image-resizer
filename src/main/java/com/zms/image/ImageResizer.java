package com.zms.image;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

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

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:/Users/zayya/Pictures"));

        Button pickFolderButton = new Button("Pick folder");
        pickFolderButton.setOnMouseClicked(e -> {
            folder = directoryChooser.showDialog(stage);
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder.toPath())) {
                for(Path file: stream) {
                    // add file name to array
                    files.add(file.toFile());
                }
            } catch (IOException | DirectoryIteratorException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button resizeButton = new Button("Resize");
        resizeButton.setOnMouseClicked(e -> {
            try {
//                resizeImage(folder);
                // 3. Delete files without appendage in them 4.Rename files by removing appendage
                for(File file: files) {
                    BufferedImage resizedImage = simpleResizeImage(ImageIO.read(file), 1000);
                    saveImage(resizedImage, "png", file.getName() + "_resized");
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        vbox.getChildren().addAll(pickFolderButton, resizeButton);
        vbox.setAlignment(Pos.CENTER);
    }

    private static File folder;

    private static ArrayList<File> files = new ArrayList<>();
    public static void main(String[] args) {
        launch();
    }

    BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth) throws IOException {
        return Scalr.resize(originalImage, targetWidth);
    }

    void saveImage(BufferedImage image, String formatName, String filename) {
        try {
            File outputFile = new File( "C:/Users/zayya/Pictures/" + filename + "." + formatName);
            ImageIO.write(image, formatName, outputFile);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    public static void resizeImage(File directory) throws IOException {

    }

}