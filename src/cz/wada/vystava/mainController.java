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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.VideoTrack;

/**
 *
 * @author snb
 */
public class mainController implements Initializable {

    private class VideoFiles implements FilenameFilter {

        public boolean accept(File dir, String name) {

            return name.matches(".*\\.(avi|mpg|mp4|vob|mov)");
        }
    }

    @FXML
    private Label label;
    private MediaPlayer player;
    private VideoTrack track;

    @FXML ListView viewFiles;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        label.setText("Hello World!");
        viewFiles.setItems(getVideoList(new File("./video")));

        viewFiles.setOnKeyReleased(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent t) {
                SelectionModel sel = viewFiles.getSelectionModel();

                if(t.getCode().equals(KeyCode.ENTER)){
                    String selected = (String) sel.getSelectedItem();
                    File f = new File("video/"+selected);
                    System.out.println(f.toURI());
                    Media media = new Media(f.toURI().toString());

                    MediaPlayer player = new MediaPlayer(media);
                    player.play();

                } else if(t.getCode().equals(KeyCode.UP)){

                } else if(t.getCode().equals(KeyCode.DOWN)){

                } else if(t.getCode().equals(KeyCode.ESCAPE) ){

                }
            }
        });

    }

    private ObservableList<String> getVideoList(File root) {

        ObservableList<String> result = javafx.collections.FXCollections.observableArrayList();
        String[] files = root.list(new VideoFiles());
        result.addAll(files);

        return result;

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
