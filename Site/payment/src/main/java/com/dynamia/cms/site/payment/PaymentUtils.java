package com.dynamia.cms.site.payment;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Map.Entry;

public class PaymentUtils {

	public static String md5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);
			// Now we need to zero pad it if you actually want the full 32
			// chars.
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean eq(Map<String, String> response, String key, String value) {
		String responseValue = response.get(key);
		if (responseValue != null) {
			return responseValue.equals(value);
		} else {
			return false;
		}
	}

	public static String mapToString(Map<String, String> response) {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> entry : response.entrySet()) {
			sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}
		return sb.toString();
	}

}
