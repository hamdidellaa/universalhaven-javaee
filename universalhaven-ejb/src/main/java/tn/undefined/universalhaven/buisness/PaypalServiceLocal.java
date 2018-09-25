package tn.undefined.universalhaven.buisness;

import javax.ejb.Local;
import javax.ejb.Remote;

@Local
public interface PaypalServiceLocal {
	public String pay (String total, String creditCardType, String creditCardNumber
			, int expireMonth, int expireYear, String cvv2, String firstName, String lastName);
}
