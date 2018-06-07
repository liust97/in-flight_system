package main.view;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

import javax.xml.stream.XMLEventWriter;


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
    @FXML
    private Button key;
    private MainApp mainApp;


    double store_volume;
    Media media;
    MediaPlayer mediaPlayer;
    String UrL;

    private void store_volume(double temp_volume) {
        this.store_volume = temp_volume;
    }

    private double getStore_volume() {
        return store_volume;
    }

    public void initialize() {

    }
    public static final int FUNC_KEY_MARK = 1;
    @FXML
    private void showmovie() {
        // this part comes from javafx.scene.media api.
        try {
            media = new Media(UrL);
            if (media.getError() == null) {
                media.setOnError(new Runnable() {
                    @Override
                    public void run() {
                        // Handle asynchronous error in Media object.
                    }
                });
            }
            mediaPlayer = new MediaPlayer(media);
            moviepane.setMediaPlayer(mediaPlayer);
            mediaPlayer.setAutoPlay(true);
        } catch (Exception mediaException) {
            // Handle exception in Media constructor.
        }

        setbuttonbackground();
        setFocus();
        setSlider();
        movie();
        setStop();
        setSilent();
    }

    // 移除可能出现的焦点事件
    private void setFocus() {
        exit.setFocusTraversable(false);
        play.setFocusTraversable(false);
        slider.setFocusTraversable(false);
        time.setFocusTraversable(false);
        volume_control.setFocusTraversable(false);
        silent.setFocusTraversable(false);
        key.setFocusTraversable(true);
    }

    @FXML
    public void keyTyped(KeyEvent event) {
        System.out.println(event.getCode());
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

    // 设置视频进度条与声音进度条
    @FXML
    private void movie() {
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                time();
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
    private void time() {
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
            Stop_method();
        });
    }

    private void Stop_method() {
        MediaPlayer.Status status = mediaPlayer.getStatus();
        if (status == MediaPlayer.Status.PLAYING) {
            String play_pause_pic = Thread.currentThread().getContextClassLoader().getResource("main/picture/pause_button.png").toString();
            play.setGraphic(new ImageView(new Image(play_pause_pic, 20, 20, true, true)));
            mediaPlayer.pause();
        } else if (status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.STOPPED || status == MediaPlayer.Status.READY) {
            String play_pic = Thread.currentThread().getContextClassLoader().getResource("main/picture/play_button.png").toString();
            play.setGraphic(new ImageView(new Image(play_pic, 20, 20, true, true)));
            mediaPlayer.seek(mediaPlayer.getCurrentTime());
            mediaPlayer.play();
        }
        Duration duration = mediaPlayer.getMedia().getDuration();

        if (duration.equals(mediaPlayer.getCurrentTime())) {
            slider.setValue(0);
            mediaPlayer.seek(duration.multiply(0));
        }
    }

    // 静音按钮
    @FXML
    private void setSilent() {
        silent.setOnAction(event -> {
            Silent_method();
        });
    }

    private void Silent_method() {
        if (volume_control.getValue() == 0) {
            volume_control.valueProperty().setValue(getStore_volume());
            String volume_pic = Thread.currentThread().getContextClassLoader().getResource("main/picture/volume_button.png").toString();
            silent.setGraphic(new ImageView((new Image(volume_pic , 20, 20, true, true))));
        } else {
            double temp = volume_control.getValue();
            store_volume(temp);
            volume_control.valueProperty().setValue(0);
            String volume_slient_pic = Thread.currentThread().getContextClassLoader().getResource("main/picture/slient_button.png").toString();
            silent.setGraphic(new ImageView((new Image(volume_slient_pic , 20, 20, true, true))));
        }
    }

    public void setMainApp(MainApp mainApp) { // 以及播放
        this.mainApp = mainApp;
        System.out.println(mainApp.getMovieURL());
        this.UrL = Thread.currentThread().getContextClassLoader().getResource(mainApp.getMovieURL()).toString();
        showmovie();
    }

    //退出按钮
    @FXML
    private void setExit() {
        mediaPlayer.stop();
        mediaPlayer.dispose();
        mainApp.showMovieOverview(mainApp.getResourceBundle().getLocale());
    }

    // 设置按钮图片
    private void setbuttonbackground() {
        String play_pic = Thread.currentThread().getContextClassLoader().getResource("main/picture/play_button.png").toString();
        String volume_pic = Thread.currentThread().getContextClassLoader().getResource("main/picture/volume_button.png").toString();
        String exit_pic = Thread.currentThread().getContextClassLoader().getResource("main/picture/exit_button.png").toString();
        play.setGraphic(new ImageView(new Image(play_pic, 20, 20, true, true)));
        silent.setGraphic(new ImageView((new Image(volume_pic , 20, 20, true, true))));
        exit.setGraphic(new ImageView((new Image(exit_pic , 20, 20, true, true))));
    }
}
