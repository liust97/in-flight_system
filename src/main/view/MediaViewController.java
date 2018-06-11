package main.view;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.MainApp;
import main.util.DateUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class MediaViewController {
    @FXML
    private MediaView moviepane;
    @FXML
    private Slider slider;
    @FXML
    private TextField time;
    @FXML
    private Button silent;
    @FXML
    private Slider volume_control;
    @FXML
    private Button play;
    @FXML
    private Button exit;
    @FXML
    private Button key;
    private MainApp mainApp;

    private double store_volume;
    //    private  Media media;
    private MediaPlayer mediaPlayer;
    private String UrL;

    /**
     * The private method is used to store the value of volume.
     * <p>
     * When the user click the silent button, the method will store
     * the value of volume.
     *
     * @param temp_volume A static variable to store the value of volume.
     */
    private void store_volume(double temp_volume) {
        this.store_volume = temp_volume;
    }

    /**
     * The private method is used to get the value of volume.
     * <p>
     * When the movie is silent, if the user click the silent  button,
     * the value of the volume will come back to the value, which is
     * the value before clicking the silent button.
     *
     * @return The value of volume, which has been store before clicking
     * the silent button.
     */
    private double getStore_volume() {
        return store_volume;
    }


    /**
     * The private method is used in the model of the MediaView in
     * the fxml file.
     * <p>
     * <li> According the UrL, creating a new media.</li>
     * <li> Using the media, creating a new MediaPlayer.</li>
     * <li> Show the mediaPlayer in the model of the MediaView in the fxml file. Then, setting
     * play the movie automatically.</li>
     * </p>
     * <p>
     * Calling the methods which were used to control the property of movie
     * </p>
     *
     * <ul>
     * <li>set_button_background()/li>
     * <li>setFocus()</li>
     * <li>setSlider()</li>
     * <li>movie()</li>
     * <li>setStop()</li>
     * <li>setSilent()</li>
     * </ul>
     */
    @FXML
    private void ShowMovie() {
        // this part comes from javafx.scene.media api.
        try {
            Media media = new Media(UrL);
            if (media.getError() == null) {
                media.setOnError(() -> {
                    // Handle asynchronous error in Media object.
                });
            }
            mediaPlayer = new MediaPlayer(media);
            moviepane.setMediaPlayer(mediaPlayer);
            mediaPlayer.setAutoPlay(true);
        } catch (Exception mediaException) {
            // Handle exception in Media constructor.
        }

        set_button_background();
        setFocus();
        setSlider();
        movie();
        setStop();
        setSilent();
    }

    /**
     * The private method is used to remove the focus of the buttons in the BorderPane.
     * <p>
     *
     * </p>
     */
    private void setFocus() {
        exit.setFocusTraversable(false);
        play.setFocusTraversable(false);
        slider.setFocusTraversable(false);
        time.setFocusTraversable(false);
        volume_control.setFocusTraversable(false);
        silent.setFocusTraversable(false);
        key.setFocusTraversable(true);
    }

    /**
     * The private method provide a way to control the mediaPlayer by keyboard.
     * <p>
     * <ul>
     * <li>When pressing the DOWN, if the movie is playing, the movie will be stopped. Then,
     * if the movie is paused, the movie will be playing.</li>
     * <li>When pressing the LEFT, the movie will be receding.</li>
     * <li>When pressing the RIGHT, the movie will be speeding.</li>
     * <li>When pressing the SUBTRACT, the volume will be decreased.</li>
     * <li>When pressing the ADD, the volume will be increased.</li>
     * <li>When pressing the MULTIPLY, the movie will be quiet.</li>
     * <li>When pressing the UP, coming back to the last scene.</li>
     * </ul>
     * </p>
     *
     * @param event The event of keyboard.
     */
    @FXML
    public void keyTyped(KeyEvent event) {
        // 按下停止视频
        if (event.getCode() == KeyCode.DOWN) {
            Stop_method();
        }
        // 按左后退视频
        if (event.getCode() == KeyCode.LEFT) {
            Duration duration = mediaPlayer.getMedia().getDuration();
            slider.setValue(slider.getValue() - 5.0);
            mediaPlayer.seek(duration.multiply(slider.getValue() / 100.0));
            setSlider();
        }
        // 按右快进视频
        if (event.getCode() == KeyCode.RIGHT) {
            Duration duration = mediaPlayer.getMedia().getDuration();
            slider.setValue(slider.getValue() + 5.0);
            mediaPlayer.seek(duration.multiply(slider.getValue() / 100.0));
            setSlider();
        }
        // 按减号声音减小
        if (event.getCode() == KeyCode.SUBTRACT) {
            double a = volume_control.getValue();
            volume_control.valueProperty().setValue(--a);

        }
        // 按加号声音增大
        if (event.getCode() == KeyCode.ADD) {
            double a = volume_control.getValue();
            volume_control.valueProperty().setValue(++a);
        }
        // 按星号静音
        if (event.getCode() == KeyCode.MULTIPLY) {
            Silent_method();
        }
        // 按上号退出，返回前一个界面
        if (event.getCode() == KeyCode.UP) {
            setExit();
        }

    }

    /**
     * The private method is used to set the progress of the movie and the progress
     * of the volume.
     * <p>
     * Add a listener to obtain the current time of mediaPlayer. Then, according to the current
     * time to set the action of the progress slider. In addition, showing the current time in
     * the TextFiled.<br>
     * Obtaining the volume of the mediaPlayer, and bind it with the slider of
     * controlling volume. Then, setting the volume is full, when starting to
     * play the movie.
     * </p>
     */
    @FXML
    private void movie() {
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            DateUtil df = new DateUtil();
            Duration duration = mediaPlayer.getMedia().getDuration();
            Platform.runLater(() -> {
                Duration currentTime = mediaPlayer.getCurrentTime();
                double show_now_time = currentTime.toMillis();
                time.setText(df.ChangeFormat(show_now_time) + "/" + df.ChangeFormat(duration.toMillis()));
                slider.setValue(currentTime.toMillis() / duration.toMillis() * 100);
            });
        });
        mediaPlayer.volumeProperty().bind(volume_control.valueProperty().divide(100));
        volume_control.valueProperty().setValue(100);
    }

    /**
     * The private method is used to deal with the action of jumping in the media.
     * <p>
     * <ul>
     * <li>When the user drags the slider</li>
     * <li>When the user click the slider</li>
     * </ul>
     * </p>
     */
    //监听进度条的变化
    @FXML
    private void setSlider() {
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (slider.isValueChanging()) {
                Duration duration = mediaPlayer.getMedia().getDuration();
                mediaPlayer.seek(duration.multiply(slider.getValue() / 100.0));
            }
            if (slider.isPressed()) {
                Duration duration = mediaPlayer.getMedia().getDuration();
                mediaPlayer.seek(duration.multiply(slider.getValue() / 100.0));
            }
        });
    }

    /**
     * The action of play button.
     */
    @FXML
    private void setStop() {
        play.setOnAction(e -> Stop_method());
    }

    /**
     * The movie status module.
     * <p>
     * <ol>
     * <li>If the user clicks the play button, the movie will be stopped.</li>
     * <li>If the status of the movie is paused, the movie will continue to playing
     * movie when the user click the play button.</li>
     * <li>When the movie is end, if the user click the button, the movie will replay.</li>
     * </ol>
     * </p>
     */
    private void Stop_method() {
        MediaPlayer.Status status = mediaPlayer.getStatus();
        Duration duration = mediaPlayer.getMedia().getDuration();
        if (status == MediaPlayer.Status.PLAYING) {
            try {
                String play_pause_pic = Thread.currentThread().getContextClassLoader().getResource("main/picture/pause_button.png").toString();
                play.setGraphic(new ImageView(new Image(play_pause_pic, 20, 20, true, true)));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mediaPlayer.pause();
            }
        } else if (status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.STOPPED || status == MediaPlayer.Status.READY) {
            try {
                String play_pic = Thread.currentThread().getContextClassLoader().getResource("main/picture/play_button.png").toString();
                play.setGraphic(new ImageView(new Image(play_pic, 20, 20, true, true)));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mediaPlayer.seek(mediaPlayer.getCurrentTime());
                mediaPlayer.play();
            }
        }
        if (duration.equals(mediaPlayer.getCurrentTime())) {
            try {
                String play_pic = Thread.currentThread().getContextClassLoader().getResource("main/picture/play_button.png").toString();
                play.setGraphic(new ImageView(new Image(play_pic, 20, 20, true, true)));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                slider.setValue(0);
                mediaPlayer.seek(duration.multiply(0));
            }

        }
    }

    /**
     * The action of Volume button.
     */
    @FXML
    private void setSilent() {
        silent.setOnAction(event -> Silent_method());
    }

    /**
     * The mute module of movie.
     * <p>
     * <ol>
     * <li>If the user clicks the silent button, the volume of the movie will be zero.</li>
     * <li>If the user clicks the silent button, when the volume of the movie is zero, the volume
     * will come back to the value which is the value before click the button.</li>
     * </ol>
     * </p>
     */
    private void Silent_method() {
        if (volume_control.getValue() == 0) {

            try {
                String volume_pic = Thread.currentThread().getContextClassLoader().getResource("main/picture/volume_button.png").toString();
                silent.setGraphic(new ImageView((new Image(volume_pic, 20, 20, true, true))));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                volume_control.valueProperty().setValue(getStore_volume());
            }

        } else {

            try {
                double temp = volume_control.getValue();
                store_volume(temp);
                volume_control.valueProperty().setValue(0);
                String volume_silent_pic = Thread.currentThread().getContextClassLoader().getResource("main/picture/silent_button.png").toString();
                silent.setGraphic(new ImageView((new Image(volume_silent_pic, 20, 20, true, true))));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public void setMainApp(MainApp mainApp) { // 以及播放
        this.mainApp = mainApp;
//        System.out.println(mainApp.getMovieURL());
        this.UrL = Thread.currentThread().getContextClassLoader().getResource(mainApp.getMovieURL()).toString();
        ShowMovie();
    }

    //退出按钮
    @FXML
    private void setExit() {
        mediaPlayer.stop();
        mediaPlayer.dispose();
        mainApp.showMovieOverview(mainApp.getResourceBundle().getLocale());
    }

    /**
     * Setting the picture of the play button, silent button and the exit button.
     */
    private void set_button_background() {
        try {
            String play_pic = Thread.currentThread().getContextClassLoader().getResource("main/picture/play_button.png").toString();
            String volume_pic = Thread.currentThread().getContextClassLoader().getResource("main/picture/volume_button.png").toString();
            String exit_pic = Thread.currentThread().getContextClassLoader().getResource("main/picture/exit_button.png").toString();
            play.setGraphic(new ImageView(new Image(play_pic, 20, 20, true, true)));
            silent.setGraphic(new ImageView((new Image(volume_pic, 20, 20, true, true))));
            exit.setGraphic(new ImageView((new Image(exit_pic, 20, 20, true, true))));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
