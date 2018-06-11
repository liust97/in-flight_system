package main.view;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Locale;

public class WelcomeController {

    private MainApp mainApp;
    private Locale locale;
    @FXML
    private ToggleGroup Language;
    @FXML
    private RadioButton zhButton;
    @FXML
    private RadioButton enButton;
    @FXML
    private RadioButton jaButton;
    @FXML
    private Button startButton;

    /**
     * add listener to button, change the content of start button by language
     */
    @FXML
    private void initialize() {
        locale = Locale.ENGLISH;


        zhButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            startButton.setText("开始");
            locale = Locale.CHINESE;
        });

        enButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            startButton.setText("Start");
            locale = Locale.ENGLISH;
        });

        jaButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            startButton.setText("開始");
            locale = Locale.JAPANESE;
        });

    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleStart() {
        mainApp.showMovieOverview(locale);
    }

    /**
     * when user type enter, click start
     *
     * @param keyEvent KeyEvent
     */
    @FXML
    private void keyboard_Start(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            handleStart();
        }
    }
}
