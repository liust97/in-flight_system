package main.model;

import javafx.beans.property.*;

/**
 * Model class for a Movie.
 *
 * @author Liu Sitong
 */
public class Movie {
    private final StringProperty name;
    private final StringProperty director;
    private final StringProperty mainActors;
    private final StringProperty duration;
    private final StringProperty language;
    private final StringProperty year;
    private final StringProperty description;
    private final StringProperty fileName;
    private final StringProperty country;
    private final BooleanProperty isOurSelection;
    private final StringProperty genre;
    /**
     * Default Constructor
     */
    public Movie(){
        this.name = new SimpleStringProperty("/");
        this.fileName = new SimpleStringProperty("/");
        this.year = new SimpleStringProperty("/");
        this.country = new SimpleStringProperty("/");
        this.duration = new SimpleStringProperty("/");
        // Some initial dummy data, just for convenient testing.
        this.director = new SimpleStringProperty("/");
        this.mainActors = new SimpleStringProperty("/");
        this.description = new SimpleStringProperty("/");
        this.language = new SimpleStringProperty("/");
        this.genre = new SimpleStringProperty("/");
        this.isOurSelection = new SimpleBooleanProperty(false);
    }

    public Movie(String fileName, String name, String country, String year, String duration) {
        this.name = new SimpleStringProperty(name);
        this.fileName = new SimpleStringProperty(fileName);
        this.year = new SimpleStringProperty(year);
        this.country = new SimpleStringProperty(country);
        this.duration = new SimpleStringProperty(duration);

        // Some initial dummy data, just for convenient testing.
        this.director = new SimpleStringProperty("/");
        this.mainActors = new SimpleStringProperty("/");
        this.description = new SimpleStringProperty("/");
        this.language = new SimpleStringProperty("/");
        this.isOurSelection = new SimpleBooleanProperty(false);
        this.genre = new SimpleStringProperty("/");
    }

    public Movie(String fileName, String name, String duration) {
        this.name = new SimpleStringProperty(name);
        this.fileName = new SimpleStringProperty(fileName);
        this.duration = new SimpleStringProperty(duration);
        // Some initial dummy data

        this.year = new SimpleStringProperty("/");
        this.language = new SimpleStringProperty("/");
        this.director = new SimpleStringProperty("/");
        this.mainActors = new SimpleStringProperty("/");
        this.description = new SimpleStringProperty("/");
        this.country = new SimpleStringProperty("/");
        this.isOurSelection = new SimpleBooleanProperty(false);
        this.genre = new SimpleStringProperty("/");
    }

    public String getDirector() {
        return director.get();
    }

    public StringProperty directorProperty() {
        return director;
    }

    public void setDirector(String director) {
        this.director.set(director);
    }

    public String getMainActors() {
        return mainActors.get();
    }

    public StringProperty mainActorsProperty() {
        return mainActors;
    }

    public void setMainActors(String mainActors) {
        this.mainActors.set(mainActors);
    }

    public String getDuration() {
        return duration.get();
    }

    public StringProperty durationProperty() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration.set(duration);
    }

    public String getLanguage() {
        return language.get();
    }

    public StringProperty languageProperty() {
        return language;
    }

    public void setLanguage(String language) {
        this.language.set(language);
    }

    public String getYear() {
        return year.get();
    }

    public StringProperty yearProperty() {
        return year;
    }

    public void setYear(String year) {
        this.year.set(year);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getFileName() {
        return fileName.get();
    }

    public StringProperty fileNameProperty() {
        return fileName;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }

    public String getCountry() {
        return country.get();
    }

    public StringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public boolean isIsOurSelection() {
        return isOurSelection.get();
    }

    public BooleanProperty isOurSelectionProperty() {
        return isOurSelection;
    }

    public void setIsOurSelection(boolean isOurSelection) {
        this.isOurSelection.set(isOurSelection);
    }

    public String getGenre() {
        return genre.get();
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }
}