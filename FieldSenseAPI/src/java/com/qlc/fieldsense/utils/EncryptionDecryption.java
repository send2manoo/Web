/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.utils;

import static com.qlc.fieldsense.utils.FieldSensePasswordEncryptionDecryption.log4jLog;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author jyoti
 */
public class EncryptionDecryption {
    
    private static final int ITERATIONS = 1000;
    private static final int KEY_LENGTH = 70; // bits
    private static final String TOKEN = "CLQESNESDLEIF";
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";
    
    public String hashCode(String value) {
        try {
            char[] passwordChars = value.toCharArray();
            byte[] saltBytes = TOKEN.getBytes();
            PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory key = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hashedPassword = key.generateSecret(spec).getEncoded();
            return String.format("%x", new BigInteger(hashedPassword));
        } catch (InvalidKeySpecException ex) {
            log4jLog.info(" hashCode "+ex);
            ex.printStackTrace();
            return "";
        } catch (NoSuchAlgorithmException ex) {
            log4jLog.info(" hashCode "+ex);
            ex.printStackTrace();
            return "";
        }
    }
    
}
