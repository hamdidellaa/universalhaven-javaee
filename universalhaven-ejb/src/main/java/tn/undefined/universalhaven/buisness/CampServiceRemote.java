package tn.undefined.universalhaven.buisness; 
 
import java.util.List;
import java.util.Map;

import javax.ejb.Remote; 
 
import tn.undefined.universalhaven.entity.Camp;
import tn.undefined.universalhaven.entity.Refugee;
import tn.undefined.universalhaven.entity.Rooms;
import tn.undefined.universalhaven.entity.User; 
@Remote 
public interface CampServiceRemote { 
  public boolean CreateCamp(Camp camp); 
  public boolean disbandCamp(long camp); 
  public boolean activateCamp(long camp);
  public List<Camp>  ListCamp(); 
  public Map<String,List<Camp>> ListCampPerCountry();
  public Map<String,Long> CountCampPerCountry();
  public boolean updateCamp(Camp camp); 
  public List<User> findCampManager();
  public Camp  getCampById(long campid);
  public long findcampid(long userid);
  public List<Refugee> findallrefugees();
  public boolean deleteRefugee(Refugee refu);
  public Refugee getRefugeeById(long refugeeid);
  public boolean deleteRefugeeangular(long refu);
  public boolean CreateRoom(Rooms room);
} 
