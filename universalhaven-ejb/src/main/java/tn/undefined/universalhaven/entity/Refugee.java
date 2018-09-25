package tn.undefined.universalhaven.entity;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

import tn.undefined.universalhaven.enumerations.IdentityType;

@Entity
@DiscriminatorValue(value="refugee")
@XmlRootElement
public class Refugee extends Person{
	
	private IdentityType identityType;
	private String identityNumber;
	private String maritalStatus;
	private String emergencyContact;
	private String specialNeeds;
	
	@ManyToOne
	private Camp camp;
	
	
	
	@Enumerated(EnumType.STRING)
	public IdentityType getIdentityType() {
		return identityType;
	}
	public void setIdentityType(IdentityType identityType) {
		this.identityType = identityType;
	}
	
	public String getIdentityNumber() {
		return identityNumber;
	}
	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}
	
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	
	public String getEmergencyContact() {
		return emergencyContact;
	}
	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}
	
	public String getSpecialNeeds() {
		return specialNeeds;
	}
	public void setSpecialNeeds(String specialNeeds) {
		this.specialNeeds = specialNeeds;
	}
	public Camp getCamp() {
		return camp;
	}
	public void setCamp(Camp camp) {
		this.camp = camp;
	}
	
	
	
	
	
}
