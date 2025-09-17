package lebron;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Lebron lebron;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image lebronImage = new Image(this.getClass().getResourceAsStream("/images/DaLebron.png"));

    /**
     * Initialises the scroll pane and dialog container.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        // Cursor assisted: Apply stylesheet if available and focus the input on start
        Scene scene = this.getScene();
        if (scene != null) {
            scene.getStylesheets().add(MainWindow.class.getResource("/view/style.css").toExternalForm());
        } else {
            this.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.getStylesheets().add(MainWindow.class.getResource("/view/style.css").toExternalForm());
                    userInput.requestFocus();
                }
            });
        }
        userInput.requestFocus();
    }

    /**
     * Injects the Lebron instance.
     *
     * @param l Lebron instance.
     */
    public void setLebron(Lebron l) {
        lebron = l;
        // Cursor assisted: welcome banner card on startup
        Label welcome = new Label("Hello! I'm Lebron\nWhat can I do for you?\n\nType 'list' to see commands.");
        welcome.getStyleClass().add("welcome-card");
        HBox container = new HBox(welcome);
        container.setPadding(new Insets(8, 8, 12, 8));
        dialogContainer.getChildren().add(container);
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Lebron's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = lebron.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getLebronDialog(response, lebronImage)
        );
        userInput.clear();
        if (input.equals("bye")) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
            PauseTransition delay = new PauseTransition(Duration.millis(1200));
            delay.setOnFinished(e -> javafx.application.Platform.exit());
            delay.play();
        }
    }
}
