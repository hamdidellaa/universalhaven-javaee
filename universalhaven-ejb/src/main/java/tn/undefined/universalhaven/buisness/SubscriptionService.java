package tn.undefined.universalhaven.buisness;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.bytebuddy.TypeCache.SimpleKey;
import tn.undefined.universalhaven.entity.Subscription;

@Stateless
public class SubscriptionService implements  SubsriptionServiceLocal{
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public boolean addSubscription(Subscription subscription) {
		
		try {
			em.persist(subscription);
			return true;
		}
		catch (Exception e) {
			return false;
		}
		
	}

	@Override
	public boolean cancelSubscription(long sub_id) {
		
		try {
			Subscription sub = em.find(Subscription.class, sub_id);
			if (sub!=null) {
				sub.setCanceled(true);
				em.merge(sub);
			}
			return false;
		}
		catch(Exception e) {
			return false;
		}
		
	}
	
	
	@Override
	public Subscription getSubscriptionByEmail(String email) {
		Query query=  em.createQuery("Select s from Subscription s where s.email=:email and canceled!=:canceled");
		query.setParameter("email", email);
		query.setParameter("canceled", true);
		List<Subscription> res = query.getResultList();
		if ((res==null) || (res.isEmpty())) {
			return null;
		}
		return res.get(0);
	}
	
	@Override
	public Map<String, Long> getNumberOfSubscriptionsByPlan(){
		Query query = em.createQuery("SELECT count(s),s.plan from Subscription s group by s.plan");
		List<Object[]> res= query.getResultList();
		Map<String, Long> results = new HashMap<>();
		for (Object[] entry : res) {
			results.put((String) entry[1], (long) entry[0]); 
		}
		return results;
	}

	@Override
	public Subscription getById(long id) {
		return em.find(Subscription.class, id);
	}

	@Override
	public void notifyUser(String email,String token) {
		try{
	          String host ="smtp.gmail.com" ;
	          String user = "universalhaven.noreply@gmail.com";
	          String pass = "uhuhuhuh";
	          String to = email;
	          String from = "universalhaven.noreply@gmail.com";
	          String subject = "Unsubscription confirmation";
	          
	          String url = "http://localhost:18080/universalhaven-web/rest/subscription/unsubscribe?token="+
	        		  		token;
	          String messageText = "We are sorry to see you go ... <br/> Please follow this link "+url;
	          boolean sessionDebug = false;
	          Properties props = System.getProperties();
	          props.put("mail.smtp.starttls.enable", "true");
	          props.put("mail.smtp.host", host);
	          props.put("mail.smtp.port", "587");
	          props.put("mail.smtp.auth", "true");
	          props.put("mail.smtp.starttls.required", "true");

	          //java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
	          Session mailSession = Session.getDefaultInstance(props, null);
	          mailSession.setDebug(sessionDebug);
	          Message msg = new MimeMessage(mailSession);
	          
	          msg.setFrom(new InternetAddress(from));
	          InternetAddress[] address = {new InternetAddress(to)};
	          msg.setRecipients(Message.RecipientType.TO, address);
	          msg.setSubject(subject); msg.setSentDate(new Date());
	          //msg.setText(messageText);
	          msg.setContent(messageText, "text/html; charset=utf-8");
	         Transport transport=mailSession.getTransport("smtp");
	         transport.connect(host, user, pass);
	         transport.sendMessage(msg, msg.getAllRecipients());
	         transport.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
		
	
}
