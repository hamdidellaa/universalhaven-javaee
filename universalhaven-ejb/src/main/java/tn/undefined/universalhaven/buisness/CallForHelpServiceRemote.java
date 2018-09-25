package tn.undefined.universalhaven.buisness;

import java.util.Set;

import javax.ejb.Remote;

import tn.undefined.universalhaven.entity.CallForHelp;

@Remote
public interface CallForHelpServiceRemote {
	public int endCallForHelp(long id);

	public Set<CallForHelp> listActiveEvents();

	public Set<CallForHelp> listEventsByCamp(long id);

	public Boolean startCallForHelp(CallForHelp callforhelp);
}
