package tn.undefined.universalhaven.buisness;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import tn.undefined.universalhaven.entity.CallForHelp;
import tn.undefined.universalhaven.entity.Camp;

@Local
public interface CallForHelpServiceLocal {
	public Boolean endCallForHelp(CallForHelp callForHelp);

	public List<CallForHelp> listActiveEvents();

	public List<CallForHelp> listEvents();

	public Set<CallForHelp> listEventsByCamp(Camp camp);

	public Boolean startCallForHelp(CallForHelp callforhelp);

	public Boolean modifyCallForHelp(CallForHelp callForHelp);
	
	public List<CallForHelp> findCallForHelpByTitle(String title);
	
	public List<CallForHelp> findCallForHelpByDescription(String description) ;
	
	
}
