package tn.undefined.universalhaven.buisness;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import tn.undefined.universalhaven.entity.Camp;
import tn.undefined.universalhaven.entity.Mail;
import tn.undefined.universalhaven.entity.Refugee;
import tn.undefined.universalhaven.entity.Rooms;
import tn.undefined.universalhaven.entity.User;
import tn.undefined.universalhaven.enumerations.UserRole;

@Stateless
public class CampService implements CampServiceLocal, CampServiceRemote {
	@PersistenceContext
	private EntityManager em;

	@Override
	public boolean CreateCamp(Camp camp) {
		Query query = em.createQuery(
				"select count(u) from User u where assignedCamp is NULL and id = :idcamp and role = 'CAMP_MANAGER'");
		long useridcamp = camp.getCampManager().getId();
		query.setParameter("idcamp", useridcamp);
		long idcamp = (Long) query.getSingleResult();

		try {
			if (idcamp != 0) {
				em.persist(camp);
				User user = em.find(User.class, useridcamp);
				user.setAssignedCamp(camp);
				user.setManagedCamp(camp);
				em.merge(user);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean disbandCamp(long campid) {

		try {
			Camp camp = em.find(Camp.class, campid);
			camp.setClosingDate(new Date());
			camp.setCreationDate(null);
			em.merge(camp);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Camp> ListCamp() {
		TypedQuery<Camp> query = em.createQuery("select c from Camp c where closingDate is NULL", Camp.class);
		return query.getResultList();
	}

	@Override
	public Map<String, List<Camp>> ListCampPerCountry() {
		Map<String, List<Camp>> maaap = new HashMap<>();
		;
		TypedQuery<String> query = em.createQuery("select DISTINCT c.country from Camp c group by c.country ",
				String.class);
		for (String temp : query.getResultList()) {
			TypedQuery<Camp> camp = em.createQuery("select c from Camp c where country = :pay ", Camp.class);
			camp.setParameter("pay", temp);
			maaap.put(temp, camp.getResultList());

		}

		return maaap;
	}

	@Override
	public Map<String, Long> CountCampPerCountry() {
		Map<String, Long> maaap = new HashMap<>();
		Query query = em.createQuery("select count(c),c.country from Camp c group by c.country ");
		List<Object[]> results = query.getResultList();
		for (Object[] temp : results) {
			long count = (Long) temp[0];
			String country = (String) temp[1];
			maaap.put(country, count);
		}
		return maaap;
	}

	@Override
	public boolean updateCamp(Camp camp) {
		Query req = em.createQuery("select c.campManager from Camp c where c.id = :idcamp");
		req.setParameter("idcamp", camp.getId());
		User idoldcm = (User) req.getSingleResult();
		
		System.out.println(idoldcm.getId());
		Query query = em.createQuery("select count(u) from User u where id = :idcamp and role = 'CAMP_MANAGER'");
		long useridcamp = camp.getCampManager().getId();
		System.out.println(useridcamp+"this is the one that im looking for");
		query.setParameter("idcamp", useridcamp);
		long idcamp = (Long) query.getSingleResult();
		
		try {
			if (idcamp != 0) {
				em.merge(camp);
				User user = em.find(User.class, useridcamp);
				User olduser = em.find(User.class, idoldcm.getId());
				user.setAssignedCamp(camp);
				olduser.setAssignedCamp(null);
				em.merge(user);
				em.merge(olduser);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public List<User> findCampManager() {
		Query query = em.createQuery("select (u) from User u where assignedCamp = null and role = 'CAMP_MANAGER'");
		return query.getResultList();
	}
	
	public Camp  getCampById(long campid) {
			Camp camp = em.find(Camp.class, campid);
			return camp;
	}

	@Override
	public boolean activateCamp(long campid) {
		try {
			Camp camp = em.find(Camp.class, campid);
			camp.setClosingDate(null);
			camp.setCreationDate(new Date());
			em.merge(camp);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public long findcampid(long userid) {
		Query query = em.createQuery("select u.assignedCamp from User u where u.id =:iduser");
		query.setParameter("iduser", userid);
		Camp idcamp = (Camp) query.getSingleResult();
		return idcamp.getId();
	}

	@Override
	public List<Refugee> findallrefugees() {
		
		TypedQuery<Refugee> query = em.createQuery("select r from Refugee r", Refugee.class);
		System.out.println(query.getResultList());
		return query.getResultList();
	}

	@Override
	public boolean deleteRefugee(Refugee refu) {
		  try {  
			  Refugee refugee = em.find(Refugee.class, refu.getId());
				em.remove(refugee);
				return true; 
		    } 
		    catch (Exception e) { 
		      e.printStackTrace(); 
		      return false;  
		    } 
	}


	@Override
	public Refugee getRefugeeById(long refugeeid) {
		Refugee ref = em.find(Refugee.class, refugeeid);
		return ref;
	}
	
	@Override
	public Camp getCampByUser(long userid) {
		User user = em.find(User.class, userid);
		if ( user.getRole().equals(UserRole.CAMP_MANAGER) ) {
			return user.getManagedCamp();
		}
		else {
			return user.getAssignedCamp();
		}
		
	}

	@Override
	public List<User> getCampStaff(long campid) {
		User user = new User();
		Camp c = em.find(Camp.class, campid);
		Query q = em.createQuery("SELECT u from User u where u.assignedCamp=:camp or u.managedCamp=:camp");
		q.setParameter("camp", c);
		return q.getResultList();
	}

	@Override
	public boolean deleteRefugeeangular(long refu) {
		
			  try {  
				  Refugee refugee = em.find(Refugee.class, refu);
					em.remove(refugee);
					return true; 
			    } 
			    catch (Exception e) { 
			      e.printStackTrace(); 
			      return false;  
			    } 
		
	}
	public boolean CreateRoom(Rooms room) {
		try {
			em.persist(room);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
