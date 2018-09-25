package tn.undefined.universalhaven.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;





@Entity
@XmlRootElement

public class ApplicationForm implements Serializable {
	

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String name;
	
	private String email;
	
	private String country;
	
	private String skills;
	
	private String gender;
	
	private String phoneNumber;
	
	private String picture;
	
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	@OneToMany(mappedBy="applicationFrom",fetch=FetchType.EAGER)
	
	private List<Attachment> attachments;
	
	private String godFatherEmail;
	
	private Boolean accepted= false;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date submissionDate = new Date();
	
	@ManyToOne(fetch=FetchType.EAGER)
	private User reviewer;

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getGodFatherEmail() {
		return godFatherEmail;
	}

	public void setGodFatherEmail(String godFatherEmail) {
		this.godFatherEmail = godFatherEmail;
	}

	public Boolean getAccepted() {
		return accepted;
	}

	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}

	public Date getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}

	public User getReviewer() {
		return reviewer;
	}

	public void setReviewer(User reviewer) {
		this.reviewer = reviewer;
	}
	
	public List<Attachment> getAttachments() {
		return attachments;
	}
	
	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		ApplicationForm other = (ApplicationForm) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ApplicationForm [id=" + id + ", name=" + name + ", email=" + email + ", country=" + country
				+ ", skills=" + skills + ", gender=" + gender + ", phoneNumber=" + phoneNumber + ", attachments="
				+ attachments + ", godFatherEmail=" + godFatherEmail + ", accepted=" + accepted + ", submissionDate="
				+ submissionDate + ", reviewer=" + reviewer + "]";
	}
	
	

	
}
