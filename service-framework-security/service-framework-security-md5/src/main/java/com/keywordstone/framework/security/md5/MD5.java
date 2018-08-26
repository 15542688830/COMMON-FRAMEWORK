package com.keywordstone.framework.security.md5;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class MD5 {

    private final static String TYPE = "MD5";

    public static String encode(String code) {
        if (StringUtils.isNotEmpty(code)) {
            try {
                MessageDigest digest = MessageDigest.getInstance(TYPE);
                return new BigInteger(digest.digest(code.getBytes())).toString(16);
            } catch (NoSuchAlgorithmException e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    public static boolean check(String encoded,
                                String decoded) {
        if (StringUtils.isNoneEmpty(encoded, decoded)) {
            return encoded.equals(encode(decoded));
        }
        return false;
    }
}
