package com.nokor.frmk.security.spring;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.crypto.keygen.KeyGenerators;

import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author prasnar
 *
 */
public class SecSaltGenerator {
	private static final String prefixSalt = "@$N%k&R!";
	private static final String SEP_TOKEN = "@";
	private static final int KEY_LENGTH = 15;

	
	/**
	 * 
	 * @return
	 */
	public static String generateSaltFromSecUserId(SecUser secUser) {
		if (secUser == null || secUser.getId() == null || secUser.getId() <= 0) {
			throw new UsernameNotFoundException("The user does not exist. It has no Id.");
		}
		byte[] saltByte1 = Utf8.encode(prefixSalt + secUser.getId() + SEP_TOKEN);
		byte[] saltByte2 = KeyGenerators.secureRandom(KEY_LENGTH).generateKey();
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write(saltByte1);
			outputStream.write(saltByte2);
		} catch (IOException e) {
			throw new IllegalStateException("Problem while writing with ByteArrayOutputStream", e);
		}
		byte[] saltByte = outputStream.toByteArray();
		
		return Utf8.decode(Base64.encode(saltByte));
	}
	
	/**
	 * KeyGenerators.string().generateKey(): 8 characters
	 * @return
	 */
	public static String generateSecureRandom() {
		byte[] saltByte = KeyGenerators.secureRandom(KEY_LENGTH).generateKey();
		return Utf8.decode(Base64.encode(saltByte));
	}
	
	
}
