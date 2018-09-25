package tn.undefined.universalhaven.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import tn.undefined.universalhaven.enumerations.Urgency;

@Entity
@XmlRootElement
/*la 1ere requette par defaut se fait avec eager
 Lazy s'execute à la demande .EX: .getCamp().getId() */
public class FundraisingEvent implements Serializable {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
	
	private String title;
	
	private String description;
	
	private double goal;
	@Temporal(TemporalType.DATE)
	private Date publishDate= new Date();
	@Enumerated(EnumType.STRING)
	private Urgency urgency ;
	@Temporal(TemporalType.DATE)
	private Date finishingDate= null;
	
	private String imagePath;
	@OneToMany(mappedBy="fundraisingEvent" , fetch = FetchType.EAGER)
	private List<Donation> donations;
	
	@ManyToOne/*(fetch = FetchType.LAZY)*/
	private User publisher;
	private String state;
	@ManyToOne /*(fetch = FetchType.LAZY)*/
	private Camp camp;

	public FundraisingEvent() {
		super();
	}
	
	public FundraisingEvent(long id) {
		super();
		this.id = id;
	}
	

	public FundraisingEvent(String title, String description, double goal, Date publishDate, Urgency urgency,
			Date finishingDate, String imagePath, User publisher, String state, Camp camp) {
		super();
		this.title = title;
		this.description = description;
		this.goal = goal;
		this.publishDate = publishDate;
		this.urgency = urgency;
		this.finishingDate = finishingDate;
		this.imagePath = imagePath;
		this.publisher = publisher;
		this.state = state;
		this.camp = camp;
	}

	public Camp getCamp() {
		return camp;
	}
	public void setCamp(Camp camp) {
		this.camp = camp;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public long getId() {
		return id;
	}
	@XmlAttribute(name="id",required=true)
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getGoal() {
		return goal;
	}
	public void setGoal(double goal) {
		this.goal = goal;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	
	public Urgency getUrgency() {
		return urgency;
	}
	public void setUrgency(Urgency urgency) {
		this.urgency = urgency;
	}
	public Date getFinishingDate() {
		return finishingDate;
	}
	public void setFinishingDate(Date finishingDate) {
		this.finishingDate = finishingDate;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public User getPublisher() {
		return publisher;
	}
	public void setPublisher(User publisher) {
		this.publisher = publisher;
	}
	public List<Donation> getDonations() {
		return donations;
	}
	public void setDonations(List<Donation> donations) {
		this.donations = donations;
	}
	
	
	
	
	
	

	
	
	
	
	
	
	
	
}	
