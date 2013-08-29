/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.wada.vystava;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
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

    private class VideoFiles implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {

            return name.matches(".*\\.(avi|mpg|mp4|vob|mov)");
        }
    }
    private Stage stage;
    private MediaPlayer player;


    @FXML
    private MediaView mediaView;

    @FXML
    ListView viewFiles;

    @FXML
    AnchorPane mediaPane;

    @FXML
    private void handleButtonAction(ActionEvent event) {
    }

    public void setStage(Stage stage) {
        this.stage = stage;

        final DoubleProperty width = mediaView.fitWidthProperty();
        final DoubleProperty height = mediaView.fitHeightProperty();

        width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
        mediaPane.setVisible(false);

        mediaView.setPreserveRatio(true);



    }

    private ObservableList<String> getVideoList(File root) {

        ObservableList<String> result = javafx.collections.FXCollections.observableArrayList();
        String[] files = root.list(new VideoFiles());
        if (files != null) {
            result.addAll(files);
        }

        return result;

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewFiles.setItems(getVideoList(new File("./video")));
        setListViewKeyboardHandle();
        viewFiles.requestFocus();
    }

    private void setListViewKeyboardHandle() {
        viewFiles.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                SelectionModel sel = viewFiles.getSelectionModel();

                if (t.getCode().equals(KeyCode.ENTER)) {

                    //if playing then can not assing another movie
                    if( player != null && player.getStatus().equals(MediaPlayer.Status.PLAYING)){
                        return;
                    }

                    String selected = (String) sel.getSelectedItem();
                    File f = new File("video/" + selected);
                    System.out.println(f.toURI());
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
                            player.volumeProperty().set(0);
                            mediaPane.toFront();

                            double height = mediaView.getBoundsInLocal().getHeight();

                            mediaView.setY((mediaView.getFitHeight() - height)/2);
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
                } else if (t.getCode().equals(KeyCode.ESCAPE)) {
                    if( player!= null && player.getStatus().equals(MediaPlayer.Status.PLAYING)){
                        player.stop();
                    }

                    t.consume();
                }
            }
        });
    }
}
