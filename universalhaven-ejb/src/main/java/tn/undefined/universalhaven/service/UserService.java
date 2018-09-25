package tn.undefined.universalhaven.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.swing.text.StyledEditorKit.BoldAction;
import javax.transaction.Transactional;
import javax.xml.registry.infomodel.EmailAddress;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;

import tn.undefined.universalhaven.entity.Camp;
import tn.undefined.universalhaven.entity.User;
import tn.undefined.universalhaven.enumerations.UserRole;
import tn.undefined.universalhaven.services.UserServiceLocal;

@Stateless
public class UserService implements UserServiceLocal {
	@PersistenceContext
	EntityManager em;

	@Override
	public boolean banUser(long id) {
		try {

			User user = em.find(User.class, id);
			user.setIsActif(false);
			em.merge(user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public int addUser(User user) {
		try {
			
			User verif = findUser(user.getEmail(), user.getLogin());

			

			if (verif.getEmail() == null || verif.getLogin() == null) {
				
				if (user.getPassword() != null) {
					user.setPassword(generateHash(user.getPassword()));
				}
				if (user.getPassword() == null) {
				
					if(!checkPassword(user))
						return  0 ;
				}
				em.persist(user);
				return 1;
			}

			if (verif.getEmail() != null || verif.getLogin() != null) {
				if (verif.getEmail().equalsIgnoreCase(user.getEmail())
						|| verif.getLogin().equalsIgnoreCase(user.getLogin())) {
					return -1;
				}
				em.persist(user);
				if (user.getPassword() != null) {
					user.setPassword(generateHash(user.getPassword()));
				}
				if (user.getPassword() == null) {
					checkPassword(user);
				}

				return 1;

			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	@Override
	public int getAgeAverage() {
		TypedQuery<Long> query = em.createQuery(
				"SELECT (sum(DATEDIFF(SYSDATE() , u.birthDate)))/count(u.id)/365 from User u ", Long.class);

		int age = query.getSingleResult().intValue();
		return age;
	}

	@Override
	@Transactional
	public Collection<User> getAvailableVolenteers() {
		Query query = em.createQuery("SELECT u from User u where u.assignedCamp IS NULL and u.role=:role");
		query.setParameter("role", UserRole.VOLUNTEER);
		return (Collection<User>) query.getResultList();
	}

	@Override
	public Map<String, Long> getGenderStats() {
		Map<String, Long> map = new HashedMap<>();
		TypedQuery<Long> query = em.createQuery("select count(u.gender) from User u where u.gender like :male ",
				Long.class);
		query.setParameter("male", "%male%");
		map.put("male", query.getSingleResult());

		TypedQuery<Long> query1 = em.createQuery("select count(u.gender) from User u where u.gender like :female ",
				Long.class);
		query1.setParameter("female", "%female%");
		map.put("female", query1.getSingleResult());
		return map;
	}

	@Override
	public Map<String, Integer> getUserCountPerRole() {

		Map<String, Integer> map = new HashedMap<>();
		for (UserRole rol : UserRole.values()) {
			System.out.println(rol);
			TypedQuery<Long> query = em.createQuery("select count(u.role) from User u where u.role = :role ",
					Long.class);
			query.setParameter("role", rol);
			map.put(rol.toString(), query.getSingleResult().intValue());
		}
		return map;
	}

	@Override
	public List<User> getUserPerRole(UserRole role) {
		TypedQuery<User> query = em.createQuery("select u from User u where u.role like :role ", User.class);
		query.setParameter("role", role);

		return query.getResultList();
	}

	@Override
	public int importUserList(String fileLocation) {
		List<User> listUsersXL = new ArrayList<User>();
		TypedQuery<User> query = em.createQuery("select u from User u  ", User.class);
		List<User> listUsers = new ArrayList<User>();
		listUsers = query.getResultList();

		String FILE_NAME = fileLocation;
		try {

			FileInputStream excelFile = new FileInputStream(new File(fileLocation));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = datatypeSheet.iterator(); // Traversing
																	// over each
																	// row of
																	// XLSX file
			while (rowIterator.hasNext()) {
				User us = new User();
				Row row = rowIterator.next(); // For each row, iterate through
												// each columns
				row.getOutlineLevel();
				if (row.getRowNum() != 0) {

					Iterator<Cell> cellIterator = row.cellIterator();
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();

						if (cell.getColumnIndex() == 0) {
							us.setSurname(cell.getStringCellValue());

						}
						if (cell.getColumnIndex() == 1) {
							us.setName(cell.getStringCellValue());
						}
						if (cell.getColumnIndex() == 2) {
							us.setAddress(cell.getStringCellValue());

						}
						if (cell.getColumnIndex() == 3) {

							us.setSkills(cell.getStringCellValue());
						}
						if (cell.getColumnIndex() == 4) {
							us.setProfession(cell.getStringCellValue());
						}
						if (cell.getColumnIndex() == 5) {
							us.setMotivation(cell.getStringCellValue());
						}
						if (cell.getColumnIndex() == 6) {
							us.setLogin(cell.getStringCellValue());
						}
						if (cell.getColumnIndex() == 7) {
							us.setEmail(cell.getStringCellValue());

						}
						if (cell.getColumnIndex() == 8) {
							SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

							String newDate = dateFormat.format(cell.getDateCellValue());
							System.out.println(newDate);

							try {
								us.setBirthDate(dateFormat.parse(newDate));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						if (cell.getColumnIndex() == 9) {
							us.setCountry(cell.getStringCellValue());

						}
						if (cell.getColumnIndex() == 10) {
							us.setGender(cell.getStringCellValue());

						}

					}
					listUsersXL.add(us);

				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (User user : listUsersXL)
			if (!listUsers.contains(user)) {
				System.out.println("dellaa");
				addUser(user);
				//em.persist(user);
			}

		return 1;
	}

	@Override
	public List<User> searchForUser(String name) {
		TypedQuery<User> query = em.createQuery("select u from User u " + "where u.name LIKE :name "
				+ "or surname LIKE :surname " + "or u.email LIKE :email ", User.class);
		query.setParameter("name", "%" + name + "%");
		query.setParameter("surname", "%" + name + "%");
		query.setParameter("email", "%" + name + "%");

		return query.getResultList();

	}

	@Override
	public int updateUser(User user) {
		try {
			User test = em.find(User.class, user.getId());
			if (test.getEmail() == null || test.getLogin() == null)
			{
				return -1;
			}
			if( !test.getLogin().equalsIgnoreCase(user.getLogin()))
			{
				return -2;
			}
			if(!test.getEmail().equalsIgnoreCase(user.getEmail()) )
			{
				return -2;
			}
			
			if(test.getPassword()==null)
			{
				checkPassword(test);
				return -3;
			}
			if(!test.getPassword().equalsIgnoreCase(user.getPassword()))
			{
				
				return -4;
			}
			em.merge(user);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	@Override
	public User findUser(String email, String login) {
		try {

			Query query = em.createQuery("select u from User u where u.login = :login or u.email = :email", User.class);
			query.setParameter("login", login);
			query.setParameter("email", email);
			User neww = (User) query.getSingleResult();
			return neww;
		} catch (Exception e) {
			return new User();
		}

	}

	@Override
	public int changePassword(String old, String neew, String username) {

		try{
		Query query = em.createQuery("select u from User u where u.login = :login", User.class);
		query.setParameter("login", username);
		User u = (User) query.getSingleResult();

		if(u.getPassword()==null){
			checkPassword(u);
			return -3;
		}
		
		if (verifPassword(old, u.getPassword())) {
			String newPass = generateHash(neew);
			u.setPassword(newPass);
			SendMail(u.getEmail(), "password changed", "" + " thank you for using our application ");
			return 1;
		}
		
		}catch(Exception e){
			return -2;
		}
		
		return -1;
	}

	// function send mail //
	public boolean SendMail(String to, String subject, String body) {

		final String username = "tiestohd@gmail.com";
		final String password = "bestdelloo1";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("from-email@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setContent(body, "text/html");

			Transport.send(message);

			return true;

		} catch (MessagingException e) {

			
			throw new RuntimeException(e);
			

		}
		
	}

	/// send email ///
	/// PASSWORD //////
	@Override
	public boolean checkPassword(User u) {

		if (u.getPassword() == null) {
			String email = generateHash(u.getEmail());
			if(SendMail(u.getEmail(), "Password reset",
					"<a href=\"http://127.0.0.1:18080/universalhaven-web/rest/user/password?token=" + email
					+ "&email=&password=\"> please click here to reset your password</a>"))
				return true ;
			
		}

		return false;
	}

	@Override
	public int addPassword(String password, String email, String hashed) {

		if (verifPassword(email, hashed)) {
			TypedQuery<User> query = em.createQuery("select u from User u where u.email=:email ", User.class);
			query.setParameter("email", email);

			User u = query.getSingleResult();
			if (u.getPassword() != null) {
				return -1;
			}
			String newPass = generateHash(password);
			u.setPassword(newPass);
			em.merge(u);
			return 1;
		}
		return 0;

	}

	/// PASSWORD //////

	/// function hash passpword ///

	public static final String SALT = "hamdidellaa";

	public String generateHash(String input) {
		StringBuilder hash = new StringBuilder();

		try {
			MessageDigest sha = MessageDigest.getInstance("MD5");
			byte[] hashedBytes = sha.digest(input.getBytes());
			char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
			for (int idx = 0; idx < hashedBytes.length; ++idx) {
				byte b = hashedBytes[idx];
				hash.append(digits[(b & 0xf0) >> 4]);
				hash.append(digits[b & 0x0f]);
			}
		} catch (NoSuchAlgorithmException e) {
			// handle error here.
		}

		return hash.toString();
	}

	public boolean verifPassword(String password, String hashPassword) {
		String saltedPassword = password;
		String hashedPassword = generateHash(saltedPassword);
		System.out.println("hhhhh" + " " + hashedPassword);
		if (hashedPassword.equalsIgnoreCase(hashPassword))
			return true;
		return false;

	}

	/// hash passowrd end //

	@Override
	public String Fbcon() {

		String authUrl = "https://graph.facebook.com/oauth/authorize?type=user_agent&client_id=371192700003900&redirect_uri=https://hdexecution.github.io/hamdiCV/&scope=user_birthday, user_religion_politics"
				+ ", user_relationships, user_relationship_details, user_hometown, user_location, user_likes"
				+ ", user_education_history, user_work_history, user_website, user_events, user_photos, user_videos, user_friends"
				+ ", user_about_me, user_status, user_games_activity, user_tagged_places, user_posts, rsvp_event, email, read_insights, publish_actions"
				+ ", read_audience_network_insights, read_custom_friendlists, user_actions.news, user_actions.fitness"
				+ ", user_actions.books, user_actions.music, user_actions.video, user_managed_groups, manage_pages, pages_manage_cta"
				+ ", pages_manage_instant_articles, pages_show_list, publish_pages, read_page_mailboxes, ads_management, ads_read, business_management, pages_messaging"
				+ ", pages_messaging_phone_number, pages_messaging_subscriptions, instagram_basic"
				+ ", instagram_manage_comments, instagram_manage_insights, public_profile";

		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get(authUrl);
		String accessToken;

		while (true) {
			if (!driver.getCurrentUrl().contains("facebook.com")) {
				String url = driver.getCurrentUrl();
				accessToken = url.replaceAll(".*#access_token=(.+)&.*", "$1");
				driver.quit();

				FacebookClient fbClient = new DefaultFacebookClient(accessToken);
				// User us = fbClient.fetchObject("me", User.class);
				return accessToken;

			}
		}

	}
	@Override
	public User authenticatee(String username, String password) {
		
		Query query=  em.createQuery("select u from User u where login=:login");
		query.setParameter("login", username);
		
		List<User> users = query.getResultList();
		if ((users==null ) || (users.isEmpty())) {
			return null;
		}
		
		if (verifPassword(password, users.get(0).getPassword())==false) {
			return null;
		}
		System.out.println("logged in");
		return users.get(0);
		
		
	}
	
	
	public List<User> getCampStaff(long campid){
		Camp c = em.find(Camp.class, campid);
		Query q = em.createQuery("SELECT u from User u where u.role=:role and u.assignedCamp=:camp");
		q.setParameter("role", UserRole.CAMP_STAFF);
		q.setParameter("camp", c);
		return q.getResultList();
	}

	@Override
	public void affect(long userid, long campid) {
		
		User u = em.find(User.class, userid);
		Camp c = em.find(Camp.class, campid);
		u.setAssignedCamp(c);
		u.setRole(UserRole.CAMP_STAFF);
	}
	
	@Override
	public void removeFromCamp(long userid) {
		
		User u = em.find(User.class, userid);
		
		u.setAssignedCamp(null);
		u.setRole(UserRole.VOLUNTEER);
	}
}
