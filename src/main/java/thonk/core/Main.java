package thonk.core;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import thonk.MainWindow;

/**
 * Main class for JavaFX
 */
public class Main extends Application {

    private final Thonk thonk = new Thonk();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            stage.setTitle("Thonk");
            fxmlLoader.<MainWindow>getController().setThonk(thonk); //inject the Duke instance
            stage.show();
            Platform.runLater(() -> {
                stage.setAlwaysOnTop(true);
                stage.toFront();
                stage.requestFocus();
                stage.setAlwaysOnTop(false);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
