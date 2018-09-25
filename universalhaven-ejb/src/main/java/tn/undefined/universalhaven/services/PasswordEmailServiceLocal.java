package tn.undefined.universalhaven.services;

import javax.ejb.Local;

@Local
public interface PasswordEmailServiceLocal {

	public  boolean SendMail(String to , String subject , String body);
	public  String generateHash(String input);
	public boolean verifPassword( String password , String hashPassword);
}
