package tn.undefined.universalhaven.dto;

public class CampDto {
	long id;
	String address;
	String country;
	public long getId() {
		return id;
	}
	
	public CampDto() {
		super();
	}

	public CampDto(String address) {
		super();
		this.address = address;
	}

	public CampDto(long id, String address) {
		super();
		this.id = id;
		this.address = address;
	}
	

	public CampDto(long id, String address, String country) {
		super();
		this.id = id;
		this.address = address;
		this.country = country;
	}

	public void setId(long id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	

}
