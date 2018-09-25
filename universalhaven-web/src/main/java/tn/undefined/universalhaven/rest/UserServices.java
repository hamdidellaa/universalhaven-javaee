package tn.undefined.universalhaven.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import com.google.gson.JsonObject;

import tn.undefined.universalhaven.entity.User;
import tn.undefined.universalhaven.enumerations.UserRole;

import tn.undefined.universalhaven.services.UserServiceLocal;

@Stateless
@Path("user")
public class UserServices {

	@EJB
	UserServiceLocal userService;
	@PersistenceContext
	EntityManager em;

	
	@Path("login") // must be puted
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public Response login(@FormParam("login") String login, @FormParam("password") String password) {
		System.out.println("Login = "+login+" && password="+password);
		if(userService.authenticatee(login, password)!=null)
		{
			
			return Response.status(Status.FOUND).entity(userService.authenticatee(login, password)).build();
		}
		return Response.status(Status.BAD_GATEWAY).entity(null).build();

	}
	
	@Path("check") // this for test
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public void checkPasswordUser(User user) {

		userService.checkPassword(user);

	}

	@Path("password")//  must be puted
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Response addPasswordUser(@QueryParam("token") String token, @QueryParam("email") String email,
			@QueryParam("password") String password) {

		if (password.length() < 8) {
			return Response.status(Status.LENGTH_REQUIRED).entity("Password length must > 8").build();
		}
		int check = userService.addPassword(password, email, token);
		System.out.println(check);

		if (check == -1)
			return Response.status(Status.FORBIDDEN)
					.entity("you can t change your password here please check your profile for changes").build();

		if (check == 1) {
			return Response.status(Status.OK).entity("password has been changed").build();
		}

		return Response.status(Status.FORBIDDEN).entity("please check your email field ").build();
	}

	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("ban")
	@PUT
	public Response banUser(User u) {

	if(userService.banUser(u.getId()))
	{
		return Response.status(Status.OK).entity("user baned").build();
	}
	return Response.status(Status.FORBIDDEN).entity("user does not exist").build();
		 
		
	}

	// @Path("add")
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public Response AddUser(User user) {
		int test = userService.addUser(user);
		if (test == 1) {
			return Response.status(Status.ACCEPTED).entity("user added ").build();
		}

		if (test == -1) {
			return Response.status(Status.NOT_ACCEPTABLE).entity("user already rxist").build();
		}

		if (test == 0) {
			return Response.status(Status.NOT_ACCEPTABLE).entity("Email not valid").build();
		}

		return Response.status(Status.NOT_ACCEPTABLE).entity("please verif your fields or user exists").build();
	}

	@Path("hamdi")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public User AddUser() {

		User user = new User();

		return user;

	}

	// @Path("update")
	@Produces(MediaType.TEXT_PLAIN)
	@PUT
	public Response UpdateUser(User user) {

		int test = userService.updateUser(user);
		if (test == 1) {
			return Response.status(Status.ACCEPTED).entity("user updated").build();
		}

		if (test == -1) {
			return Response.status(Status.NO_CONTENT).entity("please check the fields").build();
		}
		if (test == -2) {
			return Response.status(Status.FORBIDDEN).entity("email and login can not be changed").build();
		}
		if (test == -3) {
			return Response.status(Status.SEE_OTHER)
					.entity("you dont have password please check your email to have one").build();
		}
		if (test == -4) {
			return Response.status(Status.FORBIDDEN).entity("password cant be changed").build();
		}

		return Response.status(Status.NOT_FOUND).entity("user not found").build();
	}

	// getAgeAverage
	@Path("getAgeAverage")
	@Produces(MediaType.APPLICATION_JSON)
	@GET

	public Response getAgeAverage() {

		return Response.status(Status.ACCEPTED).entity(userService.getAgeAverage()).build();

	}

	@Path("getAvailableUser")
	@Produces(MediaType.APPLICATION_JSON)
	@GET

	public Response getAvailableVolenteers() {

		return Response.status(Status.OK).entity(userService.getAvailableVolenteers()).build();

	}

	// getGenderStats
	@Path("getGenderStats")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Response getGenderStats() {

		return Response.status(Status.OK).entity(userService.getGenderStats()).build();

	}

	// getUserCountPerRole
	@Path("getUserCountPerRole")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Response getUserCountPerRole() {

		return Response.status(Status.OK).entity(userService.getUserCountPerRole()).build();

	}

	@Path("role") /// if not empty array
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Response getUserPerRole(@QueryParam(value = "role") UserRole role) {

		return Response.status(Status.OK).entity(userService.getUserPerRole(role)).build();
	}

	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Response searchForUser(@QueryParam(value = "user") String variable) {

		return Response.status(Status.OK).entity(userService.searchForUser(variable)).build();

	}

	@Path("find")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public User findUser() {

		return new User();

	}

	@POST
	@Consumes("multipart/form-data")
	public Response uploadFile(MultipartFormDataInput input) {

		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("file");

		for (InputPart inputPart : inputParts) {
			MultivaluedMap<String, String> headers = inputPart.getHeaders();
			try {
				InputStream inputStream = inputPart.getBody(InputStream.class, null);
				byte[] bytes = IOUtils.toByteArray(inputStream);
				String filename = getFileName(headers);

				// test for file format //

				String extension = "";
				int i = filename.lastIndexOf('.');
				if (i >= 0) {
					extension = filename.substring(i + 1);
				}

				if (!extension.equalsIgnoreCase("xlsx")) {
					return Response.status(Status.NOT_ACCEPTABLE)
							.entity("Format not supported  please use .xlsx  format").build();
				}
				// test for file format //

				String fileLocation = "C:\\Users\\HD-EXECUTION\\workspaceJBosss\\universalhaven-javaee\\universalhaven-ejb\\assets\\"
						+ UUID.randomUUID().toString() + filename;

				FileOutputStream fileOuputStream = new FileOutputStream(fileLocation);
				fileOuputStream.write(bytes);
				int iu = userService.importUserList(fileLocation);

			} catch (IOException e) {
				return Response.status(Status.NOT_ACCEPTABLE).entity("please check your file ").build();

			}
		}
		return Response.status(Status.OK).entity("Users Imported ").build();
	}

	private String getFileName(MultivaluedMap<String, String> headers) {
		String[] contentDisposition = headers.getFirst("Content-Disposition").split(";");

		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {

				String[] name = filename.split("=");

				String finalFileName = sanitizeFilename(name[1]);
				return finalFileName;
			}
		}
		return "unknown";
	}

	private String sanitizeFilename(String s) {
		return s.trim().replaceAll("\"", "");
	}

	
	@Consumes(MediaType.APPLICATION_JSON)
	@POST

	public Response dellaa(@QueryParam(value = "old") String old, @QueryParam(value = "new") String neww,
			@QueryParam(value = "username") String username) {

		int test = userService.changePassword(old, neww, username);

		if (test == 1) {
			return Response.status(Status.OK).entity("Password changed ").build();
		}
		if (test == -2) {
			return Response.status(Status.NO_CONTENT).entity("user does not exist ").build();
		}
		if (test == -3) {
			return Response.status(Status.FORBIDDEN)
					.entity("you dont have a password please check your email to make one ").build();
		}

		return Response.status(Status.FORBIDDEN).entity("old password is note correct").build();

	}

	@GET
	@Path("fbconn")
	@Produces(MediaType.TEXT_PLAIN)

	public Response conn() {
		JSONObject jsonArray = new JSONObject();
		String token = userService.Fbcon();
		System.out.println(token);
		try {

			URL url = new URL(
					"https://graph.facebook.com/me?fields=email,first_name,last_name,birthday,gender&access_token="
							+ token);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;

			while ((output = br.readLine()) != null) {

				jsonArray = new JSONObject(output);

			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		System.out.println(jsonArray);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		User u = new User();
		for (int i = 0; i < jsonArray.length(); i++) {
			u.setGender(jsonArray.getString("gender").toString());
			u.setName(jsonArray.getString("first_name").toString());
			u.setSurname(jsonArray.getString("last_name").toString());
			u.setEmail(jsonArray.getString("email").toString());
			u.setLogin(jsonArray.getString("last_name").toString() + jsonArray.getString("first_name").toString());
			try {
				u.setBirthDate(dateFormat.parse(jsonArray.getString("birthday").toString()));
			} catch (JSONException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		partageFacebook(token);
		u.setPicture(saveImageFB(token, u.getName() + u.getSurname()));

		userService.addUser(u);
		//////////// import user images //////////////

		return Response.status(Status.CREATED).entity("user signed up with facebook").build();
	}

	
	
	
	//////////////////////
	public void partageFacebook(String token) {
		// partage sur facebook //

		String image = "http://img.thedailybeast.com/image/upload/v1492783077/articles/2013/06/12/it-s-about-time-united-nations-plans-refugee-camps-for-syrians-in-lebanon/130612-dettmer-syria-refugees-lebanon-tease_kkvdql.jpg";
		String caption = "UniversalHaven";

		URL url1;
		try {
			url1 = new URL("https://graph.facebook.com/me/photos?url=" + image + "&caption=" + caption
					+ "&access_token=" + token);
			HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
			conn1.setRequestMethod("POST");
			conn1.setRequestProperty("Accept", "application/json");
			if (conn1.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn1.getResponseCode());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	
	/////////////////////////////
	public String saveImageFB(String token, String file) {
		URL url;

		try {
			url = new URL("https://graph.facebook.com/me/picture?fields=url&type=large&access_token=" + token);

			String path = "C:\\Users\\HD-EXECUTION\\universalhaven-javaee\\universalhaven-web\\src\\main\\webapp\\assets\\";
			String path2 = path + file + ".jpg";
			InputStream in = url.openStream();
			FileOutputStream fos = new FileOutputStream(new File(path + file + ".jpg"));

			int length = -1;
			byte[] buffer = new byte[1024];// buffer for portion of data from
			// connection
			while ((length = in.read(buffer)) > -1) {
				fos.write(buffer, 0, length);
			}

			fos.close();
			in.close();
			return path2;

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	@GET
	@Path("/campstaff")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCampStaff(@QueryParam(value="campid") long campId){
		return Response.ok(userService.getCampStaff(campId)).build();
	}
	
	
	@POST
	@Path("affect")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response assignStaff(List<Integer> values,@QueryParam(value="campid") long campid){
		for (Integer value: values){
			userService.affect(value, campid);
		}
		return Response.ok("Success").build();
	}
	
	@POST
	@Path("removeFromCamp")
	
	public Response removeFromCamp(@QueryParam(value="userid") long userid){
		userService.removeFromCamp(userid);
		return Response.ok("Success").build();
	}
}
