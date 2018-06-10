package main.util;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

import java.io.File;

/**
 *  Translate the time of video into the the form of hour, minute and second.
 */
public class videoDurationUtil {
    public static String ReadVideoDuration(File source) {
        Encoder encoder = new Encoder();
        String duration = "";
        try {
            MultimediaInfo m = encoder.getInfo(source);
            long ls = m.getDuration() / 1000;
            int hour = (int) (ls / 3600);
            int minute = (int) (ls % 3600) / 60;
            int second = (int) (ls - hour * 3600 - minute * 60);
            if (hour == 0) {
                if (minute == 0)
                    duration = second + "'''";
                else
                    duration = minute + "''" + second + "'''";
            } else {
                duration = hour + "'" + minute + "''" + second + "'''";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return duration;
    }
}
