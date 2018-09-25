package tn.undefined.universalhaven.buisness; 

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless; 
import javax.persistence.EntityManager; 
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import tn.undefined.universalhaven.entity.Camp;
import tn.undefined.universalhaven.entity.Mail;
import tn.undefined.universalhaven.entity.Refugee;
import tn.undefined.universalhaven.entity.User;
import tn.undefined.universalhaven.enumerations.UserRole;

@Stateless 
public class MailService implements MailServiceLocal,MailServiceRemote{ 
  @PersistenceContext 
  private EntityManager em;

@Override
public String getSubscribedUsers() {
	TypedQuery<String> query =  em.createQuery("select email from User where subscribed =1",String.class);
	 String listString = "";
	  for (String s : query.getResultList())
	    { listString += s + ",";
	    }
	  listString = listString.substring(0, listString.length() - 1);
	return listString;
}
@Override
public boolean contacterNous(Mail mail) {
		    try { 
		      em.persist(mail); 
		      return true; 
		    } 
		    catch (Exception e) { 
		      e.printStackTrace(); 
		      return false; 
		    } 
} 

@Override
public String sendMailPerSomthing(UserRole role, String country, String skill) {
	User user = new User();
	user.setRole(role);
	String roles =  user.getRole().toString() ;
	System.out.println(roles);
	TypedQuery<String> query= null;
	String listString = "";
	if(role != null){
		 query =  em.createQuery("select email from User where Role =:role",String.class);
		 query.setParameter("role", roles);
	}
	if(country != null){
		 query =  em.createQuery("select email from User where country =:country",String.class);
		 query.setParameter("country", country);
	}
	if(skill != null){
		 query =  em.createQuery("select email from User where skills =:skill",String.class);
		 query.setParameter("skill", skill);
	}
	 
	  for (String s : query.getResultList())
	    { listString += s + ",";
	    }
	  listString = listString.substring(0, listString.length() - 1);
	return listString;
}

@Override
public boolean deleteMail(Mail mail) {
		  try {  
			  Mail mails = em.find(Mail.class, mail.getId());
				em.remove(mails);
				return true; 
		    } 
		    catch (Exception e) { 
		      e.printStackTrace(); 
		      return false;  
		    } 
	}
@Override
public boolean sendMail(Mail mail) {
	// TODO Auto-generated method stub
	return false;
}
@Override
public boolean updatestatus(long mailid) {
	try {
		Mail mail = em.find(Mail.class, mailid);
		mail.setStatusMail(true);
		em.merge(mail);
		return true;
	} catch (Exception e) {
		e.printStackTrace();
		return false;
	}
}
@Override
public List<Mail> findmails() {
	TypedQuery<Mail> query = em.createQuery("select r from Mail r", Mail.class);
	System.out.println(query.getResultList());
	return query.getResultList();
} 
@Override
public boolean deleteMailangular(long mail) {
	
		  try {  
			  Mail mails = em.find(Mail.class, mail);
				em.remove(mails);
				return true; 
		    } 
		    catch (Exception e) { 
		      e.printStackTrace(); 
		      return false;  
		    } 
	
}
  
}
