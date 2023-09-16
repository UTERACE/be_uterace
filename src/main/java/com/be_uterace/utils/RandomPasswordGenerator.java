package com.be_uterace.utils;

import java.security.SecureRandom;
import java.util.Random;

public class RandomPasswordGenerator {

    private static final String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_+=<>?";

    public static String generateRandomPassword(int length) {
        if (length < 8) {
            throw new IllegalArgumentException("Độ dài mật khẩu ít nhất là 8 ký tự.");
        }

        StringBuilder password = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        // Sử dụng ít nhất một ký tự từ mỗi loại
        password.append(LOWERCASE_CHARACTERS.charAt(secureRandom.nextInt(LOWERCASE_CHARACTERS.length())));
        password.append(UPPERCASE_CHARACTERS.charAt(secureRandom.nextInt(UPPERCASE_CHARACTERS.length())));
        password.append(DIGITS.charAt(secureRandom.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARACTERS.charAt(secureRandom.nextInt(SPECIAL_CHARACTERS.length())));

        // Sử dụng các ký tự ngẫu nhiên cho phần còn lại của mật khẩu
        String allCharacters = LOWERCASE_CHARACTERS + UPPERCASE_CHARACTERS + DIGITS + SPECIAL_CHARACTERS;
        for (int i = 4; i < length; i++) {
            password.append(allCharacters.charAt(secureRandom.nextInt(allCharacters.length())));
        }

        // Trộn lẫn các ký tự để làm cho mật khẩu ngẫu nhiên hơn
        char[] passwordArray = password.toString().toCharArray();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(length);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[randomIndex];
            passwordArray[randomIndex] = temp;
        }

        return new String(passwordArray);
    }
}

