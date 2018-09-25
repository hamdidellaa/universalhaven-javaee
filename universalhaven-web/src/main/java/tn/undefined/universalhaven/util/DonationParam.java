package tn.undefined.universalhaven.util;

import javax.ws.rs.QueryParam;
import javax.xml.bind.annotation.XmlRootElement;

import tn.undefined.universalhaven.entity.Donation;
@XmlRootElement
public class DonationParam {
	private Donation donation;
	private String method;
	private String token;
	private String creditCardType;
	private String creditCardNumber;
	private int expireMonth;
	private int expireYear;
	private String cvv2;
	public Donation getDonation() {
		return donation;
	}
	public void setDonation(Donation donation) {
		this.donation = donation;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCreditCardType() {
		return creditCardType;
	}
	public void setCreditCardType(String creditCardType) {
		this.creditCardType = creditCardType;
	}
	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	public int getExpireMonth() {
		return expireMonth;
	}
	public void setExpireMonth(int expireMonth) {
		this.expireMonth = expireMonth;
	}
	public int getExpireYear() {
		return expireYear;
	}
	public void setExpireYear(int expireYear) {
		this.expireYear = expireYear;
	}
	public String getCvv2() {
		return cvv2;
	}
	public void setCvv2(String cvv2) {
		this.cvv2 = cvv2;
	}
	
	
}
