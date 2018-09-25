package tn.undefined.universalhaven.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import tn.undefined.universalhaven.enumerations.UserRole;

@Entity
@DiscriminatorValue(value="user")
@XmlRootElement
public class User extends Person implements Serializable {
	private String email;
	private String address;
	private String login;
	private String password;
	@Temporal(TemporalType.TIMESTAMP)
	private Date subscriptionDate=new Date();
	private String skills;
	private String profession;
	private String motivation;
	private String picture;
	@Enumerated(EnumType.STRING)
	private UserRole role = UserRole.VOLUNTEER;
	private Boolean subscribed=false;
	
	private Boolean isActif=true;
	
	
	
	private String token;
	@XmlTransient
	@OneToMany(mappedBy="publisher",fetch=FetchType.LAZY)
	private List<FundraisingEvent> fundraisingEvents;
	
	
	
	
	@OneToOne(mappedBy="campManager",fetch=FetchType.EAGER)
	@XmlTransient
	private Camp managedCamp;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@XmlTransient
	private Camp assignedCamp;

	public User() {
		super();
		fundraisingEvents = new ArrayList<>();
		
	}
	
	
	public User(Long id, String name, String surname, Date birthDate, String country, String gender) {
		super(id, name, surname, birthDate, country, gender);
		// TODO Auto-generated constructor stub
	}


	public User(Long id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getSubscriptionDate() {
		return subscriptionDate.getTime();
	}

	public void setSubscriptionDate(Date subscriptionDate) {
		this.subscriptionDate = subscriptionDate;
	}

	public String getPicture() {
		return picture;
	}


	public void setPicture(String picture) {
		this.picture = picture;
	}


	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getMotivation() {
		return motivation;
	}

	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}
	@Enumerated(EnumType.STRING)
	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public Boolean getSubscribed() {
		return subscribed;
	}

	public void setSubscribed(Boolean subscribed) {
		this.subscribed = subscribed;
	}



	public Boolean getIsActif() {
		return isActif;
	}

	public void setIsActif(Boolean isActif) {
		this.isActif = isActif;
	}
	@XmlTransient
	public List<FundraisingEvent> getFundraisingEvents() {
		return fundraisingEvents;
	}

	public void setFundraisingEvents(List<FundraisingEvent> fundraisingEvents) {
		this.fundraisingEvents = fundraisingEvents;
	}
	
	@XmlTransient
	public Camp getManagedCamp() {
		return managedCamp;
	}

	public void setManagedCamp(Camp managedCamp) {
		this.managedCamp = managedCamp;
	}
	@XmlTransient
	public Camp getAssignedCamp() {
		return assignedCamp;
	}

	public void setAssignedCamp(Camp assignedCamp) {
		this.assignedCamp = assignedCamp;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
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
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} 
		
		if (login == null) {
			if (other.login != null)
				return false;
		}
		 if (login.equals(other.login) || email.equals(other.email)  )
			return true;
		return false ;
	
	}



	@Override
	public String toString() {
		return "User [email=" + email + ", address=" + address + ", login=" + login + ", password=" + password
				+ ", subscriptionDate=" + subscriptionDate + ", skills=" + skills + ", profession=" + profession
				+ ", motivation=" + motivation + ", role=" + role + ", subscribed=" + subscribed + ", isActif="
				+ isActif + "]";
	}
	
	
	

	
	
	
	
	
	

	
	
	
}
