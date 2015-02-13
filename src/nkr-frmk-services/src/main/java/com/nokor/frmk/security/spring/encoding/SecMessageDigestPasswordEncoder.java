package com.nokor.frmk.security.spring.encoding;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.util.Assert;

/**
 * see org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder
 * @author prasnar
 *
 */
public class SecMessageDigestPasswordEncoder implements PasswordEncoder, InitializingBean {
    
    private String algorithm;
    private int iterations = 1;
    private boolean encodeHashAsBase64 = false;
    
    /**
	 * 
	 * @param algorithm
	 */
	public SecMessageDigestPasswordEncoder(String algorithm) {
		this.algorithm = algorithm;
		Assert.hasText(algorithm, "An algorithm must be set");
	}
	
    @Override
	public void afterPropertiesSet() throws Exception {
        Assert.hasText(algorithm, "An algorithm must be set");
	}
    
    /**
     * 
     * @param algorithm
     */
//    public void setAlgorithm(String algorithm) {
//        this.algorithm = algorithm;
//    }
    

    /**
	 * @return the algorithm
	 */
	public String getAlgorithm() {
		return algorithm;
	}

	/**
     * @see org.springframework.security.authentication.encoding.PasswordEncoder#isPasswordValid(java.lang.String, java.lang.String, java.lang.Object)
     */
    @Override
    public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
        String pass1 = "" + encPass;
        String pass2 = encodePassword(rawPass, salt);

        return PasswordEncoderUtils.equals(pass1,pass2);
    }

    /**
     * @see org.springframework.security.authentication.encoding.PasswordEncoder#encodePassword(java.lang.String, java.lang.Object)
     */
    @Override
    public String encodePassword(String rawPass, Object salt) {
    	String saltedPass = mergePasswordAndSalt(rawPass, salt, false);

        MessageDigest messageDigest = getMessageDigest();

        byte[] digest = messageDigest.digest(Utf8.encode(saltedPass));

        // "stretch" the encoded value if configured to do so
        for (int i = 1; i < iterations; i++) {
            digest = messageDigest.digest(digest);
        }

        if (getEncodeHashAsBase64()) {
            return Utf8.decode(Base64.encode(digest));
        } else {
            return new String(Hex.encode(digest));
        }
    }
    
    
    /**
     * 
     * @param password
     * @param salt
     * @param strict
     * @return
     */
    protected String mergePasswordAndSalt(String password, Object salt, boolean strict) {
        if (password == null) {
            password = "";
        }

        if (strict && (salt != null)) {
            if ((salt.toString().lastIndexOf("{") != -1) || (salt.toString().lastIndexOf("}") != -1)) {
                throw new IllegalArgumentException("Cannot use { or } in salt.toString()");
            }
        }

        if ((salt == null) || "".equals(salt)) {
            return password;
        } else {
            return password + "{" + salt.toString() + "}";
        }
    }
    
    /**
     * 
     * @return
     * @throws IllegalArgumentException
     */
    protected final MessageDigest getMessageDigest() throws IllegalArgumentException {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm [" + algorithm + "]");
        }
    }


	/**
	 * @return the iterations
	 */
	public int getIterations() {
		return iterations;
	}


	/**
	 * @param iterations the iterations to set
	 */
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}


	/**
	 * @return the encodeHashAsBase64
	 */
	public boolean getEncodeHashAsBase64() {
		return encodeHashAsBase64;
	}


	/**
	 * @param encodeHashAsBase64 the encodeHashAsBase64 to set
	 */
	public void setEncodeHashAsBase64(boolean encodeHashAsBase64) {
		this.encodeHashAsBase64 = encodeHashAsBase64;
	}
}
