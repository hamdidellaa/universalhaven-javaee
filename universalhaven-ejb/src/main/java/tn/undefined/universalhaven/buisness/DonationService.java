package tn.undefined.universalhaven.buisness;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import tn.undefined.universalhaven.entity.Donation;

@Stateless

public class DonationService implements DonationServiceLocal,DonationServiceRemote {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public boolean add(Donation donation) {
		try {
			donation.setDonationDate(new Date());
			em.persist(donation);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Collection<Donation> getAll() {
		return (List<Donation>) em.createQuery("SELECT d from Donation d").getResultList();
	}

	@Override
	public Map<String, Double> getDonationsByCountry() {
		Query query = em.createQuery("SELECT d.country,SUM(d.amount) from Donation d" + " GROUP BY d.country");
		List<Object[]> results = query.getResultList();
		Map<String, Double> resultMap = new HashMap<>();
		for (Object[] result : results) {
			resultMap.put((String) result[0], Double.valueOf(Math.round ((Double) (result[1]) )) ) ;
		}
		return resultMap;

	}

	@Override
	public double getDonationsThisMonth() {
		int thisMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
		System.out.println("Month  : " + thisMonth);
		Query query = em.createQuery("SELECT SUM (d.amount) from Donation d" + " where MONTH(d.donationDate)=:month");
		query.setParameter("month", thisMonth);
		Object result = query.getSingleResult();
		if (result != null) {
			return Math.round ((Double) result);
		}
		return 0;

	}

	@Override
	public double getDonationsThisYear() {
		int thisYear = Calendar.getInstance().get(Calendar.YEAR);
		System.out.println("Month  : " + thisYear);
		Query query = em.createQuery("SELECT SUM (d.amount) from Donation d" + " where YEAR(d.donationDate)=:year");
		query.setParameter("year", thisYear);
		Object result = query.getSingleResult();
		if (result != null) {
			return Math.round ((Double) result);
		}
		return 0;
	}

	@Override
	public double getDonationsToday() {
		int thisYear = Calendar.getInstance().get(Calendar.YEAR);
		int thisDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		int thisMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
		System.out.println("Month  : " + thisYear);
		Query query = em.createQuery("SELECT SUM (d.amount) from Donation d" + " where YEAR(d.donationDate)=:year "
				+ "AND MONTH(d.donationDate)=:month " + "AND DAY(d.donationDate)=:day");
		query.setParameter("year", thisYear);
		query.setParameter("month", thisMonth);
		query.setParameter("day", thisDay);

		Object result = query.getSingleResult();
		if (result != null) {
			return Math.round ((Double) result);
		}
		return 0;
	}
	
	@Override
	public Map<Date, Double> getDonationsByDay() {
		int thisYear = Calendar.getInstance().get(Calendar.YEAR);
		int thisDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		int thisMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
		System.out.println("Month  : " + thisYear);
		Query query = em.createQuery("SELECT YEAR(d.donationDate)," + "MONTH(d.donationDate)," + "DAY(d.donationDate),"
				+ "SUM(d.amount) from Donation d" + " GROUP BY YEAR(d.donationDate), " + " MONTH(d.donationDate), "
				+ " DAY(d.donationDate)");

		List<Object[]> results = query.getResultList();
		Map<Date, Double> resultMap = new TreeMap<>();
		for (Object[] result : results) {
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, (int) result[0]);
			c.set(Calendar.MONTH, ((int) result[1]) - 1 );
			c.set(Calendar.DAY_OF_MONTH, (int) result[2]);
			Date date = c.getTime();

			

			resultMap.put(date, Double.valueOf(Math.round ((Double) (result[3]) )));
		}
		
		return resultMap;
	}
	
	
	public Map<String,Double> getDonationsByPaymentMethod(){
		Query query = em.createQuery("SELECT d.paymentMethod,SUM(d.amount) from Donation d" + " GROUP BY d.paymentMethod");
		List<Object[]> results = query.getResultList();
		Map<String, Double> resultMap = new HashMap<>();
		for (Object[] result : results) {
			if (result[0]!=null) {
				resultMap.put((String) result[0], Double.valueOf(Math.round ((Double) (result[1]))));
			}
		}
		return resultMap;
	}

	@Override
	public Double getAverage() {
		Query query= em.createQuery("SELECT AVG(d.amount) from Donation d");
		Object result = query.getSingleResult();
		if (result!=null) {
			return  Double.valueOf(Math.round ((Double)result));
		}
		return 0d;
	}
	

	@Override
	public Map<String, Double> getCountriesDonationAverage() {
		Query query = em.createQuery("SELECT d.country,AVG(d.amount) from Donation d" + " GROUP BY d.country");
		List<Object[]> results = query.getResultList();
		Map<String, Double> resultMap = new HashMap<>();
		for (Object[] result : results) {
			resultMap.put((String) result[0], Double.valueOf(Math.round ((Double) (result[1]))));
		}
		return resultMap;

	}
	
	
	@Override
	public Map<String, Double> getDonationsByMonth() {
		
		
		Query query = em.createQuery("SELECT YEAR(d.donationDate)," + "MONTH(d.donationDate),"
				+ "SUM(d.amount) from Donation d" + " GROUP BY YEAR(d.donationDate), " + " MONTH(d.donationDate)");

		List<Object[]> results = query.getResultList();
		Map<String, Double> resultMap = new HashMap<>();
		for (Object[] result : results) {
			
			String month = (String.valueOf((int) result[1])) +"/"+(String.valueOf((int) result[0]));
			resultMap.put(month, Double.valueOf(Math.round ((Double) (result[2]))));
		}
		return resultMap;
	}

}
