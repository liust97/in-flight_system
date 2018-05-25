package main;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.prefs.Preferences;

import main.model.Movie;
import main.model.MovieListWrapper;
import main.view.MediaviewController;
import main.view.MovieEditDialogController;
import main.view.MovieOverviewController;
import main.view.WelcomeController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import static main.util.videoDurationUtil.ReadVideoDuration;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private final String moviesPath = "src/movies";
    private final String infoPath = "info/MoviesInfo.xml";


    private String movieURL;
    private ResourceBundle resourceBundle;
    /**
     * The data as an observable list of Persons.
     */
    private ObservableList<Movie> movieData = FXCollections.observableArrayList();

    /**
     * Constructor
     */
    public MainApp() {
        // Add some sample data
//        personData.add(new Person("Hans", "Muster"));
    }

    public void initMovieData() throws Exception {
        File movies_directory = new File(moviesPath);
        if (!movies_directory.isDirectory()) {
            throw new Exception("movies_directory is not a directory");
        }

        File[] movie_list = movies_directory.listFiles();
        if (movie_list == null) throw new Exception("movie_list is null");


        File movieFile = new File(infoPath);
        if (movieFile.exists()) {
            loadMovieDataFromFile(movieFile);
        }
        ArrayList<String> fileNamesInData = new ArrayList();
        ArrayList<String> fileNamesIndir = new ArrayList();

        for (Movie movie : movieData) {
            fileNamesInData.add(movie.getFileName());
        }

        for (File moviefile : movie_list) {
            String fileName = moviefile.getName();
            fileNamesIndir.add(fileName);
            if (!moviefile.isDirectory() && !fileNamesInData.contains(fileName)) {// 如果moviedata里面已经存在这个文件，则不再读取了
                String fileData[] = fileName.split("_");
                try {
                    fileData[2] = fileData[2].substring(0, fileData[2].lastIndexOf('.')); // delete suffix
                } catch (Exception ignored) {
                }

                String duration;
                try { // 读取视频长度顺便检查类型
                    duration = ReadVideoDuration(moviefile);
                } catch (Exception e) {
                    continue;
                }

                if (fileData.length == 3) {

                    movieData.add(new Movie(fileName, fileData[0], fileData[1], fileData[2], duration));
                } else {
                    movieData.add(new Movie(fileName, fileName, duration));
                }
            }
        }

        movieData.removeIf(movie -> !fileNamesIndir.contains(movie.getFileName()));
        saveMovieDataToFile(movieFile);
    }

    /**
     * Returns the data as an observable list of movies.
     *
     * @return
     */
    public ObservableList<Movie> getMovieData() {
        return movieData;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");

        initRootLayout();
//        showMovieOverview();
        showWelcome();
    }

    public void showWelcome() {
        try {
            // Load movie overview.
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(MainApp.class.getResource("view/Welcome.fxml"));
            AnchorPane welcome = (AnchorPane) loader.load();

            // Set movie overview into the center of root layout.
            rootLayout.setCenter(welcome);

            // Give the controller access to the main app.
            WelcomeController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {

//        resourceBundle = mainApp.getResourceBundle();
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the movie overview inside the root layout.
     */
    public void showMovieOverview(Locale locale) {

        try {
            initMovieData();
            // Load movie overview.
            FXMLLoader loader = new FXMLLoader();

            String basename = "properties.config";
            loader.setResources(ResourceBundle.getBundle(basename, locale));

            loader.setLocation(MainApp.class.getResource("view/MovieOverview.fxml"));
            AnchorPane movieOverview = (AnchorPane) loader.load();

            // Set movie overview into the center of root layout.
            rootLayout.setCenter(movieOverview);

            // Give the controller access to the main app.
            MovieOverviewController controller = loader.getController();
            controller.setMainApp(this);
            controller.setResources(ResourceBundle.getBundle(basename, locale));
            this.resourceBundle = ResourceBundle.getBundle(basename, locale);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showMediaView(String movieURL) {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Mediaview.fxml"));
            AnchorPane mediaView = (AnchorPane) loader.load();
            this.movieURL = "movies/" + movieURL; //该路径为相对src的路径
//            primaryStage.setTitle("movie");
            rootLayout.setCenter(mediaView);
            MediaviewController controller = loader.getController();
            controller.setMainApp(this);
//            primaryStage.setFullScreen(true);
//全屏语句
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog to edit details for the specified person. If the user
     * clicks OK, the changes are saved into the provided person object and true
     * is returned.
     *
     * @param movie the movie object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showMovieEditDialog(Movie movie, Locale locale) {
        try {
            // Load the fxml file and create a new stage for the popup movie.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/MovieEditDialog.fxml"));

            String basename = "properties.config";
            loader.setResources(ResourceBundle.getBundle(basename, locale));

            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Movie");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the Movie into the controller.
            MovieEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMovie(movie);
            controller.setMainApp(this);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Loads movie data from the specified file. The current movie data will
     * be replaced.
     *
     * @param file
     */
    public void loadMovieDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(MovieListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            MovieListWrapper wrapper = (MovieListWrapper) um.unmarshal(file);

            movieData.clear();
            for (Movie movie : movieData) {
                movie.getFileName();
            }
            movieData.addAll(wrapper.getMovies());

            // Save the file path to the registry.
            saveMovieDataToFile(file);

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Saves the current person data to the specified file.
     *
     * @param file
     */
    public void saveMovieDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(MovieListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our person data.
            MovieListWrapper wrapper = new MovieListWrapper();
            wrapper.setMovies(movieData);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);

            // Save the file path to the registry.
            setMovieFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Dialogs.create().title("Error")
                    .masthead("Could not save data to file:\n" + file.getPath())
                    .showException(e);
        }
    }

    /**
     * Returns the person file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     *
     * @return
     */
    public File getMovieFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     *
     * @param file the file or null to remove the path
     */
    public void setMovieFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
//            primaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Update the stage title.
//            primaryStage.setTitle("AddressApp");
        }
    }

    public String getMovieURL() {
        return movieURL;
    }

    public void setMovieURL(String movieURL) {
        this.movieURL = movieURL;
    }

    /**
     * Returns the main stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
}