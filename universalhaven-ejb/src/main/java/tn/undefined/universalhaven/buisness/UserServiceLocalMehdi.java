package tn.undefined.universalhaven.buisness;

import javax.ejb.Local;

import tn.undefined.universalhaven.enumerations.UserRole;

@Local
public interface UserServiceLocalMehdi {
	
	
	public UserRole authenticate(String username, String password) throws Exception;
	
}
