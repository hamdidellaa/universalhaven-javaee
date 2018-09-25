package tn.undefined.universalhaven.buisness;



import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import tn.undefined.universalhaven.entity.ResourcesHistory;

@Local
public interface ResourcesHistoryServiceLocal {
	
	public Boolean addResourcesHistory(ResourcesHistory resourcesHistory, long userId);
	public List<ResourcesHistory> getResourcesHistory();
	public Map<String, Double> getDepositedResourcesHistory();
	public Boolean removeResourcesHistory(ResourcesHistory resourcesHistory);
	
}
