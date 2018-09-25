package tn.undefined.universalhaven.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Donation implements Serializable{
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	
	private long id;
	
	private double amount;
	
	private String contributorName;
	
	private String contributorEmail;
	
	private Date donationDate= new Date();
	
	private String contributorAddress;
	
	private String country;
	
	private String transactionReference;
	
	private String paymentMethod;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	private FundraisingEvent fundraisingEvent;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getContributorName() {
		return contributorName;
	}
	public void setContributorName(String contributorName) {
		this.contributorName = contributorName;
	}
	public String getContributorEmail() {
		return contributorEmail;
	}
	public void setContributorEmail(String contributorEmail) {
		this.contributorEmail = contributorEmail;
	}
	public Date getDonationDate() {
		return donationDate;
	}
	public void setDonationDate(Date donationDate) {
		this.donationDate = donationDate;
	}
	public String getContributorAddress() {
		return contributorAddress;
	}
	public void setContributorAddress(String contributorAddress) {
		this.contributorAddress = contributorAddress;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public FundraisingEvent getFundraisingEvent() {
		return fundraisingEvent;
	}
	public void setFundraisingEvent(FundraisingEvent fundraisingEvent) {
		this.fundraisingEvent = fundraisingEvent;
	}
	
	
	
	
	
	
	
	public String getTransactionReference() {
		return transactionReference;
	}
	public void setTransactionReference(String transactionReference) {
		this.transactionReference = transactionReference;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	@Override
	public String toString() {
		return "Donation [id=" + id + ", amount=" + amount + ", contributorName=" + contributorName
				+ ", contributorEmail=" + contributorEmail + ", donationDate=" + donationDate + ", contributorAddress="
				+ contributorAddress + ", country=" + country + ", fundraisingEvent=" + fundraisingEvent + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Donation other = (Donation) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
	
	
	
}
