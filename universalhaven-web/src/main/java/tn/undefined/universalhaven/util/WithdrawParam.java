package tn.undefined.universalhaven.util;

import javax.xml.bind.annotation.XmlRootElement;

import tn.undefined.universalhaven.entity.Resource;
@XmlRootElement
public class WithdrawParam {
	private Resource resource;
	private long userId;
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	
}
