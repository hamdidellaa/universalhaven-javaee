package tn.undefined.universalhaven.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type")
@XmlRootElement
public class Person implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected long id;
	
	private String name;
	
	private String surname;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date birthDate;
	
	private String country;
	
	private String gender;
	
	
	public Person() {
		super();
	}
	
	public Person(Long id) {
		super();
		this.id = id;
	}
	

	public Person(Long id, String name, String surname, Date birthDate, String country, String gender) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.birthDate = birthDate;
		this.country = country;
		this.gender = gender;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public long getBirthDate() {
		return birthDate.getTime();
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
	
	
}
