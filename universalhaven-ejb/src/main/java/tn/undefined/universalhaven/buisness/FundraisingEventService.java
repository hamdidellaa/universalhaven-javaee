package tn.undefined.universalhaven.buisness;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import tn.undefined.universalhaven.dto.CampDto;
import tn.undefined.universalhaven.dto.FundraisingEventDto;
import tn.undefined.universalhaven.dto.UserDto;
import tn.undefined.universalhaven.entity.FundraisingEvent;
import tn.undefined.universalhaven.entity.User;
import tn.undefined.universalhaven.enumerations.Urgency;
@Stateless
public class FundraisingEventService implements FundraisingEventServiceLocal,FundraisingEventServiceRemote {

	@PersistenceContext
	private EntityManager em;
	public Map<String, Double> getAverageCompletionDate() {
		Query query = em.createQuery("SELECT c.country,AVG(DATEDIFF(f.finishingDate,f.publishDate)) from FundraisingEvent f join f.camp c" + " where f.state='Finished' GROUP BY c.country ");
		List<Object[]> results = query.getResultList();
		Map<String, Double> resultMap = new HashMap<>();
		for (Object[] result : results) {
			resultMap.put((String) result[0], (Double) (result[1]));
			System.out.println("camp: "+(String) result[0]+" avg of completing events by day(s): "+(Double) (result[1]));
		}
		return resultMap ;
		
	}

	@Override
	public Map<String, Long> getEventCountByCountry() {
		// TODO Auto-generated method stub
//		Query query = em.createQuery("SELECT f.camp.address,Count(f.id) from FundraisingEvent f" + " GROUP BY f.camp.adrress");
		Query query = em.createQuery("SELECT c.country,count(c.id) from FundraisingEvent f join f.camp c" + " GROUP BY c.country ");
		List<Object[]> results = query.getResultList();
		Map<String,Long> resultMap = new HashMap<>();
		for (Object[] result : results) {
			resultMap.put((String) result[0], (Long) (result[1]));
			System.out.println("country: "+(String) result[0]+" number of events: "+(Long) (result[1]));
		}
		return resultMap;
	}

	@Override
	public List<FundraisingEvent> listActiveEvents() {
		List<FundraisingEvent> liste = ((List<FundraisingEvent>) em
				.createQuery("from FundraisingEvent ").getResultList());
		for(int i = 0 ; i< liste.size() ; i++){
			/*System.out.println(liste.get(i).getId());
			System.out.println(liste.get(i).getCamp().getAddress());*/
			//getPublisher().getId() :null pointer exception
			//System.out.println(liste.get(i).getPublisher().getId());
		}
		return liste;
//		List<FundraisingEvent> ls=new ArrayList<FundraisingEvent>();
//		TypedQuery<FundraisingEvent> query=em.createQuery("select f from FundraisingEvent f",FundraisingEvent.class);
//		for(FundraisingEvent f:(List<FundraisingEvent>) query.getResultList()){
//			f.setCamp(null);
//			f.setPublisher(null);
//			ls.add(f);
//		}
//		return ls;
	}

	@Override
	public List<FundraisingEvent> listEventsByUser(User user) {/*Long idUser*/
		Query query=em
				.createQuery("select f from FundraisingEvent f where f.publisher.id=:idUser ");
		query.setParameter("idUser", user.getId());
		List<FundraisingEvent> l=query.getResultList();
		for(int i = 0 ; i< l.size() ; i++){
			System.out.println(l.get(i).getPublisher().getId());
			System.out.println(l.get(i).getTitle());
		}
		return l;
	}

	@Override
	public boolean startEvent(FundraisingEvent event){
		  try {
			event.setState("In progress");
			em.persist(event);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		 
	}

	@Override
	public boolean updateEvent(FundraisingEvent event) {
		
		try {
			em.merge(event);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public void changeEventState(FundraisingEvent event) {
		TypedQuery<Double> query=em.createQuery("select sum(d.amount) from Donation d  where d.fundraisingEvent=:event",Double.class);
		query.setParameter("event", event);
		double sumAmount=query.getSingleResult();
		TypedQuery<Double> query2=em.createQuery("select goal from FundraisingEvent f  where f.id=:idEvent",Double.class);
		query2.setParameter("idEvent", event.getId());
		double goal=query2.getSingleResult();
		System.out.println("sum amount: "+sumAmount);
		System.out.println("goal: "+goal);
		if(goal<=sumAmount){
			System.out.println("wselna lil goal");
			 /*Query q=em.createQuery("UPDATE FundraisingEvent f set f.state=Finished where f.id=:idEvent ");
			 q.setParameter("idEvent",event.getId());*/
			Query q = em
					.createQuery("UPDATE FundraisingEvent f set f.state= 'Finished',f.finishingDate=?1 "
					+ "WHERE f.id=:idEvent").setParameter(1,new Date());
					q.setParameter("idEvent", event.getId());
					int updateCount = q.executeUpdate();
		}
		else System.out.println("mezelna mawselnech lil goal");		
	}

	@Override
	public List<FundraisingEventDto> listActiveEventsDto() {
		List<FundraisingEventDto> listeDto = new ArrayList<>();
		List<FundraisingEvent> liste = ((List<FundraisingEvent>) em
				.createQuery("from FundraisingEvent ").getResultList());
		for(int i = 0 ; i< liste.size() ; i++){
			System.out.println(liste.get(i).getId());
			UserDto u=new UserDto(liste.get(i).getPublisher().getId(),liste.get(i).getPublisher().getLogin());
			CampDto c=new CampDto(liste.get(i).getCamp().getId(), liste.get(i).getCamp().getAddress(),liste.get(i).getCamp().getCountry());
			listeDto.add(new FundraisingEventDto(liste.get(i).getId(), liste.get(i).getTitle(),
					liste.get(i).getDescription(), liste.get(i).getGoal(),liste.get(i).getPublishDate(),
					liste.get(i).getUrgency(), liste.get(i).getFinishingDate(),
					liste.get(i).getImagePath(), liste.get(i).getState(),c,u));
		}
		
		return listeDto;
	}

	@Override
	public List<FundraisingEventDto> listEventsByUserDto(User user) {
		List<FundraisingEvent> liste= listEventsByUser(user);
		List<FundraisingEventDto> l=new ArrayList<FundraisingEventDto>();
		for(FundraisingEvent f:liste){
			UserDto u=new UserDto(f.getPublisher().getId(),f.getPublisher().getLogin());
			CampDto c=new CampDto(f.getCamp().getId(),f.getCamp().getAddress());
			l.add(new FundraisingEventDto(f.getId(),f.getTitle(),
					f.getDescription(),f.getGoal(),f.getPublishDate(),
					f.getUrgency(),f.getFinishingDate(),
					f.getImagePath(),f.getState(),c,u));
		}
		return l;
	}

	/*@Override
	public double getSumAmountByEvent(FundraisingEvent event) {
		// TODO Auto-generated method stub
		TypedQuery<Double> query=em.createQuery("select sum(d.amount) from Donation d  where d.fundraisingEvent=:event",Double.class);
		query.setParameter("event", event);
		double result=query.getSingleResult();
		System.out.println("somme: "+result);
		return result;
	}*/

	@Override
	public Map<String, Long> getCountEventByMonth(int year) {
		Query query = em.createQuery("SELECT MONTHNAME(f.publishDate),count(f.id),extract(month from f.publishDate )from FundraisingEvent f "
		+ "WHERE extract(year from f.publishDate )=:year GROUP BY MONTHNAME(f.publishDate),extract(month from f.publishDate ) ORDER by extract(month from f.publishDate ) ");
		query.setParameter("year", year);
		List<Object[]> results = query.getResultList();
		Map<String, Long> resultMap = new HashMap<>();
		for (Object[] result : results) {
			resultMap.put((String) result[0], (Long) (result[1]));
			System.out.println("month: "+(String) result[0]+" number of event(s):  "+(Long) (result[1]));
		}
		return resultMap ;
	}

	@Override
	public List<FundraisingEventDto> listEventsByState(String state) {
		Query query=em
				.createQuery("select f from FundraisingEvent f where f.state=:state");
		query.setParameter("state", state);
		List<FundraisingEvent> l=query.getResultList();
		List<FundraisingEventDto> lDto=new ArrayList<FundraisingEventDto>();
		for(FundraisingEvent f:l){
			UserDto u=new UserDto(f.getPublisher().getId(),f.getPublisher().getLogin());
			CampDto c=new CampDto(f.getCamp().getId(),f.getCamp().getAddress(),f.getCamp().getCountry());
			lDto.add(new FundraisingEventDto(f.getId(),f.getTitle(),
					f.getDescription(),f.getGoal(),f.getPublishDate(),
					f.getUrgency(),f.getFinishingDate(),
					f.getImagePath(),f.getState(),c,u));
		}
		return lDto;
	}

	@Override
	public List<FundraisingEventDto> listEventsByUrgency(Urgency urgency) {
		Query query=em
				.createQuery("select f from FundraisingEvent f where f.urgency=:urgency");
		query.setParameter("urgency", urgency);
		List<FundraisingEvent> l=query.getResultList();
		List<FundraisingEventDto> lDto=new ArrayList<FundraisingEventDto>();
		for(FundraisingEvent f:l){
			UserDto u=new UserDto(f.getPublisher().getId(),f.getPublisher().getLogin());
			CampDto c=new CampDto(f.getCamp().getId(),f.getCamp().getAddress(),f.getCamp().getCountry());
			lDto.add(new FundraisingEventDto(f.getId(),f.getTitle(),
					f.getDescription(),f.getGoal(),f.getPublishDate(),
					f.getUrgency(),f.getFinishingDate(),
					f.getImagePath(),f.getState(),c,u));
		}
		return lDto;
	}

	@Override
	public List<FundraisingEventDto> listEventsByYearAndMonth(int month,int year) {
		Query query=em
				.createQuery("select f from FundraisingEvent f where extract(year from f.publishDate )=:year and extract(month from f.publishDate )=:month");
		query.setParameter("year", year);
		query.setParameter("month", month);
		List<FundraisingEvent> l=query.getResultList();
		List<FundraisingEventDto> lDto=new ArrayList<FundraisingEventDto>();
		for(FundraisingEvent f:l){
			UserDto u=new UserDto(f.getPublisher().getId(),f.getPublisher().getLogin());
			CampDto c=new CampDto(f.getCamp().getId(),f.getCamp().getAddress(),f.getCamp().getCountry());
			lDto.add(new FundraisingEventDto(f.getId(),f.getTitle(),
					f.getDescription(),f.getGoal(),f.getPublishDate(),
					f.getUrgency(),f.getFinishingDate(),
					f.getImagePath(),f.getState(),c,u));
		}
		return lDto;
	}

	@Override
	public FundraisingEventDto findFundraisingEvent(long id) {
		//try {

			Query query = em.createQuery("select f from FundraisingEvent f where f.id=:idEvent");
			query.setParameter("idEvent", id);
			FundraisingEvent f = (FundraisingEvent) query.getSingleResult();
			UserDto u=new UserDto(f.getPublisher().getId(),f.getPublisher().getLogin());
			CampDto c=new CampDto(f.getCamp().getId(),f.getCamp().getAddress(),f.getCamp().getCountry());
			FundraisingEventDto fDto=new FundraisingEventDto(f.getId(),f.getTitle(),
					f.getDescription(),f.getGoal(),f.getPublishDate(),
					f.getUrgency(),f.getFinishingDate(),
					f.getImagePath(),f.getState(),c,u);
			return fDto;
		/*} catch (Exception e) {
			System.out.println("introuvable");
			//return new FundraisingEventDto();
		}*/
	}

	@Override
	public double getSumAmountByEvent(long id) {
		try {

			TypedQuery<Double> query=em.createQuery("select sum(d.amount) from Donation d  where d.fundraisingEvent.id=:event",Double.class);
			query.setParameter("event", id);
			double result=query.getSingleResult();
			System.out.println("somme: "+result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		/*TypedQuery<Double> query=em.createQuery("select sum(d.amount) from Donation d  where f.fundraisingEvent.id=:idEvent",Double.class);
		query.setParameter("idEvent", id);
		double result=query.getSingleResult();
		System.out.println("somme: "+result);
		return result;*/
		
	}

	@Override
	 public List<FundraisingEvent> listEventsByUserId(long id) {
		Query query=em
				.createQuery("select f from FundraisingEvent f where f.publisher.id=:idUser ");
		query.setParameter("idUser", id);
		List<FundraisingEvent> l=query.getResultList();
		for(int i = 0 ; i< l.size() ; i++){
			System.out.println(l.get(i).getPublisher().getId());
			System.out.println(l.get(i).getTitle());
		}
		return l;
	}
	@Override
	public List<FundraisingEventDto> listEventsByUserIdDto(long id) {
		// TODO Auto-generated method stub
		List<FundraisingEvent> liste= listEventsByUserId(id);
		List<FundraisingEventDto> l=new ArrayList<FundraisingEventDto>();
		for(FundraisingEvent f:liste){
			UserDto u=new UserDto(f.getPublisher().getId(),f.getPublisher().getLogin());
			CampDto c=new CampDto(f.getCamp().getId(),f.getCamp().getAddress());
			l.add(new FundraisingEventDto(f.getId(),f.getTitle(),
					f.getDescription(),f.getGoal(),f.getPublishDate(),
					f.getUrgency(),f.getFinishingDate(),
					f.getImagePath(),f.getState(),c,u));
		}
		return l;
	}

}
