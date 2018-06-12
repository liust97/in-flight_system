package main.view;

import com.melloware.jintellitype.JIntellitype;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.MainApp;
import main.model.Category;
import main.model.Movie;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.util.ScrapingUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MovieOverviewController {
    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TableColumn<Movie, String> movieNameColumn;
    @FXML
    private TableView<String> categoriesTable;
    @FXML
    private TableColumn<String, String> categoriesColumn;

    @FXML
    private Label directorLabel;
    @FXML
    private Label mainActorsLabel;
    @FXML
    private Label yearLabel;
    @FXML
    private Label countryLabel;
    @FXML
    private Label durationLabel;
    @FXML
    private TextArea descriptionLabel;
    @FXML
    private Label languageLabel;
    @FXML
    private Label movieNameLabel;
    @FXML
    private Label genreLabel;
    @FXML
    private ToggleGroup categoryGroup;
    @FXML
    private RadioButton allRadio;
    @FXML
    private RadioButton genreRadio;
    @FXML
    private RadioButton countryRadio;
    @FXML
    private RadioButton ageRadio;
    @FXML
    private RadioButton ourSelectionRadio;
    @FXML
    private Button Auto_set_all;
    @FXML
    private Button Edit;
    private ResourceBundle resourceBundle;
    private static final int Super_Administrator_1 = 1;

    // Reference to the main application.
    private MainApp mainApp;


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public MovieOverviewController() {
    }

    /**
     * add data to movie table
     * add listener to the toggles
     */
    @FXML
    private void initialize() {

        allRadio.setUserData("all");
        genreRadio.setUserData("genre");
        countryRadio.setUserData("country");
        ageRadio.setUserData("age");
        ourSelectionRadio.setUserData("ourSelection");

        // 显示电影列表
        movieNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
//        categoriesColumn.setCellValueFactory();
//        movieNameColumn
        // Clear movie details.
        showMovieDetails(null);

        descriptionLabel.setFocusTraversable(false);

        set_admin_visible();
        // 超级用户监听
        set_super_administrator();


        // Listen for selection changes and show the movie details when changed.
        movieTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showMovieDetails(newValue));

        categoryGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue.getUserData().toString()) {
                case "all":
                    handleAllCategory();
                    break;
                case "age":
                    handleAgeCategory();
                    break;
                case "country":
                    handleCountryCategory();
                    break;
                case "ourSelection":
                    handleOurSelectionCategory();
                    break;
                case "genre":
                    handleGenreCategory();
                    break;
            }
        });

        allRadio.addEventFilter(KeyEvent.KEY_PRESSED, this::change_table_for_radio);
        genreRadio.addEventFilter(KeyEvent.KEY_PRESSED, this::change_table_for_radio);
        countryRadio.addEventFilter(KeyEvent.KEY_PRESSED, this::change_table_for_radio);
        ageRadio.addEventFilter(KeyEvent.KEY_PRESSED, this::change_table_for_radio);
        ourSelectionRadio.addEventFilter(KeyEvent.KEY_PRESSED, this::change_table_for_radio);

    }

    /**
     * The private method is used for the action of keyboard.
     *
     * @param event The event of keyboard.
     */
    @FXML
    private void change_table_for_radio(KeyEvent event) {
        KeyCode code = event.getCode();
        keyboard_action_judge(event, code);
        if (code == KeyCode.LEFT) {
            event.consume();
        }
    }

    /**
     * The keyboard action to change next table.
     * <p>
     * <ul>
     * <li>When pressing the ENTER, the focus will change to next table.</li>
     * <li>When pressing the RIGHT, the focus will change to next table.</li>
     * <li>When pressing the LEFT, the focus will change to last table.</li>
     * </ul>
     * </p>
     *
     * @param event The event of keyboard.
     */
    @FXML
    private void change_table(KeyEvent event) {
        KeyCode code = event.getCode();
        keyboard_action_judge(event, code);
        if (code == KeyCode.LEFT) {
            switchLeft();
        }
        if (code == KeyCode.RIGHT) {
            event.consume();
        }
    }

    private void switchLeft() {
        try {
            Robot r = new Robot();
            r.keyPress(java.awt.event.KeyEvent.VK_SHIFT);
            r.keyPress(java.awt.event.KeyEvent.VK_TAB);
            r.keyRelease(java.awt.event.KeyEvent.VK_SHIFT);
            r.keyRelease(java.awt.event.KeyEvent.VK_TAB);
        } catch (Exception ignored) {
            System.out.println("fail to switchLeft");
        }
    }

    private void keyboard_action_judge(KeyEvent event, KeyCode code) {
        if (code == KeyCode.ENTER || code == KeyCode.RIGHT) {
            try {
                Robot r = new Robot();
                r.keyPress(java.awt.event.KeyEvent.VK_TAB);
                r.keyRelease(java.awt.event.KeyEvent.VK_TAB);
            } catch (Exception ignored) {
            }
            event.consume();
        }
    }


    /**
     * Using the JIntellitype jar to make a hot key.
     * <p>
     * When the administrator press ctrl+alt+E, the System will enter the model of Administrator.
     * </p>
     */
    private void set_super_administrator() {
        // 管理员 超级按钮监听
        // 全局键盘监听，根据输入超级指令选择对应的语言文件
        JIntellitype.getInstance().registerHotKey(Super_Administrator_1, JIntellitype.MOD_CONTROL + JIntellitype.MOD_ALT, (int) 'E');
        JIntellitype.getInstance().addHotKeyListener(i -> {
            if (i == Super_Administrator_1) {
                if (Auto_set_all.isVisible()) {
                    System.out.println("Exit Administrator");
                    set_admin_visible();
                } else {
                    System.out.println("Welcome！Super Administrator");
                    set_visible();
                }
            }
        });

    }

    /**
     * The private method is that hide the button which is only used by the administrator.
     */
    private void set_admin_visible() {
        Auto_set_all.setVisible(false);
        Edit.setVisible(false);
    }

    /**
     * The private method is that show the button which is used by the administrator, when
     * the System enters the administrator model.
     */
    private void set_visible() {
        Auto_set_all.setVisible(true);
        Edit.setVisible(true);
    }

    /**
     * when all category toggle is selected, show all movies data
     */
    private void handleAllCategory() {
//        categoryData.clear();
        ObservableList<String> categoryData = FXCollections.observableArrayList();
        categoryData.add("All");
        categoriesTable.setItems(categoryData);
        categoriesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));

        movieTable.setItems(mainApp.getMovieData());
        movieNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        // Listen for selection changes and show the movie
        categoriesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            movieTable.setItems(mainApp.getMovieData());
            movieNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
            movieTable.getSelectionModel().selectFirst();
        });
        categoriesTable.getSelectionModel().selectFirst();
    }

    /**
     * when age toggle is selected, show movies by age
     */
    private void handleAgeCategory() {
        Category ageCategory = new Category("Age");
        for (Movie movie : mainApp.getMovieData()) { // 分类年代
            String ageType;
            int year;
            try {
                year = Integer.parseInt(movie.getYear());
            } catch (NumberFormatException e) {
                year = -1;
            }
            if (year == -1) {
                ageType = "other";
            } else if (year < 1960) {
                ageType = "Before 1959";
            } else if (year < 2000) {
                ageType = "1960~1999";
            } else if (year < 2010) {
                ageType = "2000~2009";
            } else {
                ageType = "2010~now";
            }
            ageCategory.add(movie, ageType);
        }

        ObservableList<String> categoryData = FXCollections.observableArrayList();
        categoryData.addAll(ageCategory.getMovieMap().keySet());

        categoriesTable.setItems(categoryData);
        categoriesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        // Listen for selection changes and show the movie
        categoriesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            movieTable.setItems(ageCategory.getMovieMap().get(newValue));
            movieNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
            movieTable.getSelectionModel().selectFirst();
        });
        categoriesTable.getSelectionModel().selectFirst();
    }

    /**
     * when country toggle is selected, show movies by country
     */
    private void handleCountryCategory() {
        Category countryCategory = new Category("country");
        for (Movie movie : mainApp.getMovieData()) {// 将缩写变为全程
            Locale l = new Locale("", movie.getCountry());
            countryCategory.add(movie, l.getDisplayCountry(this.resourceBundle.getLocale()));
        }

        ObservableList<String> categoryData = FXCollections.observableArrayList();
        categoryData.addAll(countryCategory.getMovieMap().keySet());

        categoriesTable.setItems(categoryData);
        categoriesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));

        // Listen for selection changes and show the movie
        categoriesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            movieTable.setItems(countryCategory.getMovieMap().get(newValue));
            movieNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
            movieTable.getSelectionModel().selectFirst();
        });
        categoriesTable.getSelectionModel().selectFirst();
    }

    /**
     * when genre toggle is selected, show movies by genre
     */
    private void handleGenreCategory() {
        Category genreCategory = new Category("country");
        for (Movie movie : mainApp.getMovieData()) {
            genreCategory.add(movie, movie.getGenre());
        }

        ObservableList<String> categoryData = FXCollections.observableArrayList();
        categoryData.addAll(genreCategory.getMovieMap().keySet());

        categoriesTable.setItems(categoryData);
        categoriesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));

        // Listen for selection changes and show the movie
        categoriesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            movieTable.setItems(genreCategory.getMovieMap().get(newValue));
            movieNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
            movieTable.getSelectionModel().selectFirst();
        });
        categoriesTable.getSelectionModel().selectFirst();
    }

    /**
     * when our selection toggle is selected, show movies by our selection
     */
    private void handleOurSelectionCategory() {
        Category ourSelectionCategory = new Category("ourSelection");
        for (Movie movie : mainApp.getMovieData()) {
            if (movie.isIsOurSelection()) {
                ourSelectionCategory.add(movie, "ourSelection");
            }
        }

        ObservableList<String> categoryData = FXCollections.observableArrayList();
        categoryData.addAll(ourSelectionCategory.getMovieMap().keySet());

        categoriesTable.setItems(categoryData);
        categoriesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        // Listen for selection changes and show the movie
        categoriesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            movieTable.setItems(ourSelectionCategory.getMovieMap().get(newValue));
            movieNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
            movieTable.getSelectionModel().selectFirst();
        });
        categoriesTable.getSelectionModel().selectFirst();
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp the mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table 添加电影列表
        movieTable.setItems(mainApp.getMovieData());
        handleAllCategory(); //先显示全部
//        movieTable.getSelectionModel().select(0);

    }

    /**
     * Fills all text fields to show details about the movie.
     * If the specified movie is null, all text fields are cleared.
     *
     * @param movie the movie or null
     */
    private void showMovieDetails(Movie movie) {
        if (movie != null) {
            Locale l = new Locale("", movie.getCountry());
            // Fill the labels with info from the movie object.
            directorLabel.setText(movie.getDirector());
            mainActorsLabel.setText(movie.getMainActors());
            durationLabel.setText(movie.getDuration());
            countryLabel.setText(l.getDisplayCountry(this.resourceBundle.getLocale()));
            yearLabel.setText(movie.getYear());
            descriptionLabel.setText(movie.getDescription());
            languageLabel.setText(movie.getLanguage());
            movieNameLabel.setText(movie.getName());
            genreLabel.setText(movie.getGenre());
        } else {
            directorLabel.setText("");
            mainActorsLabel.setText("");
            durationLabel.setText("");
            countryLabel.setText("");
            yearLabel.setText("");
            descriptionLabel.setText("");
            languageLabel.setText("");
            movieNameLabel.setText("");
            genreLabel.setText("");
        }
    }

    /**
     * when user click edit, show the movie edit dialog.
     * when user finish editing, show current movie's new detail
     */
    @FXML
    private void handleEditMovie() {

        Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            boolean okClicked = mainApp.showMovieEditDialog(selectedMovie, resourceBundle.getLocale());
            if (okClicked) {
                showMovieDetails(selectedMovie);
            }

        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Movie Selected");
            alert.setContentText("Please select a movie in the table.\n");
            alert.showAndWait();
        }
    }

    /**
     * Called when the user clicks back.
     * show the welcome page
     */
    @FXML
    private void handleBack() {
        mainApp.showWelcome();
    }

    /**
     * The private method is that creating a keyboard operation of back button.
     * <p>
     * <ul>
     * <li>When pressing the ENTER, the System will perform the handleBack method.</li>
     * <li>When pressing the RIGHT, the focus will change to next table.</li>
     * <li>When pressing the RIGHT, the focus will change to last table.</li>
     * </ul>
     * </p>
     *
     * @param event The action of keyboard.
     */
    @FXML
    private void set_keyboard_Back(KeyEvent event) throws AWTException {
        if (event.getCode() == KeyCode.ENTER) {
            handleBack();
        }
        if (event.getCode() == KeyCode.RIGHT) {
            return;
        }
        if (event.getCode() == KeyCode.LEFT) {
            switchLeft();
            event.consume();
        }
    }

    @FXML
    public void setResources(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    /**
     * Called when the user clicks auto set all.
     * automatically set all the information of movies
     */
    @FXML
    private void handleAutoSetAll() throws IOException {
        ObservableList<Movie> movieData = mainApp.getMovieData();
        System.out.println("start auto set all, please wait");
        for (Movie aMovieData : movieData) {
            Movie movie = ScrapingUtil.scrapMovieInfo(aMovieData.getName());
            aMovieData.setDirector(movie.getDirector());
            aMovieData.setMainActors(movie.getMainActors());
            aMovieData.setLanguage(movie.getLanguage());
            aMovieData.setDescription(movie.getDescription());
        }
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
                    System.out.println("fail to auto set all");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
//            File movieFile = mainApp.getMovieFilePath();
        // Make sure it has the correct extension
        mainApp.saveMovieDataToFile(file);
        System.out.println("finish auto set all");

    }


    /**
     * Called when the user clicks play.
     * play the video
     */
    public void handlePlay() {
        String url;
        try {
            url = movieTable.getSelectionModel().getSelectedItem().getFileName();
//            splitPane.getStylesheets().add(getClass().getResource("view/style_black.css").toExternalForm());

            mainApp.showMediaView(url);
        } catch (NullPointerException ignored) {
        }
    }

    /**
     * The private method is that creating a keyboard operation of play button.
     *
     * @param event The action of keyboard.
     */
    @FXML
    private void set_keyboard_Play(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handlePlay();
        }
    }

    /**
     * Set the keyboard action of Auto set all Button.
     *
     * @param event The action of keyboard.
     * @throws IOException A exception of IO
     */
    @FXML
    private void set_keyboard_Admin(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            handleAutoSetAll();
        }
    }

    /**
     * Set the keyboard action of Edit Button.
     *
     * @param event The action of keyboard.
     * @throws IOException A exception of IO
     */
    @FXML
    private void set_keyboard_Edit(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            handleEditMovie();
        }
    }
}