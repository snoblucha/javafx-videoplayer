/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.wada.vystava;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
    private final ObjectProperty<Label> duration = new SimpleObjectProperty<>();

    private final static HashMap<File,Duration> durations = new HashMap<>();

    private MediaPlayer player;

    final int WIDTH = 320;
    final int HEIGHT = 200;
    final double PADDING = 0;

    public File getFile() {
        return file.get();
    }

    public final void setFile(File value) {
        file.set(value);
        title.get().setText("");
        duration.get().setText("--:--");
        this.init();
    }



    public Label getTitle() {
        return title.get();
    }

    public void setTitle(Label value) {
        title.set(value);
    }

    public ObjectProperty titleProperty() {
        return title;
    }

    public VideoView(File file) {

        getStyleClass().add("VideoView");
        initLayout();

        setFile(file);
    }

    private void initLayout() {
        Insets padding = new Insets(PADDING);
        setPadding(padding);

        setPrefWidth(WIDTH);
        setPrefHeight(HEIGHT);

        // preview
        ImageView iv = new ImageView();

        //prepare image
        iv.setFitWidth(WIDTH);
        iv.setFitHeight(HEIGHT);
        iv.setPreserveRatio(true);
        iv.getStyleClass().add("VideoViewImage");

        image.set(iv);
        getChildren().add(image.get());

        Label textLabel = new Label();
        textLabel.setWrapText(true);
        textLabel.getStyleClass().add("VideoViewTitle");
        title.set(textLabel);

        Label durationLabel = new Label();
        durationLabel.getStyleClass().add("VideoViewDuration");
        duration.set(durationLabel);

        this.getChildren().addAll(title.get(), duration.get());


    }

    private void init() {
        File imageFile = getImageFile();
        title.get().setText(file.get().getName().replaceAll("\\.mp4$", ""));
        if (!imageFile.exists()) {
            generateImage(this.getFile(), imageFile, 5);
        } else {
            Image imageToShow = new Image(imageFile.toURI().toString());
            image.get().setImage(imageToShow);
            setDurationFromMedia();
        }

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
        player = new MediaPlayer(media);
        final MediaView mView = new MediaView(player);

        mView.setFitWidth(WIDTH);
        mView.setFitHeight(HEIGHT);
        mView.setPreserveRatio(true);

        player.setOnPaused(new Runnable() {
            @Override
            public void run() {
                int width = (int) mView.getBoundsInLocal().getWidth();
                int height = (int) mView.getBoundsInLocal().getHeight();
                WritableImage writebleImage = new WritableImage(width, height);
                image.get().setImage(writebleImage);

                SnapshotParameters params = new SnapshotParameters();
                params.setFill(Color.BLACK);

                mView.snapshot(params, writebleImage);
                BufferedImage bImage = SwingFXUtils.fromFXImage(writebleImage, null);

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

    private void setDurationFromMedia() {

        Duration durationFromMap = durations.get(file.get());
        if(durationFromMap != null){
            setDuration(durationFromMap);
            return;
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final Media media = new Media(file.get().toURI().toString());
                final MediaPlayer player = new MediaPlayer(media);
                player.setAutoPlay(false);
                player.setOnReady(new Runnable() {
                    @Override
                    public void run() {
                        durations.put(file.get(), media.getDuration());
                        setDuration(media.getDuration());
                        player.stop();

                    }
                });
            }
        });

        thread.start();

    }

    private void setDuration(Duration mediaDuration) {
        String res;

        int hour = (int) Math.floor(mediaDuration.toHours());
        int min = ((int) Math.floor(mediaDuration.toMinutes())) % 60;
        int sec = ((int) Math.floor(mediaDuration.toSeconds())) % 60;
        res = (hour > 0 ? String.valueOf(hour) + ":" : "") + String.format("%02d:%02d", min, sec);

        duration.get().textProperty().set(res);

    }
}
