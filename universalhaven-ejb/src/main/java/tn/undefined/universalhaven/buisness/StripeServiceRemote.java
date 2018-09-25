package tn.undefined.universalhaven.buisness;

import javax.ejb.Remote;

@Remote
public interface StripeServiceRemote {
	public String pay(String token, int amount, String nomPrenom) ;
	public String addSubscription(String plan, String email, String name, String token);
	public boolean cancelSubscription(String customer_id) ;
}
