package com.cviana.hermes.utilities;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncoderSha256 {

    private static String convertBytesToHex(byte[] object) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : object) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String encode(Object object) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(GeneralConverter.convertObjectToBytes(object));
            return convertBytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String encode(String object) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(object.getBytes(StandardCharsets.UTF_8));
            return convertBytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
