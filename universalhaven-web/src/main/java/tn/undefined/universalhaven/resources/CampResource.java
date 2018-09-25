package tn.undefined.universalhaven.resources;

import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
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
import tn.undefined.universalhaven.entity.Rooms;
import tn.undefined.universalhaven.entity.User;
import tn.undefined.universalhaven.enumerations.UserRole;
import tn.undefined.universalhaven.jwt.JWTTokenNeeded;
import tn.undefined.universalhaven.buisness.CampServiceLocal;
import tn.undefined.universalhaven.buisness.UserServiceLocal;

import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
@Path("camp")
@RequestScoped
public class CampResource {

	@EJB
	CampServiceLocal serviceCamp;
	@GET
	@Path("/findallcamps")
	@Produces(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response getallcamps() {
		List<Camp> camps = new ArrayList<>();

		camps.addAll(serviceCamp.ListCamp());
		if (camps.isEmpty()) {
			return Response.status(Status.NO_CONTENT).build();
		}
		return Response.ok(camps).build();
	}
	@GET
	@Path("/findallrefugees")
	@Produces(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response getallrefugees() {
		List<Refugee> refuge = new ArrayList<>();

		refuge.addAll(serviceCamp.findallrefugees());
		if (refuge.isEmpty()) {
			System.out.println(refuge);
			return Response.status(Status.NO_CONTENT).build();
		}
		return Response.ok(refuge).build();
	}
	@GET
	@Path("/findbycountry")
	@Produces(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response getallpercountry() {
		Map<String, List<Camp>> camps;
		camps = (serviceCamp.ListCampPerCountry());
		return Response.status(Status.CREATED).entity(camps).build();
	}
	@GET
	@Path("/countcamps")
	@Produces(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response countAllCampsPerCountry() {
		Map<String, Long> camps;
		camps = (serviceCamp.CountCampPerCountry());
		if (camps.isEmpty() == false) {
			System.out.println("is empty");
		}
		return Response.ok(camps).build();
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Camp> getall() {
		List<Camp> camps = new ArrayList<>();
		camps.add(new Camp());
		return camps;
	}
	@GET
	@Path("/getallroom")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Rooms> getallroom() {
		List<Rooms> room = new ArrayList<>();
		room.add(new Rooms());
		return room;
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response create(Camp camp) {	
		if (serviceCamp.CreateCamp(camp)) {
			return  Response.ok().entity("Camp added").build();
		} else {
			return Response.status(Status.NOT_ACCEPTABLE).entity("Camp Not added").build();
		}
	}
	@POST
	@Path("/createroom")
	@Consumes(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response createroom(Rooms room) {	
		if (serviceCamp.CreateRoom(room)) {
			return  Response.ok().entity("Camp added").build();
		} else {
			return Response.status(Status.NOT_ACCEPTABLE).entity("Camp Not added").build();
		}
	}
	@PUT
	@Path("/disbandcamps")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response disbandCamps(@FormParam(value = "id") long campId) {
		if (serviceCamp.disbandCamp(campId)) {
			return Response.ok().entity("Camp modified").build();
		} else {
			return Response.status(Status.NOT_MODIFIED).entity("Camp no").build();
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response UpdateCamps(Camp camp) {
		if (serviceCamp.updateCamp(camp)) {
			return Response.ok().entity("Camp edited").build();
		} else {
			return Response.status(Status.NOT_MODIFIED).entity("Camp Not edited").build();
		}
	}
	
	@GET
	@Path("campbyuser")
	public Response getCampPerUser(@QueryParam(value="userid") long userid) {
		
		return Response.ok(serviceCamp.getCampByUser(userid).getId()).build();
		
	}
	
	@GET
	@Path("campstaff")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCampStaff(@QueryParam(value="campid") long campid) {
		return Response.ok(serviceCamp.getCampStaff(campid)).build();
	}

	@GET
	@Path("/findmanager")
	@Produces(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response getCampMan() {
		List<User> users = new ArrayList<>();
		users.addAll(serviceCamp.findCampManager());
		if (users.isEmpty()) {
			return Response.status(Status.NO_CONTENT).build();
		}
		return Response.ok(users).build();
	}
	@GET
	@Path("/findCampById")
	@Produces(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response findCampById(@QueryParam(value = "id") long campId) {
		Camp camp = serviceCamp.getCampById(campId);
		if (camp.getId()== 0) {
			return Response.status(Status.NO_CONTENT).build();
		}
		return Response.ok(camp).build();
	}
	@PUT
	@Path("/activatecamps")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response activateCamps(@FormParam(value = "id") long campId) {
		if (serviceCamp.activateCamp(campId)) {
			return Response.ok().entity("Camp modified").build();
		} else {
			return Response.status(Status.NOT_MODIFIED).entity("Camp no").build();
		}
	}
	@GET
	@Path("/getcampid")
	@Produces(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response getcampid(@QueryParam(value = "userid")long id) {
		long camps;
		camps = (serviceCamp.findcampid(id));		
		return Response.ok(camps).build();
	}
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response deleteRefugees(Refugee ref) {
		if (serviceCamp.deleteRefugee(ref)) {
			return Response.ok(ref).build();
		} else {
			return Response.status(Status.NOT_MODIFIED).entity("refugee is not deleted").build();
		}
	}
	@DELETE
	@Path("/deleteRefugeeById")
	@Consumes(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response deleteRefugeeswithangular(@QueryParam(value = "idref") long idref) {
		if (serviceCamp.deleteRefugeeangular(idref)) {
			return Response.ok().build();
		} else {
			return Response.status(Status.NOT_MODIFIED).entity("refugee is not deleted").build();
		}
	}
	private static void copyFileUsingJava7Files(File source, File dest) throws IOException {
	    Files.copy(source.toPath(), dest.toPath());
	}	
	@GET
	@Path("/upfile")
	public void upfile(@QueryParam(value="file")String file) throws IOException{
		File source = new File("C:/Users/BlacK SouL/Downloads/"+file);
        File dest = new File("D:/4twin/PIDEV/universalhaven-dotnet/Web/Content/Images/"+file);
        long start = System.nanoTime();
        start = System.nanoTime();
        copyFileUsingJava7Files(source, dest);
        System.out.println("Time taken by Java7 Files Copy = "+(System.nanoTime()-start));
	}
	@GET
	@Path("/findRefugeeById")
	@Produces(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response findRefugeeById(@QueryParam(value = "id") long refugeeId) {
		Refugee ref = serviceCamp.getRefugeeById(refugeeId);
		if (ref.getId()== 0) {
			return Response.status(Status.NO_CONTENT).build();
		}
		return Response.ok(ref).build();
	}
}
