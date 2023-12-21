package com.be_uterace.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Map;

//@Component
public class VariableGlobal {
    private static boolean flag;

    @PostConstruct
    public void init() {
        flag = false;
        // Bạn có thể khởi tạo bản đồ với các giá trị mặc định ở đây nếu cần
        // map.put(key, value);
    }

    public static boolean getFlag() {
        return flag;
    }

    public static void setFlag(boolean newFlag) {
        flag = newFlag;
    }
}
