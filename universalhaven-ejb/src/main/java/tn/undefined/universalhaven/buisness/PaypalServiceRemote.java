package tn.undefined.universalhaven.buisness;

import javax.ejb.Remote;

@Remote
public interface PaypalServiceRemote {
	public String pay (String total, String creditCardType, String creditCardNumber
			, int expireMonth, int expireYear, String cvv2, String firstName, String lastName);
}
