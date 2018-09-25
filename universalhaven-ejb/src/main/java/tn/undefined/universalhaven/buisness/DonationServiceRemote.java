package tn.undefined.universalhaven.buisness;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.ejb.Local;

import tn.undefined.universalhaven.entity.Donation;
@Local
public interface DonationServiceRemote {
	public boolean add(Donation donation);
	public Collection<Donation> getAll();
	public Map<String,Double> getDonationsByCountry();
	public double getDonationsThisMonth();
	public double getDonationsThisYear();
	public double getDonationsToday();
	public Map<Date,Double> getDonationsByDay();
	public Map<String,Double> getDonationsByPaymentMethod();
	public Double getAverage();
	public Map<String,Double> getCountriesDonationAverage();
	public Map<String, Double> getDonationsByMonth() ;
}
