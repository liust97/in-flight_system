package main.view;

import main.MainApp;
import main.util.DateUtil;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;


public class MediaviewController {
    @FXML
    private AnchorPane anchorPane;
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
    private BorderPane borderPane;
    @FXML
    private Button exit;
    private MainApp mainApp;


    double store_volume;
    Media media;
    MediaPlayer mediaPlayer;
    String UrL;

    //    String urL = "file:///C:/aaa/BlackOrpheus_br_1959.mp4";
    private void store_volume(double temp_volume) {
        this.store_volume = temp_volume;
    }

    private double getStore_volume() {
        return store_volume;
    }

    public void initialize() {

    }

    @FXML
    private void showmovie() {
        media = new Media(UrL);
        mediaPlayer = new MediaPlayer(media);
        moviepane.setMediaPlayer(mediaPlayer);
        mediaPlayer.setAutoPlay(true);
        setSlider();
        movie();
        setStop();
        setSilent();
//        setExit();
    }

    // 设置视频进度条与声音进度条
    @FXML
    private void movie() {
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                update_time();
            }
        });
        mediaPlayer.volumeProperty().bind(volume_control.valueProperty().divide(100));
        volume_control.valueProperty().setValue(100);
    }

    //监听进度条的变化
    @FXML
    private void setSlider() {
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (slider.isValueChanging()) {
                    Duration duration = mediaPlayer.getMedia().getDuration();
                    mediaPlayer.seek(duration.multiply(slider.getValue() / 100.0));
                }
                if (slider.isPressed()) {
                    Duration duration = mediaPlayer.getMedia().getDuration();
                    mediaPlayer.seek(duration.multiply(slider.getValue() / 100.0));
                }
            }
        });
    }

    // 建构视频执行时间显示框
    private void update_time() {
        DateUtil df = new DateUtil();
        Duration duration = mediaPlayer.getMedia().getDuration();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Duration currentTime = mediaPlayer.getCurrentTime();
                double show_now_time = currentTime.toMillis();
                time.setText(df.changeformat(show_now_time) + "/" + df.changeformat(duration.toMillis()));
                slider.setValue(currentTime.toMillis() / duration.toMillis() * 100);
            }
        });
    }

    // 视频暂停与重连模块，已经播放完成后，点击重播
    @FXML
    private void setStop() {
        play.setOnAction(e -> {
            MediaPlayer.Status status = mediaPlayer.getStatus();
            if (status == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
            } else if (status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.STOPPED || status == MediaPlayer.Status.READY) {
                mediaPlayer.seek(mediaPlayer.getCurrentTime());
                mediaPlayer.play();
            }
            Duration duration = mediaPlayer.getMedia().getDuration();

            if (duration.equals(mediaPlayer.getCurrentTime())) {
                slider.setValue(0);
                mediaPlayer.seek(duration.multiply(0));
            }
        });
    }

    // 静音按钮
    @FXML
    private void setSilent() {
        silent.setOnAction(event -> {
            if (volume_control.getValue() == 0) {
                volume_control.valueProperty().setValue(getStore_volume());
            } else {
                double temp = volume_control.getValue();
                store_volume(temp);
                volume_control.valueProperty().setValue(0);
            }
        });
    }

    public void setMainApp(MainApp mainApp){ // 以及播放
        this.mainApp = mainApp;
        System.out.println(mainApp.getMovieURL());
        this.UrL = Thread.currentThread().getContextClassLoader().getResource(mainApp.getMovieURL()).toString();
        showmovie();
    }

    //退出按钮
    @FXML
    private void setExit() {
        mainApp.showMovieOverview(mainApp.getResourceBundle().getLocale());
    }
}
