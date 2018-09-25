package tn.undefined.universalhaven.util;

import javax.ejb.Local;

@Local
public interface RestCallLoggerInterface {
	public void log(String ipAddress,String uri) throws CallLimitExceededException;
}
