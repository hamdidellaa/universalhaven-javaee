package tn.undefined.universalhaven.buisness;

import java.util.List;

import javax.ejb.Remote;

import tn.undefined.universalhaven.entity.Mail;
import tn.undefined.universalhaven.enumerations.UserRole;
@Remote 
public interface MailServiceRemote {
	public String getSubscribedUsers();
	public boolean contacterNous(Mail mail);
	public boolean deleteMail(Mail mail);
	public String sendMailPerSomthing(UserRole role, String country, String skill);
	public boolean sendMail(Mail mail);
	public boolean updatestatus(long mailid);
	public List<Mail> findmails();
	public boolean deleteMailangular(long mail);
}