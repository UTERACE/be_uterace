package com.be_uterace.utils;

public class Format {
    public static String formatSeconds(int seconds) {
        // Chia số giây thành giờ, phút và giây
        int minutes = seconds / 60;
        seconds %= 60;
        int hours = minutes / 60;
        minutes %= 60;

        // Định dạng thành chuỗi "giờ:phút:giây"
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static String secondsToHMS(int seconds) {
        int totalSeconds = seconds * 60;  // Chuyển đổi thành số giây
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int remainingSeconds = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }

}
