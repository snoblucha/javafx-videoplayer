/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.wada.vystava;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javax.imageio.ImageIO;

/**
 *
 * @author snb
 */
public class VideoView extends VBox {

    private final ObjectProperty<ImageView> image = new SimpleObjectProperty<>();
    private final ObjectProperty<File> file = new SimpleObjectProperty<>();
    private final ObjectProperty<Label> title = new SimpleObjectProperty<>();

    final int WIDTH = 160;
    final int HEIGHT = 120;
    final double PADDING = 0;

    public File getFile() {
        return file.get();
    }

    public final void setFile(File value) {
        file.set(value);
        this.init();
    }

    public ImageView getImage() {
        return image.get();
    }

    public void setImage(ImageView value) {
        image.set(value);
    }

    public ObjectProperty imageProperty() {
        return image;
    }

    public Label getTitle() {
        return title.get();
    }

    public void setTitle(Label value) {
        title.set(value);
    }

    public ObjectProperty stringProperty() {
        return title;
    }

    public VideoView(File file) {
        this.setFile(file);
        getStyleClass().add("VideoView");

    }

    private void init() {

        Insets padding = new Insets(PADDING);
        setPadding(padding);
        

        setPrefWidth(200);
        setPrefHeight(200);

        File imageFile = getImageFile();
        ImageView iv = new ImageView();
        //prepare image
        iv.setFitWidth(WIDTH);
        iv.setFitHeight(HEIGHT);
        iv.setPreserveRatio(true);

        iv.getStyleClass().add("VideoViewimage");

        imageProperty().set(iv);
        getChildren().add(getImage());

        if (!imageFile.exists()) {
            this.generateImage(this.getFile(), imageFile, 30);
        } else {
            Image imageToShow = new Image(imageFile.toURI().toString());
            iv.setImage(imageToShow);

        }

        Label label = new Label();
        label.setWrapText(true);
        label.getStyleClass().add("VideoViewTitle");
        this.setTitle(label);
        label.setText(file.get().getName().replaceAll("\\.(mp4|avi)$", ""));

        this.getChildren().add(title.get());

    }

    private File getImageFile() {
        String imageFilename = file.get().toString().replaceAll("\\.mp4$", ".png");
        return new File(imageFilename);
    }

    public void regenerateImage(int percent) {
        generateImage(file.get(), getImageFile(), percent);
    }

    /**
     * Generate image thumbnail
     *
     * @param videoFile
     * @param imageFile
     * @param percent Position of thumbnail in length percent
     */
    private void generateImage(final File videoFile, final File imageFile, final int percent) {

        final Media media = new Media(videoFile.toURI().toString());
        final MediaPlayer player = new MediaPlayer(media);
        final MediaView mView = new MediaView(player);

        mView.setFitWidth(WIDTH);
        mView.setFitHeight(HEIGHT);
        mView.setPreserveRatio(true);

        player.setOnPaused(new Runnable() {

            @Override
            public void run() {
                int width = (int) mView.getBoundsInLocal().getWidth();
                int height = (int) mView.getBoundsInLocal().getHeight();
                WritableImage image = new WritableImage(width, height);
                getImage().setImage(image);
                SnapshotParameters params = new SnapshotParameters();
                params.setFill(Color.BLACK);

                mView.snapshot(params, image);
                BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
                try {
                    ImageIO.write(bImage, "png", imageFile);
                } catch (IOException e) {
                    Logger.getLogger(VideoView.class.getName()).log(Level.WARNING, "Image can not be written: {0}", e.getMessage());
                } finally {
                    player.stop();
                }
            }
        });

        player.setOnPlaying(new Runnable() {
            @Override
            public void run() {
                player.pause();
            }
        });

        player.setOnReady(new Runnable() {
            @Override
            public void run() {
                player.seek(Duration.millis(player.getTotalDuration().toMillis() * percent / 100));
                player.play();
            }
        });
    }

}
