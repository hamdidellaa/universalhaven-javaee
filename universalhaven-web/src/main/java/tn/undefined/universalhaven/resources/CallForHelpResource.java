package tn.undefined.universalhaven.resources;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.undefined.universalhaven.buisness.CallForHelpServiceLocal;
import tn.undefined.universalhaven.entity.CallForHelp;
import tn.undefined.universalhaven.enumerations.UserRole;
import tn.undefined.universalhaven.jwt.JWTTokenNeeded;

@Path("callforhelp")
@RequestScoped
@ManagedBean
public class CallForHelpResource {
	@EJB
	private CallForHelpServiceLocal callForHelpServiceLocal;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.CAMP_STAFF)
	public Response addCallForHelpResource(CallForHelp callForHelp) {
		if (callForHelpServiceLocal.startCallForHelp(callForHelp))
			return Response.status(Status.ACCEPTED).entity("Call For Help Added").build();
		return Response.status(Status.NOT_ACCEPTABLE).entity("Call For Help  couldn't be added").build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.CAMP_STAFF)
	public Response listActiveCallForHelpEventsResource() {
		List<CallForHelp> list = callForHelpServiceLocal.listEvents();
		if (list.isEmpty() == false)
			return Response.ok(list).build();
		else
			return Response.status(Status.NO_CONTENT).entity("No Calls For Help were found ").build();

	}

	@GET
	@Path("active")
	@Produces(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.CAMP_STAFF)
	public Response listCallForHelpEventsResource() {
		List<CallForHelp> list = callForHelpServiceLocal.listActiveEvents();
		if (list.isEmpty())
			return Response.status(Status.NO_CONTENT).entity("No Calls For Help were found").build();

		return Response.ok(list).build();
	}

	@GET
	@Path("{title}")
	@Produces(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.CAMP_STAFF)
	public Response getCallForHelpByNameResource(@PathParam("title") String title) {
		List<CallForHelp> list = callForHelpServiceLocal.findCallForHelpByTitle(title);
		if (list.isEmpty() == false)
			return Response.ok(list).build();
		return Response.status(Status.NO_CONTENT).entity("No Calls For Help were found").build();
	}

	@GET
	@Path("description/{str}")
	@Produces(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.CAMP_STAFF)
	public Response getCallForHelpByCriteriaResource(@PathParam("str") String description) {
		List<CallForHelp> list = callForHelpServiceLocal.findCallForHelpByDescription(description);
		if (list.isEmpty() == false)
			return Response.ok(list).build();
		return Response.status(Status.NO_CONTENT).entity("No Calls For Help were found").build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.CAMP_MANAGER)
	public Response modifyCallForHelpResource(CallForHelp callForHelp) {
		if (callForHelpServiceLocal.modifyCallForHelp(callForHelp))
			return Response.status(Status.ACCEPTED).entity("Call For Help modified successfully").build();
		return Response.status(Status.NOT_ACCEPTABLE).entity("Call For Help couldn't be modified ").build();
	}

	@DELETE
	//@JWTTokenNeeded(role = UserRole.CAMP_MANAGER)
	public Response removeCallForHelpResource(CallForHelp callForHelp) {
		if (callForHelpServiceLocal.endCallForHelp(callForHelp))
			return Response.status(Status.ACCEPTED).entity("Call For Help removed successfully").build();
		return Response.status(Status.NOT_ACCEPTABLE).entity("Call For Help couldn't be removed").build();
	}
}
