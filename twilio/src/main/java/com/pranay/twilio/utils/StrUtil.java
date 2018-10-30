package com.pranay.twilio.utils;

/**
 * Created By Pranay on 10/30/2018
 */
public class StrUtil {


    /**
     * Non null.
     *
     * @param s
     *            the Input String
     * @return the string - which is not null
     */
    public static String nonNull(String s) {
        return s != null ? s : "";
    }
}
