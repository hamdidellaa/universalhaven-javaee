package tn.undefined.universalhaven.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import tn.undefined.universalhaven.enumerations.ResourceHistoryType;

@Entity
public class ResourcesHistory {
	private long id;
	private User user;
	private Resource resource;
	private double quantity;
	private Date date = new Date();
	private ResourceHistoryType type;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	@ManyToOne
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	@ManyToOne
	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	@Enumerated(EnumType.STRING)
	public ResourceHistoryType getType() {
		return type;
	}

	public void setType(ResourceHistoryType type) {
		this.type = type;
	}

	public ResourcesHistory(long id, double quantity) {
		super();
		this.id = id;
		this.quantity = quantity;
	}

	public ResourcesHistory() {
		super();
	}

	@Override
	public String toString() {
		return "ResourcesHistory:  [id=" + id + ", \r\n user=" + user.getId() + ", \r\n resource=" + resource + ", \r\n quantity=" + quantity
				+ ", date=" + date + ", type=" + type + "]";
	}

}
