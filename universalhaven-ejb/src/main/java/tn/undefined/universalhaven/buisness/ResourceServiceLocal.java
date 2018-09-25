package tn.undefined.universalhaven.buisness;

import java.util.List;
import java.util.Map;
import javax.ejb.Local;

import tn.undefined.universalhaven.entity.Resource;
import tn.undefined.universalhaven.entity.ResourcesHistory;

@Local
public interface ResourceServiceLocal {
	public boolean addResource(Resource resource);

	public boolean addResourceToCamp(Resource resource, int campId);

	public ResourcesHistory depositResource(Resource resource);

	public boolean modifyResource(Resource resource);

	public List<Resource> getCampResources();

	public boolean withdrawResource(Resource resource, Double qte);

	public Boolean removeResource(long id);

	public Map<String, Double> getQuantityByType();

	public Resource FindResourceById(long id);
}
