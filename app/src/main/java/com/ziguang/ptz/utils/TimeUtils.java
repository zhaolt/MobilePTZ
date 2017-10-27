package com.ziguang.ptz.utils;

import android.content.Context;

import com.ziguang.ptz.App;
import com.ziguang.ptz.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by liutao on 6/22/16.
 */
public class TimeUtils {

    private static StringBuilder sFormatBuilder = new StringBuilder();
    private static Formatter sFormatter = new Formatter(sFormatBuilder, Locale.getDefault());

    public static String getDurationSecond(int timeS) {
        if (timeS < 0) {
            timeS = 0;
        }
        int seconds = timeS % 60;
        int minutes = (timeS / 60) % 60;
        int hours = timeS / 3600;
        sFormatBuilder.setLength(0);
        if (hours > 0) {
            return sFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return sFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public static String getDuration(int timeMs) {
        if (timeMs < 0) {
            timeMs = 0;
        }
        int millisecond = timeMs % 1000;

        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        //如果大于500毫秒，秒加一
        if (millisecond >= 500) {
            seconds = seconds + 1;
        }
        sFormatBuilder.setLength(0);
        if (hours > 0) {
            return sFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return sFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public static String getDurationCentisecond(long timeMs) {
        if (timeMs < 0) {
            timeMs = 0;
        }
        long millisecond = timeMs % 1000;

        long totalSeconds = timeMs / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long centisecond = millisecond / 100 * 10;
        centisecond += millisecond % 100 / 10;
        sFormatBuilder.setLength(0);
        return sFormatter.format("%02d:%02d.%02d", minutes, seconds, centisecond).toString();
    }

    public static String getDurationChinese(final int timeMs) {
        int totalSeconds = timeMs / 1000;
        int millisecond = timeMs % 1000;
        int seconds = totalSeconds % 60;
        int minutes = totalSeconds / 60;
        //如果大于500毫秒，秒加一
        if (millisecond >= 500) {
            seconds = seconds + 1;
        }
        //如果大于100分钟不显示秒，防止字符串在UI越界
        if (minutes > 100) {
            //如果大于30秒，加一分钟
            if (seconds > 30) {
                minutes = minutes + 1;
            }
            return minutes + App.INSTANCE.getResources().getString(R.string.minute);
        } else if (minutes > 0) {
            return minutes + App.INSTANCE.getResources().getString(R.string.minute) + seconds + App.INSTANCE.getResources().getString(R.string.second);
        } else {
            return seconds + App.INSTANCE.getResources().getString(R.string.second);
        }
    }

    public static String getDurationChineseTwo(final int timeMs) {
        int totalSeconds = timeMs / 1000;
        int millisecond = timeMs % 1000;
        int seconds = totalSeconds % 60;
        int minutes = totalSeconds / 60;
        //如果大于500毫秒，秒加一
        String vlaue = String.valueOf(millisecond).substring(0, 1);
        //如果大于100分钟不显示秒，防止字符串在UI越界
        if (minutes > 100) {
            //如果大于30秒，加一分钟
            if (seconds > 30) {
                minutes = minutes + 1;
            }
            return minutes + App.INSTANCE.getResources().getString(R.string.minute);
        } else if (minutes > 0) {
            return minutes + App.INSTANCE.getResources().getString(R.string.minute) + seconds + "." + vlaue + App.INSTANCE.getResources().getString(R.string.second);
        } else {
            return seconds + "." + vlaue + App.INSTANCE.getResources().getString(R.string.second);
        }
    }

    public static String getDurationMillisecond(final int timeMs) {
        int millisecond = timeMs % 1000;
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        //如果大于500毫秒，秒加一
        if (millisecond >= 500) {
            seconds = seconds + 1;
        }
        sFormatBuilder.setLength(0);
        if (hours > 0) {
            return sFormatter.format("%d:%02d:%02d:%d", hours, minutes, seconds, millisecond).toString();
        } else {
            return sFormatter.format("%02d:%02d:%d", minutes, seconds, millisecond).toString();
        }
    }

    public static String getDate(long dateAdded) {
        Date date = new Date(dateAdded);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(date);
    }

    public static String getDate2(long dateAdded) {
        Date date = new Date(dateAdded);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        return format.format(date);
    }

    public static String applyFriendlyDate(Context context, long date) {
        int offSet = Calendar.getInstance().getTimeZone().getRawOffset();
        long today = (System.currentTimeMillis() + offSet) / 86400000L;
        long start = (date * 1000L + offSet) / 86400000L;
        long intervalTime = start - today;
        if (intervalTime == 0) {
            return context.getString(R.string.today);
        } else if (intervalTime == -1) {
            return context.getString(R.string.yesterday);
        } else {
            return null;
        }
    }

    public static String getQuotationFormatedDuration(int duration) {
        int totalSeconds = duration / 1000;
        int millisecond = duration % 1000;

        int seconds = totalSeconds % 60;
        int minutes = totalSeconds / 60;
        //如果大于500毫秒，秒加一
        if (millisecond >= 500) {
            seconds = seconds + 1;
        }
        sFormatBuilder.setLength(0);
        return sFormatter.format("%d'%02d''", minutes, seconds).toString();
    }

    public static String getQuotationFormatedDurationMillisecond(int duration) {
        int millisecond = duration % 1000;

        int totalSeconds = duration / 1000;

        int seconds = totalSeconds % 60;
        int minutes = totalSeconds / 60;

        sFormatBuilder.setLength(0);
        return sFormatter.format("%02d'%02d''%03d", minutes, seconds, millisecond).toString();
    }
}
