package com.be_uterace.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
    public static Date convertStringToDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(dateString);
        } catch (Exception e) {
            // Xử lý lỗi nếu định dạng không hợp lệ hoặc chuỗi ngày không thể chuyển đổi thành Date
            e.printStackTrace();
            return null;
        }
    }
}
