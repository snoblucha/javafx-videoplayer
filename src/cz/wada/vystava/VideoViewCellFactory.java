/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.wada.vystava;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

/**
 *
 * @author snb
 */
class VideoViewCellFactory implements Callback<ListView<VideoView>, ListCell<VideoView>> {

    private final double PADDING = 10.0;

    @Override
    public ListCell<VideoView> call(ListView<VideoView> p) {
        ListCell<VideoView> view;
        view = new ListCell<VideoView>() {
            @Override
            protected void updateItem(VideoView t, boolean bln) {
                super.updateItem(t, bln);
                if (t != null) {
                    AnchorPane pane = new AnchorPane();

                    Label title = new Label(t.getText());
                    Label duration = new Label(t.getDurationText());

                    pane.getChildren().addAll(title, duration);
                    AnchorPane.setBottomAnchor(title, PADDING);
                    AnchorPane.setTopAnchor(title, PADDING);
                    AnchorPane.setLeftAnchor(title, PADDING);

                    AnchorPane.setBottomAnchor(duration, PADDING);
                    AnchorPane.setTopAnchor(duration, PADDING);
                    AnchorPane.setRightAnchor(duration, PADDING);

                    getChildren().clear();
                    getChildren().add(pane);

                    //setText(t.getText());
                }
            }
        };

        return view;
    }

}
