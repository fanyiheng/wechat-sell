package com.example.sell.util;

import java.util.Random;

public class KeyUtil {
    public static synchronized String genUniqueKey(){
        Random random = new Random();
        int randomNum = random.nextInt(900000) + 100000;
        return System.currentTimeMillis()+String.valueOf(randomNum);
    }
}
