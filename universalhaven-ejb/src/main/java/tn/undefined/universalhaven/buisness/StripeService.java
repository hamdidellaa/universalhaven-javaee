package tn.undefined.universalhaven.buisness;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;

@Stateless
public class StripeService implements StripeServiceLocal, StripeServiceRemote{
	
	
	@PostConstruct
	public void init() {
		Stripe.apiKey="sk_test_wnmKqMazFr951HJfDovuPINB";
	}
	
	public String pay(String token, int amount, String nomPrenom) {
		
		amount =  amount * 100;
		
		// Charge the user's card:
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", amount);
		params.put("currency", "usd");
		params.put("description", "Donation from "+nomPrenom);
		params.put("source", token);

		try {
			Charge charge = Charge.create(params);
			return charge.getId();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		
	}
	
	public String addSubscription(String plan, String email, String name, String token) {
		
		Map<String,Object> params= new HashMap<>();
		params.put("email", email);
		params.put("description", name);
		params.put("card", token);
		try {
			Customer customer = Customer.create(params);
			Map<String,Object> subscription = new HashMap<>();
			subscription.put("plan", plan);
			
			customer.createSubscription(subscription);
			return customer.getId();
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (CardException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (APIException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return "";
	}
	
	
	public boolean cancelSubscription(String customer_id) {
		try {
			Customer c= Customer.retrieve(customer_id);
			c.cancelSubscription();
			return true;
		} catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException
				| APIException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return false;
	}
	
	
	
	
}
