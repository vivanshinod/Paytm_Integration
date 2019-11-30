package com.ravan.paytmintegration.checksum_constant;

import java.util.UUID;

public class Helpers {
    public static String generateRandomString() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }
}
