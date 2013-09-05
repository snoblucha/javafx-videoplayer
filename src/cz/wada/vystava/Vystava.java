/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.wada.vystava;

import java.io.File;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author snb
 */
public class Vystava extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        URL location = getClass().getResource("main.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

        Parent root = (Parent) fxmlLoader.load(location.openStream());

        Scene scene = new Scene(root);

        MainController controller = fxmlLoader.getController();

        stage.initStyle(StageStyle.UNDECORATED);

        stage.setScene(scene);
        stage.setResizable(false);

        stage.setX(0);
        stage.setY(0);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        controller.setStage(stage);

        File styleFile = new File("css/style.css");
        scene.getStylesheets().add(styleFile.toURI().toString());

        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());
        stage.setFullScreen(true);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application. main()
     * serves only as fallback in case the application can not be launched through
     * deployment artifacts, e.g., in IDEs with limited FX support. NetBeans ignores
     * main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
