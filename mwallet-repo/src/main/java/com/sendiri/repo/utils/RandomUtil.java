package com.sendiri.repo.utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {

    public static String random6Digit() {
        int number = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return String.valueOf(number);
    }

}