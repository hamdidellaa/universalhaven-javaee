package tn.undefined.universalhaven.util;

import javax.xml.bind.annotation.XmlRootElement;

import tn.undefined.universalhaven.entity.Mail;
import tn.undefined.universalhaven.enumerations.UserRole;
@XmlRootElement
public class MailParam {
	private Mail mail;
	private UserRole role;
	private String skill;
	private String country;
	public Mail getMail() {
		return mail;
	}
	public void setMail(Mail mail) {
		this.mail = mail;
	}
	public UserRole getRole() {
		return role;
	}
	public void setRole(UserRole role) {
		this.role = role;
	}
	public String getSkill() {
		return skill;
	}
	public void setSkill(String skill) {
		this.skill = skill;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
}
