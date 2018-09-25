package tn.undefined.universalhaven.services;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import tn.undefined.universalhaven.entity.ApplicationForm;

@Local
public interface ApplicationFormServiceLocal {

	
	public int apply(ApplicationForm app);
	public List<ApplicationForm> listApplication();
	public List<ApplicationForm> listApplicationPerCountry(String country);
	public List<ApplicationForm> listApplicationPerGender(String gender);
	public long reviewApplication(ApplicationForm application , boolean review , long revieww );
	public int addAttachment(int application , String name );
	
}
