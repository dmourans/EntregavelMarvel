package com.br.deividmoura_marvel.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Creator {

    public static final String PRIVATE_KEY = "0dd0c16fedb8a02985977eafca66b49f5e6a526f";
    public static final String PUBLIC_KEY = "6eb7e8896ec5850c52515a8a23ee97f0";

    private static char[] HEXCHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static String hexEncode(byte[] bytes) {
        char[] result = new char[bytes.length * 2];
        int b;
        for (int i = 0, j = 0; i < bytes.length; i++) {
            b = bytes[i] & 0xff;
            result[j++] = HEXCHARS[b >> 4];
            result[j++] = HEXCHARS[b & 0xf];
        }
        return new String(result);
    }

    public static String md5(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());

            return hexEncode(digest.digest());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
