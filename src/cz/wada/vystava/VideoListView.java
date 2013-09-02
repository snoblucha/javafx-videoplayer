/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.wada.vystava;

import java.io.File;
import java.io.FilenameFilter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.TilePane;

/**
 *
 * @author snb
 */
public class VideoListView extends TilePane {

    private ObservableList<File> items = javafx.collections.FXCollections.observableArrayList();
    private ObservableList<VideoView> views = javafx.collections.FXCollections.observableArrayList();
    private final double PADDING = 10.0;

    private final SelectionModel<VideoView> selectionModel;

    public SelectionModel<VideoView> getSelectionModel() {
        return selectionModel;
    }

    public VideoListView(File directory) {

        this.selectionModel = new SingleSelectionModel<VideoView>() {

            @Override
            protected VideoView getModelItem(int i) {
                return views.get(i);
            }

            @Override
            protected int getItemCount() {
                return views.size();
            }
        };
        setDirectory(directory);
        initSelectionModeListener();
        setDefaultKeyboardEvents();

        Insets padding = new Insets(PADDING);
        setPadding(padding);
        setHgap(PADDING);
        setVgap(PADDING);
        setFocusTraversable(true);
        getStyleClass().add("VideoListView");
    }

    private void initSelectionModeListener() {
        selectionModel.selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                String selectedClass = "selected";

                VideoView last = views.get(oldValue.intValue());
                if (last != null) {
                    last.getStyleClass().remove(selectedClass);
                }

                VideoView newItem = views.get(newValue.intValue());
                if (newItem != null) {
                    newItem.getStyleClass().add(selectedClass);
                }

            }
        });
    }

    public final void setDirectory(File directory) {
        getChildren().clear();
        loadVideoList(directory);
    }

    private void loadVideoList(File root) {

        File[] files = root.listFiles(new VideoFiles());
        VideoView.setMargin(this, new Insets(10));
        if (files != null) {
            items.addAll(files);

            for (File file : files) {
                VideoView view = new VideoView(file);

                views.add(view);
            }
            getChildren().addAll(views);
            selectionModel.select(0);
            if (selectionModel.getSelectedItem() != null) {
                selectionModel.getSelectedItem().getStyleClass().add("selected");
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
        addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent t) {
                if (t.getCode().equals(KeyCode.DOWN) || t.getCode().equals(KeyCode.RIGHT)) {
                    getSelectionModel().selectNext();
                }
                if (t.getCode().equals(KeyCode.UP) || t.getCode().equals(KeyCode.LEFT)) {
                   getSelectionModel().selectPrevious();
                }
            }
        });

    }

}
