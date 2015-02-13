/**
 * 
 */
package com.nokor.frmk.security.spring.encoding;

import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.util.Assert;

/**
 * @author prasnar
 *
 */
public class MyMessageDigestPasswordEncoder extends MessageDigestPasswordEncoder {

	/**
	 * 
	 * @param algorithm
	 */
	public MyMessageDigestPasswordEncoder(String algorithm) {
		super(algorithm);
		Assert.hasText(algorithm, "An algorithm must be set");
	}

	/**
	 * @see org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder#encodePassword(java.lang.String, java.lang.Object)
	 */
	@Override
	public String encodePassword(String rawPass, Object salt) {
		 String encodedPwd = super.encodePassword(rawPass, salt);
		 return encodedPwd;
	 }
	
	/**
	 * @see org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder#setIterations(int)
	 */
	@Override
	public void setIterations(int iterations) {
		super.setIterations(iterations);
	}
}
