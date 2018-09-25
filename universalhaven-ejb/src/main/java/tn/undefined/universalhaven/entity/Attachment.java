package tn.undefined.universalhaven.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@XmlRootElement
public class Attachment implements Serializable{

	@Id
	@GeneratedValue
	private int id ;
	
	private String name ; 
	
	
	@ManyToOne
	@XmlTransient
	private ApplicationForm applicationFrom ;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@XmlTransient
	public ApplicationForm getApplicationFrom() {
		return applicationFrom;
	}
	@XmlTransient
	public void setApplicationFrom(ApplicationForm applicationFrom) {
		this.applicationFrom = applicationFrom;
	}
	
}
