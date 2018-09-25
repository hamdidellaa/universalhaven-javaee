package tn.undefined.universalhaven.util;

public class CallLimitExceededException extends Exception {
	
	public CallLimitExceededException() {
		super("You have reached the max call rate per minute, please try again");
	}
	
}
