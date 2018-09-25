package tn.undefined.universalhaven.buisness; 
 
import java.util.List;
import java.util.Map;

import javax.ejb.Local; 
 
import tn.undefined.universalhaven.entity.Camp;
import tn.undefined.universalhaven.entity.Refugee;
import tn.undefined.universalhaven.entity.Rooms;
import tn.undefined.universalhaven.entity.User; 
@Local 
public interface CampServiceLocal { 
  public boolean CreateCamp(Camp camp); 
  public boolean disbandCamp(long camp); 
  public boolean activateCamp(long camp);
  public List<Camp>  ListCamp(); 
  public Map<String,List<Camp>> ListCampPerCountry(); 
  public Map<String,Long> CountCampPerCountry();
  public boolean updateCamp(Camp camp); 
  public List<User> findCampManager();
  public Camp  getCampById(long campid);
  public Refugee getRefugeeById(long refugeeid);
  public long findcampid(long userid);
  public List<Refugee> findallrefugees();
  public boolean deleteRefugee(Refugee refu);
  public Camp getCampByUser(long userid);
  public List<User> getCampStaff( long campid);
  public boolean deleteRefugeeangular(long refu);
  public boolean CreateRoom(Rooms room);
} 
