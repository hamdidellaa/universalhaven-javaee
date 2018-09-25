package tn.undefined.universalhaven.buisness;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import tn.undefined.universalhaven.entity.ApplicationForm;
import tn.undefined.universalhaven.entity.Attachment;

@Local
public interface ApplicationFormServiceLocal {

	
	public long apply(ApplicationForm app);
	public List<ApplicationForm> listApplication();
	public List<ApplicationForm> listApplicationPerCountry(String country);
	public List<ApplicationForm> listApplicationPerGender(String gender);
	public long reviewApplication(ApplicationForm application , boolean review , long revieww );
	public int addAttachment(int application , String name );
	public int addAttachmentMobile(int application, Attachment att);
}
