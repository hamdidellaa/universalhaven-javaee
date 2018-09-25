package tn.undefined.universalhaven.buisness;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import tn.undefined.universalhaven.entity.CallForHelp;
import tn.undefined.universalhaven.entity.Camp;

@Stateful
public class CallForHelpService implements CallForHelpServiceLocal {
	@PersistenceContext(name = "universalhaven-ejb")
	private EntityManager em;

	@Override
	public Boolean endCallForHelp(CallForHelp callForHelp) {
		try {
			CallForHelp call = em.find(CallForHelp.class, callForHelp.getId());
			em.remove(call);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override // change the name of this
	public List<CallForHelp> listActiveEvents() {
		return em.createQuery("SELECT cfh FROM CallForHelp cfh WHERE active=1", CallForHelp.class).getResultList();

	}

	@Override
	public Set<CallForHelp> listEventsByCamp(Camp camp) {
		em.find(Camp.class, camp.getId());
		return camp.getCallForHelpEvents();
	}

	@Override
	public Boolean startCallForHelp(CallForHelp callforhelp) {
		em.persist(callforhelp);

		return true;

	}

	@Override
	public Boolean modifyCallForHelp(CallForHelp callForHelp) {
		try {
			em.merge(callForHelp);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<CallForHelp> listEvents() {
		return em.createQuery("SELECT cfh FROM CallForHelp cfh ORDER BY cfh.creationDate DESC ", CallForHelp.class).getResultList();

	}

	@Override
	public List<CallForHelp> findCallForHelpByTitle(String title) {
		try {
			return (List<CallForHelp>) (em.createQuery("Select c from CallForHelp c where  c.title = :title")
					.setParameter("title", title).getResultList());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<CallForHelp> findCallForHelpByDescription(String description) {
		try {
			return (List<CallForHelp>) (em
					.createQuery("Select c from CallForHelp c where (0 < LOCATE(:description, c.description ))")
					.setParameter("description", description).getResultList());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


}
