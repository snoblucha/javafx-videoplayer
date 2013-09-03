/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.wada.vystava;

import com.sun.javafx.css.StyleManager;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author snb
 */
public class MainController implements Initializable {

    private Stage stage;
    private MediaPlayer player;

    @FXML
    private MediaView mediaView;

    VideoListView videoList;

    @FXML
    AnchorPane rootPane;

    @FXML
    AnchorPane mediaPane;

    public void setStage(Stage stage) {
        this.stage = stage;

        final DoubleProperty width = mediaView.fitWidthProperty();
        final DoubleProperty height = mediaView.fitHeightProperty();

        width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
        mediaPane.setVisible(false);

        mediaView.setPreserveRatio(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        videoList = new VideoListView(new File("./video"));
        rootPane.getChildren().add(videoList);
        setListViewKeyboardHandle();
        videoList.requestFocus();
        videoList.setPrefHeight(rootPane.getHeight());
        videoList.setPrefWidth(rootPane.getWidth());

        videoList.prefWidthProperty().bind(rootPane.widthProperty());
        videoList.prefHeightProperty().bind(rootPane.heightProperty());

    }

    private void setListViewKeyboardHandle() {
        videoList.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                SelectionModel sel = videoList.getListView().get().getSelectionModel();

                if (t.getCode().equals(KeyCode.ENTER)) {

                    //if playing then can not assing another movie
                    if (player != null && player.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                        return;
                    }

                    VideoView selected = (VideoView) sel.getSelectedItem();
                    File f = selected.getFile();

                    Media media = new Media(f.toURI().toString());
                    player = new MediaPlayer(media);

                    mediaView.setMediaPlayer(player);
                    player.setOnError(new Runnable() {
                        @Override
                        public void run() {
                            String message = player.errorProperty().get().getMessage();
                            System.out.println(message);
                        }
                    });

                    player.setOnReady(new Runnable() {
                        @Override
                        public void run() {
                            
                            mediaPane.toFront();

                            double height = mediaView.getBoundsInLocal().getHeight();

                            mediaView.setY((mediaView.getFitHeight() - height) / 2);
                            mediaPane.setVisible(true);
                            mediaView.getScene().setFill(Color.BLACK);

                            player.play();

                        }
                    });

                    Runnable onFinished = new Runnable() {
                        @Override
                        public void run() {
                            mediaPane.setVisible(false);
                            mediaPane.toBack();
                        }
                    };

                    player.setOnStopped(onFinished);
                    player.setOnEndOfMedia(onFinished);

                } else if (t.getCode().equals(KeyCode.UP)) {
                    //sel.getSelectedIndex();
                } else if (t.getCode().equals(KeyCode.DOWN)) {
                } else if (t.getCode().equals(KeyCode.F) && t.isControlDown()) {
                    videoList.setDirectory(new File("./video"));
                } else if (t.getCode().equals(KeyCode.R) && t.isControlDown()) {
                    System.out.println("Reloading styles");
                    StyleManager.getInstance().reloadStylesheets(stage.getScene());
                } else if (t.getCode().equals(KeyCode.ESCAPE)) {
                    if (player != null && player.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                        player.stop();
                    }

                    t.consume();
                }
            }
        });
    }
}
