/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.utils;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.log4j.Logger;

/**
 *
 * @author mayank
 */
public class FieldSensePasswordEncryptionDecryption {

    private static final int ITERATIONS = 1000;
    private static final int KEY_LENGTH = 70; // bits
    private static final String TOKEN = "QLC";
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger(" FieldSensePasswordEncryptionDecryption ");

    /**
     *
     * @param password
     * @return
     */
    public static String hashPassword(String password) {//-5b34029c6d12
        try {
            char[] passwordChars = password.toCharArray();
            byte[] saltBytes = TOKEN.getBytes();
            PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory key = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hashedPassword = key.generateSecret(spec).getEncoded();
            return String.format("%x", new BigInteger(hashedPassword));
        } catch (InvalidKeySpecException ex) {
            log4jLog.info(" hashPassword "+ex);
//            ex.printStackTrace();
            return "";
        } catch (NoSuchAlgorithmException ex) {
            log4jLog.info(" hashPassword "+ex);
//            ex.printStackTrace();
            return "";
        }
    }

    /**
     *
     * @param p
     */
    public static void main(String p[]) {
//        FieldSensePasswordEncryptionDecryption.decrypt(FieldSensePasswordEncryptionDecryption.encrypt("ramesh@123"));
//        System.out.println(hashPassword("ramesh@123"));//671ae87758d70c80
    }
//    private static final SecretKey secretKey = getSecretKey();
//
//    /**
//     * Generate an AES key using KeyGenerator
//     * @return
//     */
//    private static SecretKey getSecretKey() {
//        try {
//            //Set Algorithm and get the instance of KeyGenerator
//            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
//
//            //Initialize the keysize to 128
//            keyGen.init(128);
//
//            //Generate the Key
//            return keyGen.generateKey();
//        } catch (NoSuchAlgorithmException nae) {
//            return null;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public static String decrypt(String password) {
//        try {
//            //Create a Cipher by specifying the Algorithm name
//            Cipher aesCipher = Cipher.getInstance(ALGORITHM);
//
//            //Initialize the Cipher for Decryption
//            aesCipher.init(Cipher.DECRYPT_MODE, secretKey, aesCipher.getParameters());
//
//            ////Decode the data using Base64 decoder
//            byte[] byteBase64 = new BASE64Decoder().decodeBuffer(password);
//
//            //Decrypt the cipher bytes using doFinal method
//            byte[] byteDecryptedText = aesCipher.doFinal(byteBase64);
//
//            //Decrypt the bytes to encoded string
//            String strDecryptedText = new String(byteDecryptedText);
//
//            //Remove the token to password
//            String decryptedPass = strDecryptedText.replace(TOKEN, "");
//            System.out.println(decryptedPass);
//            return decryptedPass;
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return "";
//        } catch (IllegalBlockSizeException ex) {
//            ex.printStackTrace();
//            return "";
//        } catch (BadPaddingException ex) {
//            ex.printStackTrace();
//            return "";
//        } catch (InvalidAlgorithmParameterException ex) {
//            ex.printStackTrace();
//            return "";
//        } catch (InvalidKeyException ex) {
//            ex.printStackTrace();
//            return "";
//        } catch (NoSuchAlgorithmException nae) {
//            nae.printStackTrace();
//            return "";
//        } catch (NoSuchPaddingException npe) {
//            npe.printStackTrace();
//            return "";
//        }
//    }
//
//    public static String encrypt(String password) {
//        try {
//            //Create a Cipher by specifying the Algorithm name
//            Cipher aesCipher = Cipher.getInstance(ALGORITHM);
//
//            //Initialize the Cipher for Encryption
//            aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
//
//            //Add token to password
//            String strDataToEncrypt = password + "" + TOKEN;
//
//            //Convert the Input Text to Bytes
//            byte[] byteDataToEncrypt = strDataToEncrypt.getBytes();
//
//            //Encrypt the bytes using doFinal method
//            byte[] byteCipherText = aesCipher.doFinal(byteDataToEncrypt);
//
//            //Encode the data using Base64 encoder
//            String strCipherText = new BASE64Encoder().encode(byteCipherText);
//            System.out.println(strCipherText);
//
//            return strCipherText;
//        } catch (IllegalBlockSizeException ex) {
//            ex.printStackTrace();
//            return "";
//        } catch (BadPaddingException ex) {
//            ex.printStackTrace();
//            return "";
//        } catch (InvalidKeyException ex) {
//            ex.printStackTrace();
//            return "";
//        } catch (NoSuchAlgorithmException nae) {
//            nae.printStackTrace();
//            return "";
//        } catch (NoSuchPaddingException npe) {
//            npe.printStackTrace();
//            return "";
//        }
//    }
//    public static void AEScrypt() {
//        String strDataToEncrypt = new String();
//        String strCipherText = new String();
//        String strDecryptedText = new String();
//
//        try {
//            /**
//             *  Step 1. Generate an AES key using KeyGenerator
//             *  		Initialize the keysize to 128
//             *
//             */
//            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
//            keyGen.init(128);
//            SecretKey key = getSecretKey();
//            /**
//             *  Step2. Create a Cipher by specifying the following parameters
//             * 			a. Algorithm name - here it is AES
//             */
//            Cipher aesCipher = Cipher.getInstance("AES");
//
//            /**
//             *  Step 3. Initialize the Cipher for Encryption
//             */
//            aesCipher.init(Cipher.ENCRYPT_MODE, key);
//
//            /**
//             *  Step 4. Encrypt the Data
//             *  		1. Declare / Initialize the Data. Here the data is of type String
//             *  		2. Convert the Input Text to Bytes
//             *  		3. Encrypt the bytes using doFinal method
//             */
//            strDataToEncrypt = "mayank";//hu3jOp5Zvgh8k7Go7/5yaQ==
//            byte[] byteDataToEncrypt = strDataToEncrypt.getBytes();
//            byte[] byteCipherText = aesCipher.doFinal(byteDataToEncrypt);
//            strCipherText = new BASE64Encoder().encode(byteCipherText);
////            strCipherText = byteCipherText.toString();
//            System.out.println("Cipher Text generated using AES is " + strCipherText);
//
//            /**
//             *  Step 5. Decrypt the Data
//             *  		1. Initialize the Cipher for Decryption
//             *  		2. Decrypt the cipher bytes using doFinal method
//             */
//            aesCipher.init(Cipher.DECRYPT_MODE, key, aesCipher.getParameters());
//            byte[] byteBase64 = new BASE64Decoder().decodeBuffer(strCipherText);
//            byte[] byteDecryptedText = aesCipher.doFinal(byteBase64);
//            strDecryptedText = new String(byteDecryptedText);
//            System.out.println(" Decrypted Text message is " + strDecryptedText);
//
//        } catch (IOException ex) {
//            Logger.getLogger(FieldSensePasswordEncryptionDecryption.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NoSuchAlgorithmException noSuchAlgo) {
//            System.out.println(" No Such Algorithm exists " + noSuchAlgo);
//        } catch (NoSuchPaddingException noSuchPad) {
//            System.out.println(" No Such Padding exists " + noSuchPad);
//        } catch (InvalidKeyException invalidKey) {
//            System.out.println(" Invalid Key " + invalidKey);
//        } catch (BadPaddingException badPadding) {
//            System.out.println(" Bad Padding " + badPadding);
//        } catch (IllegalBlockSizeException illegalBlockSize) {
//            System.out.println(" Illegal Block Size " + illegalBlockSize);
//        } catch (InvalidAlgorithmParameterException invalidParam) {
//            System.out.println(" Invalid Parameter " + invalidParam);
//        }
//    }
//    public static final int SALT_LENGTH = 16;
//    public static final String ALGORITHM = "RC2";
//    public static final int ITERATION_COUNT = 8192;
//    public static final int KEY_SIZE = 160;
//
//    public static byte[] nextSalt() throws NoSuchAlgorithmException {
//        byte[] salt = new byte[SALT_LENGTH];
//        SecureRandom sr = SecureRandom.getInstance(ALGORITHM);
//        sr.nextBytes(salt);
//        return salt;
//    }
//
//    public static byte[] generateSalt() {
//        SecureRandom random = new SecureRandom();
//        byte bytes[] = new byte[20];
//        random.nextBytes(bytes);
//        return bytes;
//    }
//
//    public static void salt() {
//        try {
//            // Initialize a secure random number generator
//            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
//
//            // Method 1 - Calling nextBytes method to generate Random Bytes
//            byte[] bytes = new byte[512];
//            secureRandom.nextBytes(bytes);
//
//            // Printing the SecureRandom number by calling secureRandom.nextDouble()
//            System.out.println(" Secure Random # generated by calling nextBytes() is " + secureRandom.nextDouble());
//
//            // Method 2 - Using setSeed(byte[]) to reseed a Random object
//            int seedByteCount = 10;
//            byte[] seed = secureRandom.generateSeed(seedByteCount);
//
//            // TBR System.out.println(" Seed value is " + new BASE64Encoder().encode(seed));
//
//            secureRandom.setSeed(seed);
//
//            System.out.println(" Secure Random # generated using setSeed(byte[]) is  " + secureRandom.nextDouble());
//
//        } catch (NoSuchAlgorithmException noSuchAlgo) {
//            System.out.println(" No Such Algorithm exists " + noSuchAlgo);
//        }
//    }
//
//    public static byte[] hashPassword(char[] password, byte[] salt)
//            throws GeneralSecurityException {
//        return hashPassword(password, salt, ITERATION_COUNT, KEY_SIZE);
//    }
//
//    public static byte[] hashPassword(char[] password, byte[] salt, int iterationCount, int keySize) throws GeneralSecurityException {
//        PBEKeySpec spec = new PBEKeySpec(password, salt, iterationCount, keySize);
//        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
//        return factory.generateSecret(spec).getEncoded();
//    }
}
