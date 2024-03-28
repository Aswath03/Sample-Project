package com.cars4u.sample.common;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;

public class EncryptionUtils {

	private static final byte[] SECRET_KEY = generateAESKey();

	private static byte[] generateAESKey() {
		SecureRandom secureRandom = new SecureRandom();
		byte[] key = new byte[16]; // 16 bytes for AES-128
		secureRandom.nextBytes(key);
		return key;
	}

	public static String encrypt(Long id) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encryptedId = cipher.doFinal(String.valueOf(id).getBytes());
			return Base64.getUrlEncoder().encodeToString(encryptedId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Long decrypt(String encryptedId) {
		try {
			if (encryptedId.startsWith(",")) {
				encryptedId = encryptedId.substring(1);
			}
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY, "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] decryptedId = cipher.doFinal(Base64.getUrlDecoder().decode(encryptedId));
			return Long.parseLong(new String(decryptedId));
		} catch (IllegalArgumentException e) {
			// Invalid Base64 character
			e.printStackTrace();
		} catch (Exception e) {
			// Other exceptions
			e.printStackTrace();
		}
		return null;
	}
}
