/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.juniorlima.login.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 *
 * @author junior
 */
public class Utils {

    public String createHash(String password) throws NoSuchAlgorithmException {
        MessageDigest token = MessageDigest.getInstance("SHA-256");
        String tokenInput = password;
        token.update(tokenInput.getBytes(), 0, tokenInput.length());
        password = new BigInteger(1, token.digest()).toString();
        return password;
    }

    public long compareMinutes(Date now, Date lastLogin) {
        DateTime d1 = new DateTime(now);
        DateTime d2 = new DateTime(lastLogin);

        Interval interval = new Interval(d2, d1);
        System.out.println(interval);

        org.joda.time.Duration duration = interval.toDuration();
        System.out.println("Duration in minutes: " + duration.getStandardMinutes());
        long minutes = duration.getStandardMinutes();
        return minutes;
    }
}
