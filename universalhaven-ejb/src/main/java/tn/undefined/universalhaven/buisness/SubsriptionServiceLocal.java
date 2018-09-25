package tn.undefined.universalhaven.buisness;

import java.util.Map;

import javax.ejb.Local;

import tn.undefined.universalhaven.entity.Subscription;


@Local
public interface SubsriptionServiceLocal {
		
	
	public boolean addSubscription(Subscription subscription);
	
	public boolean cancelSubscription(long sub_id);

	public Subscription getSubscriptionByEmail(String email);

	public Map<String, Long> getNumberOfSubscriptionsByPlan();
	
	public Subscription getById(long id);
	
	public void notifyUser(String email,String token);
	
}
