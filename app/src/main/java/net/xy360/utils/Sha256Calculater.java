package net.xy360.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jolin on 2016/3/14.
 */
public class Sha256Calculater {
    public static String calSha(String s) {
        String passwordsha = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.reset();
            byte[] hash = md.digest(s.getBytes());
            passwordsha = String.format("%064x", new BigInteger(1, hash));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return passwordsha;
    }
}
