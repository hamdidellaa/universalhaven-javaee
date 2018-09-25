package tn.undefined.universalhaven.buisness;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import tn.undefined.universalhaven.entity.Camp;
import tn.undefined.universalhaven.entity.Resource;
import tn.undefined.universalhaven.entity.ResourcesHistory;
import tn.undefined.universalhaven.enumerations.ResourceHistoryType;
import tn.undefined.universalhaven.enumerations.ResourceType;

@Stateless
public class ResourceService implements ResourceServiceLocal {
	@PersistenceContext(name = "universalhaven-ejb")
	private EntityManager em;

	@Override
	public boolean addResource(Resource resource) {
		if (resource.getQuantity() < 0) {
			return false;
		} else {
			em.persist(resource);
			return true;
		}

	}

	// Unused Method
	@Override
	public boolean addResourceToCamp(Resource resource, int campId) {
		try {
			Camp camp = em.find(Camp.class, campId);
			resource.setCamp(camp);
			if (resource.getQuantity() < 0) {
				return false;
			} else {
				em.persist(resource);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public ResourcesHistory depositResource(Resource resource) {
		try {
			ResourcesHistory history = new ResourcesHistory();
			Resource tmpRes = em.find(Resource.class, resource.getId());
			history.setDate(new Date());
			history.setResource(resource);
			System.out.println("**** before " + tmpRes.getQuantity());
			if (tmpRes.getQuantity() == resource.getQuantity()) {
				return null;
			}
			if (resource.getQuantity() > tmpRes.getQuantity()) {
				System.out.println("it is a deposit method");
				history.setType(ResourceHistoryType.DEPOSIT);
				history.setQuantity(resource.getQuantity() - tmpRes.getQuantity());

			} else if (resource.getQuantity() < tmpRes.getQuantity()) {
				System.out.println("it is a withdrawal method");
				history.setType(ResourceHistoryType.WITHDRAWAL);
				history.setQuantity(tmpRes.getQuantity() - resource.getQuantity());
			}
			em.merge(resource);

			System.out.println("**** after " + resource.getQuantity());

			return history;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public List<Resource> getCampResources() {
		return em.createQuery("SELECT r FROM Resource r").getResultList();

	}

	// Unused Method
	@Override
	public boolean withdrawResource(Resource resource, Double qte) {
		try {
			em.find(Resource.class, resource.getId());
			resource.setQuantity(resource.getQuantity() - qte);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public Boolean removeResource(long id) {
		try {
			Resource res = em.find(Resource.class, id);
			/*Resource restmp = em.find(Resource.class, id);
			em.createQuery("DELETE FROM ResourcesHistory ResHis WHERE ResHis.resource =:res ")
					.setParameter("res", restmp).executeUpdate();*/
			em.remove(res);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public Map<String, Double> getQuantityByType() {
		List<Object[]> query = em.createQuery("SELECT SUM(r.quantity), type FROM Resource r GROUP BY r.type")
				.getResultList();
		Map<String, Double> results = new HashMap<>();
		for (Object[] row : query) {
			results.put(((ResourceType) row[1]).toString(), (Double) row[0]);
		}
		return results;
	}

	@Override
	public boolean modifyResource(Resource resource) {
		try {
			
			/*Resource res = em.find(Resource.class, resource.getId());
			System.out.println("before"+res.getId());
			res = resource;*/
			em.merge(resource);
			System.out.println("after"+resource);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	@Override
    public Resource FindResourceById(long id) {
        return em.find(Resource.class, id);
    }

}
