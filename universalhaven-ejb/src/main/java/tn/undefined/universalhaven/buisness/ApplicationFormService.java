package tn.undefined.universalhaven.buisness;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import tn.undefined.universalhaven.entity.ApplicationForm;
import tn.undefined.universalhaven.entity.Attachment;
import tn.undefined.universalhaven.entity.User;
import tn.undefined.universalhaven.enumerations.UserRole;
import tn.undefined.universalhaven.buisness.ApplicationFormServiceLocal;
import tn.undefined.universalhaven.buisness.UserServiceLocal;

@Stateless
public class ApplicationFormService implements ApplicationFormServiceLocal {

	@PersistenceContext
	EntityManager em;
	@EJB
	UserServiceLocal userService;

	@Override
	public long apply(ApplicationForm app) {
		try {
			TypedQuery<ApplicationForm> query = em.createQuery("select a from ApplicationForm a where email like :c",
					ApplicationForm.class);
			query.setParameter("c", app.getEmail());
			ApplicationForm test = query.getSingleResult();

			if (test.equals(app)) {
				return -1;
			}

			return 1;
		} catch (Exception e) {
				if(app.getPicture() != null){
					String name =  saveImage64("nothing", app.getPicture());
					app.setPicture(name);
				}
			
			
			em.persist(app);
			
			return app.getId();
		}

	}

	@Override
	public List<ApplicationForm> listApplication() {

		TypedQuery<ApplicationForm> query = em.createQuery("select a from ApplicationForm a", ApplicationForm.class);
		return query.getResultList();
	}

	@Override
	public List<ApplicationForm> listApplicationPerCountry(String country) {
		TypedQuery<ApplicationForm> query = em.createQuery("select a from ApplicationForm a where country like :c",
				ApplicationForm.class);
		query.setParameter("c", country);
		return query.getResultList();
	}

	@Override
	public List<ApplicationForm> listApplicationPerGender(String gender) {
		TypedQuery<ApplicationForm> query = em.createQuery("select a from ApplicationForm a where gender like :c",
				ApplicationForm.class);
		query.setParameter("c", gender);
		return query.getResultList();
	}

	@Override
	public long reviewApplication(ApplicationForm application, boolean review, long revieww) {

		User u = em.find(User.class, revieww);
		System.out.println(u.getRole());
		String tmp = String.valueOf(application.getId());
		application = em.find(ApplicationForm.class, Integer.parseInt(tmp));
		if (u.getRole().compareTo(UserRole.ICRC_MANAGER) == 0) {
			application.setReviewer(u);
			application.setAccepted(review);
			em.merge(application);
			em.flush();
 
			User newUser = new User();
			newUser.setName(application.getName());
			newUser.setBirthDate(new Date());
			newUser.setEmail(application.getEmail());
			newUser.setSkills(application.getSkills());
			newUser.setGender(application.getGender());
			newUser.setRole(UserRole.VOLUNTEER);
			if(application.getPicture() != null){
				newUser.setPicture(application.getPicture());
			}
			System.out.println(newUser.toString());
			String[] parts = application.getEmail().split("@");

			newUser.setLogin(application.getName() + parts[0].trim());

			userService.addUser(newUser);
			return newUser.getId();

		}

		return -1;
	}

	@Override
	public int addAttachment(int application, String name) {
		try {
			ApplicationForm applications = em.find(ApplicationForm.class, application);
			if (applications.getEmail() == null) {
				return -1;
			}
			Attachment a = new Attachment();
			em.persist(a);
			a.setApplicationFrom(applications);
			a.setName(name);
			return 1;
		} catch (Exception e) {
			return -1;
		}

	}
	
	
	@Override
	public int addAttachmentMobile(int application, Attachment att ) {
		try {
			ApplicationForm applications = em.find(ApplicationForm.class, application);
			if (applications.getEmail() == null) {
				return -1;
			}
			Attachment a = new Attachment();
			
			a.setApplicationFrom(applications);
			
			String namee = saveImage64("nothing",att.getName());
			
			a.setName(namee);
			em.persist(a);
			return 1;
		} catch (Exception e) {
			return -2;
		}

	}
	

	public String saveImage64(String outputPath, String base64) {

		String temp = UUID.randomUUID().toString() + ".jpeg";
		/*String fileLocation = "E:\\hamdi\\D\\Hamdi\\etude\\4Twin\\pi\\dotNet\\universalhaven-dotnet\\Web\\Content\\Images\\attachments\\"
				+ temp;*/
		
		String fileLocation = "C:\\wamp64\\www\\images\\"
				+ temp;
		//C:\wamp64\www\images
		try (FileOutputStream imageOutFile = new FileOutputStream(fileLocation)) {
			// Converting a Base64 String into Image byte array
			byte[] imageByteArray = Base64.getDecoder().decode(base64);
			imageOutFile.write(imageByteArray);
		} catch (FileNotFoundException e) {
			System.out.println("Image not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading the Image " + ioe);
		}
		return temp;
	}

}
