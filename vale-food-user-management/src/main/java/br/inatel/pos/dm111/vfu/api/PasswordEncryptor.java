package br.inatel.pos.dm111.vfu.api;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptor {

	public String encrypt(String text) {
		MessageDigest crypt = null;
		try {
			crypt = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

		crypt.reset();
		crypt.update(text.getBytes(StandardCharsets.UTF_8));

		return new BigInteger(1, crypt.digest()).toString();
	}
}
