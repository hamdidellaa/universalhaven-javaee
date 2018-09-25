package tn.undefined.universalhaven.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.undefined.universalhaven.entity.Camp;
import tn.undefined.universalhaven.entity.Mail;
import tn.undefined.universalhaven.entity.Refugee;
import tn.undefined.universalhaven.enumerations.UserRole;
import tn.undefined.universalhaven.jwt.JWTTokenNeeded;
import tn.undefined.universalhaven.util.MailParam;
import tn.undefined.universalhaven.buisness.MailServiceLocal;

@Path("mail")
@RequestScoped
public class MailResource {
	@EJB
	MailServiceLocal serviceMail;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Mail> getall() {
		List<Mail> camps = new ArrayList<>();
		camps.add(new Mail());
		return camps;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response SendMail(Mail mail) {
		System.out.println(mail.getMailSender());
		if (serviceMail.contacterNous(mail)) {
			return Response.status(Status.CREATED).entity("Mail sendet").build();
		} else {
			return Response.status(Status.NOT_ACCEPTABLE).entity("Mail Not sendet").build();
		}

	}


	@POST
	@Path("/sendmail")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response sendMailPer(MailParam param) {
		try {
			Mail mail = param.getMail();
			UserRole role = param.getRole();
			String country = param.getCountry();
			String skill = param.getSkill();
			String host = "smtp.gmail.com";
			String user = "he.flach.smok.c4@gmail.com";
			String pass = "mo********";
			String to = serviceMail.sendMailPerSomthing(role, country, skill);
			String from = "he.flach.smok.c4@gmail.com";
			String subject = mail.getSubject();
			String messageText = mail.getContent();
			boolean sessionDebug = false;
			String[] recipientList = to.split(",");
			Properties props = System.getProperties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.required", "true");
			Session mailSession = Session.getDefaultInstance(props, null);
			mailSession.setDebug(sessionDebug);
			Message msg = new MimeMessage(mailSession);
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] address = new InternetAddress[recipientList.length];
			int counter = 0;
			for (String recipient : recipientList) {
				address[counter] = new InternetAddress(recipient.trim());
				counter++;
			}
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			msg.setText(messageText);
			Transport transport = mailSession.getTransport("smtp");
			transport.connect(host, user, pass);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			System.out.println("NewsLetter sended successfully");
			return Response.status(Status.ACCEPTED).entity("NewsLetter sended successfully").build();
		} catch (Exception ex) {
			System.out.println(ex);
			return Response.status(Status.NOT_ACCEPTABLE).entity("Problem of sending the NewsLetter").build();
		}
	}
	/*@POST
	@Path("/sendmailto")
	@Consumes(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response sendMailto(Mail mail) {
		try {
			
			
			String host = "smtp.gmail.com";
			String user = "universalhavens.noreply@gmail.com";
			String pass = "abcd1234@esprit";
			String to = mail.getMailSender();
			String from = "universalhavens.noreply@gmail.com";
			String subject = mail.getSubject();
			String messageText = mail.getContent();
			boolean sessionDebug = false;
			Properties props = System.getProperties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.required", "true");
			Session mailSession = Session.getDefaultInstance(props, null);
			mailSession.setDebug(sessionDebug);
			Message msg = new MimeMessage(mailSession);
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] address = {new InternetAddress(to)};
			
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			msg.setText(messageText);
			Transport transport = mailSession.getTransport("smtp");
			transport.connect(host, user, "");
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			System.out.println("NewsLetter sended successfully");
			return Response.status(Status.ACCEPTED).entity("NewsLetter sended successfully").build();
		} catch (Exception ex) {
			System.out.println(ex);
			return Response.status(Status.NOT_ACCEPTABLE).entity("Problem of sending the NewsLetter").build();
		}
	}
	@GET
	@Path("/getallmails")
	@Produces(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response getallmails() {
		List<Mail> mail = new ArrayList<>();

		mail.addAll(serviceMail.findmails());
		if (mail.isEmpty()) {
			System.out.println(mail);
			return Response.status(Status.NO_CONTENT).build();
		}
		return Response.ok(mail).build();
	}

	@POST
	@Path("/newsletter")
	@Consumes(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response SendNewsLetter(Mail mail) {
		if ( (mail.getContent()==null) || (mail.getContent().equals("")) ){
			return Response.status(Status.NOT_ACCEPTABLE).entity("Problem of sending the NewsLetter").build();
		}
		try {
			String host = "smtp.gmail.com";
			String user = "universalhavens.noreply@gmail.com";
			String pass = "abcd1234@esprit";
			String to = serviceMail.getSubscribedUsers();
			to = to+","+mail.getMailSender();
			to = to.substring(0, to.length() - 1);
			String from = "universalhavens.noreply@gmail.com";
			String subject = mail.getSubject();
			String messageText = mail.getContent();
			boolean sessionDebug = false;
			String[] recipientList = to.split(",");
			Properties props = System.getProperties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.required", "true");
			Session mailSession = Session.getDefaultInstance(props, null);
			mailSession.setDebug(sessionDebug);
			Message msg = new MimeMessage(mailSession);
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] address = new InternetAddress[recipientList.length];
			int counter = 0;
			for (String recipient : recipientList) {
				address[counter] = new InternetAddress(recipient.trim());
				counter++;
			}
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(subject);
	          msg.setContent(messageText, "text/html; charset=utf-8");
			msg.setSentDate(new Date());
			
			Transport transport = mailSession.getTransport("smtp");
			transport.connect(host, user, "");
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			System.out.println("NewsLetter sended successfully");
			return Response.ok().entity("NewsLetter sended successfully").build();
		} catch (Exception ex) {
			System.out.println(ex);
			return Response.status(Status.NOT_ACCEPTABLE).entity("Problem of sending the NewsLetter").build();
		}
	}*/

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response deleteMail(Mail mail) {
		if (serviceMail.deleteMail(mail)) {
			return Response.ok().entity("mail is deleted").build();
		} else {
			return Response.status(Status.NOT_MODIFIED).entity("mail is not deleted").build();
		}

	}
	@DELETE
	@Path("/deletemail")
	@Consumes(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response deleteMailangu(@QueryParam(value = "idmail")long mail) {
		if (serviceMail.deleteMailangular(mail)) {
			return Response.ok().entity("mail is deleted").build();
		} else {
			return Response.status(Status.NOT_MODIFIED).entity("mail is not deleted").build();
		}

	}
	@PUT
	@Path("/updatestatus")
	//@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response disbandCamps(@QueryParam(value="id") long mailId) {
		if (serviceMail.updatestatus(mailId)) {
			return Response.ok().entity("Status modified").build();
		} else {
			return Response.status(Status.NOT_MODIFIED).entity("Status no").build();
		}
	}

	/*
	 * @POST
	 * 
	 * @Path("/send")
	 * 
	 * @Consumes (MediaType.APPLICATION_JSON)
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public Response SendMail(Mail
	 * mail){
	 * 
	 * try{ String host ="smtp.gmail.com" ; String user = mail.getSenderMail();
	 * String pass = mail.getMailPassword(); String to =
	 * "customercare@universalhaven.com"; String from = mail.getSenderMail();
	 * String subject = mail.getSubject(); String messageText =
	 * mail.getContent(); boolean sessionDebug = false; Properties props =
	 * System.getProperties(); props.put("mail.smtp.starttls.enable", "true");
	 * props.put("mail.smtp.host", host); props.put("mail.smtp.port", "587");
	 * props.put("mail.smtp.auth", "true");
	 * props.put("mail.smtp.starttls.required", "true");
	 * 
	 * //java.security.Security.addProvider(new
	 * com.sun.net.ssl.internal.ssl.Provider()); Session mailSession =
	 * Session.getDefaultInstance(props, null);
	 * mailSession.setDebug(sessionDebug); Message msg = new
	 * MimeMessage(mailSession); msg.setFrom(new InternetAddress(from));
	 * InternetAddress[] address = {new InternetAddress(to)};
	 * msg.setRecipients(Message.RecipientType.TO, address);
	 * msg.setSubject(subject); msg.setSentDate(new Date());
	 * msg.setText(messageText);
	 * 
	 * Transport transport=mailSession.getTransport("smtp");
	 * transport.connect(host, user, pass); transport.sendMessage(msg,
	 * msg.getAllRecipients()); transport.close(); System.out.println(
	 * "message send successfully"); return Response.ok().entity(
	 * "message send successfully").build(); }catch(Exception ex) {
	 * System.out.println(ex); return
	 * Response.status(Status.NOT_ACCEPTABLE).entity(
	 * "problem of sending the message").build(); } }
	 */
}
