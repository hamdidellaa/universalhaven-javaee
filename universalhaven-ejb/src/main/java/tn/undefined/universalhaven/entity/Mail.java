package tn.undefined.universalhaven.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement
public class Mail implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String subject;
	private String content;
	private String mailSender;
	private Date mailCreated = new Date();
	private boolean statusMail;
	
	public Date getMailCreated() {
		return mailCreated;
	}
	public void setMailCreated(Date mailCreated) {
		this.mailCreated = mailCreated;
	}
	public boolean isStatusMail() {
		return statusMail;
	}
	public void setStatusMail(boolean statusMail) {
		this.statusMail = statusMail;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMailSender() {
		return mailSender;
	}
	public void setMailSender(String mailSender) {
		this.mailSender = mailSender;
	}
	


	
}
