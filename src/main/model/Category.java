package main.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class Category {

    private final StringProperty name;
    private HashMap<String,ObservableList<Movie>> movieMap;

    public Category(String name) {
        this.name = new SimpleStringProperty(name);
        this.movieMap = new HashMap<>();
    }

    public void add(Movie movie, String key){
        if (movieMap.containsKey(key)){
            movieMap.get(key).add(movie);
        }else{
            ObservableList<Movie> ob = FXCollections.observableArrayList();
            ob.add(movie);
            movieMap.put(key,ob);
        }
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

    public HashMap<String, ObservableList<Movie>> getMovieMap() {
        return movieMap;
    }

    public void setMovieMap(HashMap<String, ObservableList<Movie>> movieMap) {
        this.movieMap = movieMap;
    }
}
