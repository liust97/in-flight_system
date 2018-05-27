package main.view;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import main.MainApp;
import javafx.beans.value.ObservableValue;
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
    private RadioButton frButton;
    @FXML
    private Button startButton;


    @FXML
    private void initialize() {
        locale = Locale.ENGLISH;



        zhButton.selectedProperty().addListener(new javafx.beans.value.ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                startButton.setText("开始");
                locale = Locale.CHINESE;
            }
        });

        enButton.selectedProperty().addListener(new javafx.beans.value.ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                startButton.setText("Start");
                locale = Locale.ENGLISH;
            }
        });

        frButton.selectedProperty().addListener(new javafx.beans.value.ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                startButton.setText("Commencer");
                locale = Locale.FRENCH;
            }
        });
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleStart() {
        Toggle selectedToggle = Language.getSelectedToggle();
//        System.out.println(selectedToggle);
        if (selectedToggle == null) {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Language Selected");
            alert.setContentText("Please select a language.");
            alert.showAndWait();
        }

        mainApp.showMovieOverview(locale);
    }
}
