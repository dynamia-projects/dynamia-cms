/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package tools.dynamia.cms.payment

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Map.Entry

class PaymentUtils {

	static String md5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5")
            byte[] messageDigest = md.digest(input.bytes)
            BigInteger number = new BigInteger(1, messageDigest)
            String hashtext = number.toString(16)
            // Now we need to zero pad it if you actually want the full 32
			// chars.
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext
            }
			return hashtext
        } catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e)
        }
	}

    static boolean eq(Map<String, String> response, String key, String value) {
		String responseValue = response.get(key)
        if (responseValue != null) {
			return responseValue.equals(value)
        } else {
			return false
        }
	}

    static String mapToString(Map<String, String> response) {
		StringBuilder sb = new StringBuilder()
        for (Entry<String, String> entry : response.entrySet()) {
			sb.append(entry.key).append(": ").append(entry.value).append("\n")
        }
		return sb.toString()
    }

    static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256")
            byte[] messageDigest = md.digest(input.bytes)
            BigInteger number = new BigInteger(1, messageDigest)
            String hashtext = number.toString(16)
            // Now we need to zero pad it if you actually want the full 32
            // chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext
            }
            return hashtext
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e)
        }
    }
}
