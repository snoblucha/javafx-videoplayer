/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.wada.vystava;

import java.io.File;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 *
 * @author snb
 */
class VideoViewCellFactory implements Callback<ListView<File>, ListCell<File>> {


    @Override
    public ListCell<File> call(ListView<File> p) {
        ListCell<File> view;
        view = new ListCell<File>() {
            @Override
            protected void updateItem(File t, boolean bln) {
                super.updateItem(t, bln);
                if (t != null) {
                    String text = t.getName().replaceAll("\\.mp4", "");
                    setText(text);
                }
            }
        };

        return view;
    }

}
