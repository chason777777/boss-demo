package com.chason.bossdemo.common.util;

import java.util.Random;
import java.util.UUID;

public class UuidUtil {
    public static String randomUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String random32Key() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String random16Key() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
    }

    public static String randomSalt() {
        Random rand = new Random();
        char[] letters = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
                'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
                'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'r',
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        String str = "";
        int index;
        boolean[] flags = new boolean[letters.length];//默认为false
        for (int i = 0; i < 5; i++) {
            do {
                index = rand.nextInt(letters.length);
            } while (flags[index] == true);
            char c = letters[index];
            str += c;
            flags[index] = true;
        }
        return str;
    }
}
