/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.wada.vystava;

import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 *
 * @author snb
 */
class VideoViewCellFactory implements Callback<ListView<VideoView>, ListCell<VideoView>> {


    @Override
    public ListCell<VideoView> call(ListView<VideoView> p) {
        ListCell<VideoView> view;
        view = new ListCell<VideoView>() {
            @Override
            protected void updateItem(VideoView t, boolean bln) {
                super.updateItem(t, bln);
                if (t != null) {
                    setText(t.getText());
                }
            }
        };

        return view;
    }

}
