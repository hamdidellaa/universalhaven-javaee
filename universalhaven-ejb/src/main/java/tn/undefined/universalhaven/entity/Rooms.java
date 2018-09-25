package tn.undefined.universalhaven.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement
public class Rooms implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long roomId;
	private int Type;
	private int beds;
	private double superficy;
	private String reference;
	private int remainingbeds;
	@ManyToOne
	@JoinColumn(name="campId")
	private Camp campId;
	
	public long getRoomId() {
		return roomId;
	}
	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}
	
	public int getType() {
		return Type;
	}
	public void setType(int type) {
		Type = type;
	}
	public int getBeds() {
		return beds;
	}
	public void setBeds(int beds) {
		this.beds = beds;
	}
	public double getSuperficy() {
		return superficy;
	}
	public void setSuperficy(double superficy) {
		this.superficy = superficy;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public int getRemainingbeds() {
		return remainingbeds;
	}
	public void setRemainingbeds(int remainingbeds) {
		this.remainingbeds = remainingbeds;
	}
	public Camp getCampId() {
		return campId;
	}
	public void setCampId(Camp campId) {
		this.campId = campId;
	}
	
	
	
}
