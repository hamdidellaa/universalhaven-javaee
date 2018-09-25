package tn.undefined.universalhaven.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;



@Singleton

public class RestCallLogger implements RestCallLoggerInterface {
	
	
	
	private Map<String, List<Date> > logs;
	private Map<String,Date> blacklistedAddresses;
	
	private final static  int allowedcalls= 5000;
	
	private static FileLogger logger = null ;
	
	@PostConstruct
	public void init() {
		if (logger==null) {
			logger= new FileLogger();
		}
		logs = new HashMap<>();
		blacklistedAddresses = new HashMap<>();
		
	}
	
	
	
	
	public void log(String ipAddress,String uri) throws CallLimitExceededException {
		if (blacklistedAddresses.containsKey(ipAddress)) {
			throw new CallLimitExceededException();
		}
		if (logs.containsKey(ipAddress)) {
			int calls= logs.get(ipAddress).size() +1;
			if (calls>allowedcalls) {
				Calendar c = Calendar.getInstance();
				c.add(Calendar.MINUTE, 15);		
				blacklistedAddresses.put(ipAddress , c.getTime());
				logs.remove(ipAddress);
				logger.log(ipAddress,uri,"SEVERE");
				throw new CallLimitExceededException();
			}
			logs.get(ipAddress).add(new Date());
			logger.log(ipAddress,uri,"INFO");
		}
		else {
			List<Date> dates = new ArrayList<>();
			dates.add(new Date());
			logs.put(ipAddress,dates);
		}
	}
	
	
	@Schedule(minute="*/2", hour="*", second="*",persistent=false)
	public void clearLogs() throws InterruptedException {
		Calendar calendar= Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -1);
		Date oneMinuteEarlier=calendar.getTime();
		
		for (Entry<String, List<Date>> entry  : logs.entrySet()) {
			Iterator<Date> it = entry.getValue().iterator();
			while (it.hasNext()) {
				Date date = it.next();
				if (oneMinuteEarlier.after(date)) {
					it.remove();
				}
			}
		}
		List<String> toRemove = new ArrayList<>(); 
		for (Entry<String, Date> entry : blacklistedAddresses.entrySet()) {
			if (entry.getValue().after(new Date())) {
				toRemove.add(entry.getKey());
			}
		}
		for (String ip : toRemove) {
			blacklistedAddresses.remove(ip);
		}
	}
	
	

}
