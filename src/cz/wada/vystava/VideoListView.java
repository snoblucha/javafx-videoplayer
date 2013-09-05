/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.wada.vystava;

import java.io.File;
import java.io.FilenameFilter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author snb
 */
public class VideoListView extends AnchorPane {

    private final ObservableList<File> items = javafx.collections.FXCollections.observableArrayList();
    private final ObjectProperty<VideoView> videoView = new SimpleObjectProperty<>();
    private final ObjectProperty<ListView> listView = new SimpleObjectProperty<>();
    private final AnchorPane videoViewPane = new AnchorPane();
    private final double PADDING = 10.0;

    public VideoListView(File directory) {

        setDirectory(directory);

        setDefaultKeyboardEvents();

        Insets padding = new Insets(PADDING);
        setPadding(padding);

        setFocusTraversable(false);
        getStyleClass().add("VideoListView");

    }



    public final void setDirectory(File directory) {
        getChildren().clear();
        loadVideoList(directory);
    }

    private void loadVideoList(File root) {

        //add List for
        ListView list = new ListView();

        list.setEditable(false);

        list.getStyleClass().add("VideoListListView");
        list.setCellFactory(new VideoViewCellFactory());
        listView.set(list);
        getChildren().add(list);


        setTopAnchor(list, PADDING);
        setBottomAnchor(list, PADDING);
        setLeftAnchor(list, PADDING);

        setTopAnchor(videoViewPane, PADDING);
        setRightAnchor(videoViewPane, PADDING);
        setBottomAnchor(videoViewPane, PADDING);
        setBottomAnchor(videoViewPane, PADDING);

        getChildren().add(videoViewPane);
        videoViewPane.setId("VideoViewPane");
        SimpleDoubleProperty right = new SimpleDoubleProperty(0.45);
        list.prefWidthProperty().bind(right.multiply(widthProperty()));
        //videoViewPane.setPrefWidth(getWidth() * 0.5);
        File[] files = root.listFiles(new VideoFiles());

        if (files != null) {
            items.addAll(files);
            list.setItems(items);

            if(files.length > 0){

                VideoView view = new VideoView(items.get(0));
                videoView.set(view);
                videoViewPane.getChildren().add(videoView.get());
                listView.get().getSelectionModel().select(0);
                listView.get().setItems(items);
            }
        }
    }

    private class VideoFiles implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {

            return name.matches(".*\\.mp4");
        }
    }

    private void setDefaultKeyboardEvents() {

        getListView().get().addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode().equals(KeyCode.DOWN) || t.getCode().equals(KeyCode.UP)) {

                    File selected = (File) listView.get().getSelectionModel().getSelectedItem();

                    videoView.get().setFile(selected);
                    videoViewPane.getChildren().clear();
                    videoViewPane.getChildren().add(videoView.get());
                }
            }
        });

    }

    public ObjectProperty<ListView> getListView() {
        return listView;
    }

    public VideoView getVideoView() {
        return videoView.get();
    }

    public ObjectProperty videoViewProperty() {
        return videoView;
    }
}
