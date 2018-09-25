package tn.undefined.universalhaven.buisness;

import java.util.List;

import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import tn.undefined.universalhaven.entity.User;
import tn.undefined.universalhaven.enumerations.UserRole;



@Stateless
public class UserServiceMehdi implements UserServiceLocalMehdi {
	
	@PersistenceContext
	EntityManager em;
	
	
	@Override
	public UserRole authenticate(String username, String password) throws Exception {
		Query query = em.createQuery("SELECT u from User u where u.login=:login "
				+ "and u.password=:password");
		query.setParameter("login", username);
		query.setParameter("password", password);
		
		List<User> results = query.getResultList();
		if (results.isEmpty()) {
			throw new Exception();
		}
		return results.get(0).getRole();
	}

}
