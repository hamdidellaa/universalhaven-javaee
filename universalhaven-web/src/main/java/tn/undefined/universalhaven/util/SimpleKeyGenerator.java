package tn.undefined.universalhaven.util;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Random;


public class SimpleKeyGenerator implements KeyGenerator {

   
    @Override
    public Key generateKey() {
        String keyString = "UnIv£rSaLhAv£n-UnD£FiN£D";
        Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
        return key;
    }
    
    public static String getSalt() {
    	String SALTCHARS = "ABCDEFGHIJKMNOPQRSTUVWXY1234567890$!$*";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
    public static String crypt(long id){
		String crypted= getSalt() + "Z" + String.valueOf(id)+"L";
		crypted+=getSalt();
		return crypted;
    }
    public static long decrypt(String crypted) {
    	int start = crypted.indexOf("Z");
    	int end = crypted.indexOf("L");
    	if ((start==-1) || (end==-1)) {
    		return 0;
    	}
    	String decrypted= crypted.substring(start+1, end);
    	try {
    		return Long.parseLong(decrypted);
    	}
    	catch(NumberFormatException e) {
    		return 0;
    	}
    	
    }
    
}