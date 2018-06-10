package main.view;

import main.MainApp;
import main.model.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.util.ScrapingUtil;
import org.controlsfx.dialog.Dialogs;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * Dialog to edit details of a movie.
 *
 */
public class MovieEditDialogController {

    @FXML
    private TextField directorField;
    @FXML
    private TextField mainActorsField;
    @FXML
    private TextField topicField;
    @FXML
    private TextField languageField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField countryField;
    @FXML
    private TextField yearField;
    @FXML
    private Label fileNameLabel;
    @FXML
    private RadioButton ourSelectionRadio;
    @FXML
    private ChoiceBox genreChoiceBox;

    // Reference to the main application.
    private MainApp mainApp;

    private Stage dialogStage;
    private Movie movie;
    private boolean okClicked = false;
    private ObservableList genreList = FXCollections.observableArrayList(
            "/",
            new Separator(), "Action", "Adventure", "Comedy", "Crime", "Drama", "Horror", "Musicals", "Science Fiction", "War");

    /**
     *
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        genreChoiceBox.setItems(genreList);
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the movie to be edited in the dialog.
     *
     * @param movie
     */
    public void setMovie(Movie movie) {
        this.movie = movie;
        fileNameLabel.setText(movie.getFileName());
        nameField.setText(movie.getName());
        countryField.setText(movie.getCountry());
        yearField.setText(movie.getYear());
        directorField.setText(movie.getDirector());
        mainActorsField.setText(movie.getMainActors());
        topicField.setText(movie.getDescription());
        languageField.setText(movie.getLanguage());
        ourSelectionRadio.setSelected(movie.isIsOurSelection());
        genreChoiceBox.setValue(movie.getGenre());
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     * Save new information into the xml file
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            movie.setName(nameField.getText());
            movie.setCountry(countryField.getText());
            movie.setYear(yearField.getText());
            movie.setDirector(directorField.getText());
            movie.setMainActors(mainActorsField.getText());
            movie.setDescription(topicField.getText());
            movie.setLanguage(languageField.getText());
            movie.setIsOurSelection(ourSelectionRadio.isSelected());
            movie.setGenre(genreList.get(genreChoiceBox.getSelectionModel().selectedIndexProperty().getValue()).toString()); // 得到选择的种类
            okClicked = true;

            // save
            String path = "info";//所创建文件的路径
            File f = new File(path);
            if (!f.exists()) {
                if (!f.mkdirs()) {
                    System.out.println("fail to make dir");
                }//创建目录
            }
            String fileName = "MoviesInfo.xml";//文件名及类型
            File file = new File(path, fileName);
            if (!file.exists()) {
                try {
                    if (!file.createNewFile()) {
                        System.out.println("fail to save file");
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
//            File movieFile = mainApp.getMovieFilePath();
            // Make sure it has the correct extension
            mainApp.saveMovieDataToFile(file);

            dialogStage.close();
        } else {
            System.out.println("Input value is not valid");
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    @FXML
    private void handleAutoSet() throws IOException {
        Movie movie = ScrapingUtil.scrapMovieInfo(nameField.getText());
        directorField.setText(movie.getDirector());
        mainActorsField.setText(movie.getMainActors());
        topicField.setText(movie.getDescription());
        languageField.setText(movie.getLanguage());
    }

    /**
     * Called when the user clicks reset.
     * Reset information of movie to the origin state
     */
    @FXML
    private void handleReset() throws IOException {
        Movie movie = new Movie();
        String fileName = this.movie.getFileName();
        String fileData[] = fileName.split("_");
        fileData[2] = fileData[2].substring(0, fileData[2].lastIndexOf('.')); // delete suffix
        Locale l = new Locale("", fileData[1]);
        fileData[1] = l.getDisplayCountry();
        if (fileData.length == 3) {
            movie.setName(fileData[0]);
            movie.setCountry(fileData[1]);
            movie.setYear(fileData[2]);
            movie.setDuration(this.movie.getDuration());
        } else {
            movie.setName(fileData[0]);
            movie.setDuration(this.movie.getDuration());
        }
        nameField.setText(movie.getName());
        countryField.setText(movie.getCountry());
        yearField.setText(movie.getYear());
        directorField.setText(movie.getDirector());
        mainActorsField.setText(movie.getMainActors());
        topicField.setText(movie.getDescription());
        languageField.setText(movie.getLanguage());
        ourSelectionRadio.setSelected(false);
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (directorField.getText() == null || directorField.getText().length() == 0) {
            errorMessage += "No valid last name!\n";
        }
        if (mainActorsField.getText() == null || mainActorsField.getText().length() == 0) {
            errorMessage += "No valid street!\n";
        }

        if (topicField.getText() == null || topicField.getText().length() == 0) {
            errorMessage += "No valid postal code!\n";
        }

        if (languageField.getText() == null || languageField.getText().length() == 0) {
            errorMessage += "No valid language!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Not valid data");
            alert.setContentText("Make sure your data are valid\n");
            alert.showAndWait();
            return false;
        }
    }
}