package tn.undefined.universalhaven.dto;

public class UserDto {
	long id;
	String login;
	public long getId() {
		return id;
	}
	
	public UserDto() {
		super();
	}
	

	public UserDto(String login) {
		super();
		this.login = login;
	}

	public UserDto(long id, String login) {
		super();
		this.id = id;
		this.login = login;
	}

	public void setId(long id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	

}
