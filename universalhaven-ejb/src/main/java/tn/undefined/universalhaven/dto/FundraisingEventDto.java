package tn.undefined.universalhaven.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import tn.undefined.universalhaven.enumerations.Urgency;

public class FundraisingEventDto implements Serializable {
	private long id;
	private String title;
	private String description;
	private double goal;
	private Date publishDate= new Date();
	private Urgency urgency ;
	private Date finishingDate= null;
	private String imagePath;
	private String state;
	private CampDto camp;
	private UserDto user;
	
	public FundraisingEventDto() {
		super();
	}
	public FundraisingEventDto(long id, String title, String description, double goal, Date publishDate,
			Urgency urgency, Date finishingDate, String imagePath, String state) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.goal = goal;
		this.publishDate = publishDate;
		this.urgency = urgency;
		this.finishingDate = finishingDate;
		this.imagePath = imagePath;
		this.state = state;
	}
	
	public FundraisingEventDto(long id, String title, String description, double goal, Date publishDate,
			Urgency urgency, Date finishingDate, String imagePath, String state, CampDto camp,UserDto user) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.goal = goal;
		this.publishDate = publishDate;
		this.urgency = urgency;
		this.finishingDate = finishingDate;
		this.imagePath = imagePath;
		this.state = state;
		this.camp = camp;
		this.user = user;
	}
	public long getId() {
		return id;
	}
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public CampDto getCamp() {
		return camp;
	}
	public void setCamp(CampDto camp) {
		this.camp = camp;
	}
	public UserDto getUser() {
		return user;
	}
	public void setUser(UserDto user) {
		this.user = user;
	}
	
	
	

}
