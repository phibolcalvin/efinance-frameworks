package org.seuksa.frmk.tools.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author prasnar
 * @version $Revision$
 */
public class CryptoHelper {

    private final static String KEY_ALGORITHM = "PBEWithMD5AndDES";
    private final static String CIPHER_ALGORITHM = KEY_ALGORITHM + "/CBC/PKCS5Padding";
    private static Logger logger = Logger.getLogger(CryptoHelper.class.getName());

    private static byte[] salt = {
        (byte) 0xA9,
        (byte) 0x9B,
        (byte) 0xC8,
        (byte) 0x32,
        (byte) 0x56,
        (byte) 0x35,
        (byte) 0xE3,
        (byte) 0x03
    };

    private static Cipher ecipher;
    private static Cipher dcipher;
    private static SecretKey masterKey = null;
    private static String masterPwdInit = null;

    private static int iterationCount = 19;

    /**
     * 
     */
    private CryptoHelper() {

    }

    /**
     * 
     * @param pass
     */
    public static void init(final char[] pass) {
        try {
            masterPwdInit = String.valueOf(pass);
            // Create the key
            final PBEParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
            masterKey = SecretKeyFactory.getInstance(KEY_ALGORITHM).generateSecret(new PBEKeySpec(pass));
            ecipher = Cipher.getInstance(CIPHER_ALGORITHM);
            dcipher = Cipher.getInstance(CIPHER_ALGORITHM);

            // Prepare the parameter to the ciphers
            // AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
            // iterationCount);
            ecipher.init(Cipher.ENCRYPT_MODE, masterKey, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, masterKey, paramSpec);

        }
        catch (final Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 
     * @param in
     * @return
     * @throws Exception
     */
    public static synchronized String decrypt(final String in) {
        try {
            // Decode base64 to get bytes
            final byte[] dec = new BASE64Decoder().decodeBuffer(in);
            // Decrypt
            final byte[] utf8 = dcipher.doFinal(dec);
            // Decode using utf-8
            return new String(utf8, "UTF8");
        }
        catch (final Exception e) {
            // throw new SecurityException("Could not decrypt: " +
            // e.getMessage());
            logger.warn("Could not decrypt: " + in);
        }
        return null;
    }

    /**
     * 
     * @param in
     * @return
     * @throws Exception
     */
    public static synchronized String encrypt(final String in) {
        try {

            // Encode the string into bytes using utf-8
            final byte[] utf8 = in.getBytes("UTF8");
            // Encrypt
            final byte[] enc = ecipher.doFinal(utf8);
            // Encode bytes to base64 to get a string
            return new BASE64Encoder().encode(enc);
        }
        catch (final Exception e) {
            // throw new SecurityException("Could not encrypt: " +
            // e.getMessage());
            logger.warn("Could not encrypt: " + in);
        }
        return null;
    }

    /**
     * @return the masterPwdInit
     */
    public static String getMasterPwdInit() {
        return masterPwdInit;
    }

}
