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
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ImageResizer extends Application {
    @Override
    public void start(Stage stage) {
        VBox vbox = new VBox();

        Scene scene = new Scene(vbox, 320, 240);
        stage.setTitle("Image Resizer");
        stage.setScene(scene);
        stage.show();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("C:/Users/zayya/Pictures/AI/Test/"));

        Button pickFolderButton = new Button("Pick folder");
        Button resizeButton = new Button("Resize");
        Button renameButton = new Button("Rename");

        pickFolderButton.setOnMouseClicked(e -> {
            folder = directoryChooser.showDialog(stage);

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder.toPath())) {
                for(Path file: stream) {
                    // check if image's width is > 1000. If so, add to files array
                    File imageFile = file.toFile();
                    if(getWidth(imageFile) > 1000) {
                        // add file to array
                        files.add(imageFile);
                    }
                }
            } catch (IOException | DirectoryIteratorException ex) {
                throw new RuntimeException(ex);
            }
        });

        resizeButton.setOnMouseClicked(e -> {
            try {
                for(File file: files) {
                    // resize image
                    BufferedImage resizedImage = simpleResizeImage(ImageIO.read(file), 1000);
                    String fullFilename = file.getName();

                    // delete original file
                    if(deleteOriginalImage(file)) {
                        System.out.println("File deleted");
                    }

                    String imageFilename = fullFilename.substring(0, fullFilename.indexOf('.'));
                    System.out.println(folder.getPath() + "/" + imageFilename + ".png");
                    saveImage(resizedImage, imageFilename);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        /*renameButton.setOnMouseClicked(e -> {
            try(DirectoryStream<Path> stream = Files.newDirectoryStream(folder.toPath())) {
                // get the images in folder and rename (remove "resized))
                for(Path path: stream) {
                    File imageFile = path.toFile();
                    String fileName = imageFile.getName();
                    if(fileName.contains("resized_")) {
                        // remove "resized_" from file name
                        File outputDir = new File(directoryChooser.getInitialDirectory().getName() + "/" + fileName.substring(fileName.indexOf("_") + 1));
                        System.out.println(outputDir);
                        // imageFile.renameTo();
                    }
                }
            } catch (IOException | DirectoryIteratorException ex) {
                throw new RuntimeException(ex);
            }
        });*/

        vbox.getChildren().addAll(pickFolderButton, resizeButton, renameButton);
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

    void saveImage(BufferedImage image, String filename) {
        try {
            // get selected folder
            String outputDir = folder.getPath() + "\\";
            File outputFile = new File(outputDir + filename + ".png");
            ImageIO.write(image, "png", outputFile);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    static int getWidth(final File image) throws IOException {
        // gotten from StackOverflow and refined by Claude
        int width;
        try (ImageInputStream stream = new FileImageInputStream(image)) {
            ImageReader reader = ImageIO.getImageReadersByFormatName("png").next();
            try {
                reader.setInput(stream);
                width = reader.getWidth(0);
            } finally {
                reader.dispose();
            }
        } catch (IOException ex) {
            throw new IOException("Error reading image dimensions: " + ex.getMessage());
        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException("No PNG reader found");
        }
        return width;
    }

    static boolean deleteOriginalImage(File file) throws IOException {
        return file.delete();
    }

}