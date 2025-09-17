package lebron;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Class that starts the GUI
 */
public class Main extends Application {
    private static final String DEFAULT_SAVE_PATH = "./LebronData/Lebron.txt";
    private final Lebron lebron = new Lebron(DEFAULT_SAVE_PATH);

    /**
     * Sets up stage and other GUI elements.
     *
     * @param stage stage for GUI.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setLebron(lebron); // inject the Lebron instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
