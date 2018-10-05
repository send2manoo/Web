/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.utils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.log4j.Logger;

/**
 *
 * @author Ramesh
 * @date 31-01-2014
 * @purpose This class is used for an encryption or decryption of token.
 */
public class EncryptDecryptManager implements Serializable {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("EncryptDecryptManager");
    private byte[] encoded;

    /**
     *
     */
    public EncryptDecryptManager() {

        try {
            // Generate a temporary key. In practice, you would save this key.
            // See also Encrypting with DES Using a Pass Phrase.
            SecretKey key = KeyGenerator.getInstance("DES").generateKey();
            encoded = key.getEncoded();
        } catch (Exception e) {
            log4jLog.info(" EncryptDecryptManager " + e);
//            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public byte[] getKey() {
        try {
            // Generate a temporary key. In practice, you would save this key.
            // See also Encrypting with DES Using a Pass Phrase.
            SecretKey key = KeyGenerator.getInstance("DES").generateKey();
            encoded = key.getEncoded();
        } catch (Exception e) {
            log4jLog.info(" getKey " + e);
//            e.printStackTrace();
        }
        return this.encoded;
    }

    /**
     *
     * @param str
     * @param key
     * @return
     */
    public String encrypt(String str, byte[] key) {
        String encrypted = null;
        SecretKey myKey = new SecretKeySpec(key, "DES");
        // Create encrypter/decrypter class
        DesEncrypter encrypter = new DesEncrypter(myKey);

        encrypted = encrypter.encrypt(str);


        return FieldSenseUtils.replaceMe(encrypted);
    }

    /**
     *
     * @param str
     * @param key
     * @return
     */
    public String decrypt(String str, byte[] key) {
        String decrypted = null;
        SecretKey myKey = new SecretKeySpec(key, "DES");
        // Create encrypter/decrypter class
        DesEncrypter encrypter = new DesEncrypter(myKey);

        decrypted = encrypter.decrypt(FieldSenseUtils.replaceMeBack(str));

        return decrypted;
    }
}

class DesEncrypter {

    Cipher ecipher;
    Cipher dcipher;

    DesEncrypter(SecretKey key) {
        try {
            ecipher = Cipher.getInstance("DES");
            dcipher = Cipher.getInstance("DES");
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);

        } catch (javax.crypto.NoSuchPaddingException e) {
        } catch (java.security.NoSuchAlgorithmException e) {
        } catch (java.security.InvalidKeyException e) {
        }
    }

    public String encrypt(String str) {
        try {
            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");

            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            return new sun.misc.BASE64Encoder().encode(enc);
        } catch (javax.crypto.BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (java.io.IOException e) {
        }
        return null;
    }

    public String decrypt(String str) {
        try {
            // Decode base64 to get bytes
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);

            // Decode using utf-8
            return new String(utf8, "UTF8");
        } catch (javax.crypto.BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (java.io.IOException e) {
        }
        return null;
    }
}
