package com.example.teamcity.api.generators;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomData {
    private static final int LENGTH = 5;
    private static final int LENGTH2 = 226;
    private static final int LENGTH3 = 225;

    // generateString instead of getString
    public static String getString() {
        return "test_" + RandomStringUtils.randomAlphabetic(LENGTH) + RandomStringUtils.randomNumeric(LENGTH);
    }

    public static String getString226() {
        return RandomStringUtils.randomAlphabetic(LENGTH2);
    }

    public static String getString225() {
        return RandomStringUtils.randomAlphabetic(LENGTH3);
    }

    public static String generateStringWithSpecialCharacters(int length) {

        return RandomStringUtils.random(length, "!@#$%^&*';:1234567890?.,/`~><=+-[](){}йцукенгшщзхъфывапролджэёячсмитьбюqwertyuiopasdfghjklzxcvbnm");
    }

}

