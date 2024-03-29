package tn.undefined.universalhaven.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ejb.Stateless;

@Stateless
public class PasswordService {

	public static final String SALT = "hamdidellaa";
	
	public  String generateHash(String input) {
		StringBuilder hash = new StringBuilder();

		try {
			MessageDigest sha = MessageDigest.getInstance("MD5");
			byte[] hashedBytes = sha.digest(input.getBytes());
			char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
					'a', 'b', 'c', 'd', 'e', 'f' };
			for (int idx = 0; idx < hashedBytes.length; ++idx) {
				byte b = hashedBytes[idx];
				hash.append(digits[(b & 0xf0) >> 4]);
				hash.append(digits[b & 0x0f]);
			}
		} catch (NoSuchAlgorithmException e) {
			// handle error here.
		}

		return hash.toString();
	}
	
	
	public boolean verifPassword( String password , String hashPassword) {
		String saltedPassword = SALT + password;
		String hashedPassword = generateHash(saltedPassword);
		
		if(hashedPassword.equalsIgnoreCase(hashPassword))
			return true ;
		return false ;
		
	
	}

	
	
	

	
}
