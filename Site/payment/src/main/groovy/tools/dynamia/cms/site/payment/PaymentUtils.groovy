/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.site.payment

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

}
