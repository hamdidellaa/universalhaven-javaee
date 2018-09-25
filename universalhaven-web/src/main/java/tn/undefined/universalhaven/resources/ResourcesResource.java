package tn.undefined.universalhaven.resources;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.undefined.universalhaven.buisness.ResourceServiceLocal;
import tn.undefined.universalhaven.buisness.ResourcesHistoryServiceLocal;
import tn.undefined.universalhaven.buisness.UserServiceLocal;
import tn.undefined.universalhaven.entity.Resource;
import tn.undefined.universalhaven.entity.ResourcesHistory;
import tn.undefined.universalhaven.enumerations.UserRole;
import tn.undefined.universalhaven.jwt.JWTTokenNeeded;
import tn.undefined.universalhaven.util.WithdrawParam;

@Path("resource")
@RequestScoped

public class ResourcesResource {

	@EJB
	private ResourceServiceLocal resourceServiceLocal;

	@EJB
	private ResourcesHistoryServiceLocal historyService;

	@EJB
	private UserServiceLocal serviceUser;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.LOGISTICS_AND_REFUGEES_MANAGER)
	public Response addResourcesResource(Resource resource) {
		if (resourceServiceLocal.addResource(resource))
			return Response.status(Status.ACCEPTED).entity("Resource added successfully").build();
		return Response.status(Status.NOT_ACCEPTABLE).entity("Resource couldn't be added").build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.LOGISTICS_AND_REFUGEES_MANAGER)
	public Response modifyResourcesResource(WithdrawParam params) {
		Resource resource = params.getResource();

		ResourcesHistory history = resourceServiceLocal.depositResource(resource);
		if (history != null) {
			if (historyService.addResourcesHistory(history, params.getUserId()))
				return Response.status(Status.ACCEPTED).entity("Resource Modified with Resource History added").build();
		}

		return Response.status(Status.NOT_ACCEPTABLE).entity("Resource wasn't modified").build();
	}
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	//@JWTTokenNeeded(role = UserRole.LOGISTICS_AND_REFUGEES_MANAGER)
	public Response modifyResourceResource(@PathParam(value = "id")long id,Resource resource) {
		
		if (resourceServiceLocal.modifyResource(resource)) {
				return Response.status(Status.ACCEPTED).entity("Resource Modified").build();
		}

		return Response.status(Status.NOT_ACCEPTABLE).entity("Resource wasn't modified").build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.LOGISTICS_AND_REFUGEES_MANAGER)
	public Response getCampResourcesResource() {
		List<Resource> list = resourceServiceLocal.getCampResources();
		if (list.isEmpty() == false)
			return Response.ok(list).build();
		return Response.status(Status.NO_CONTENT).entity("No Resources were found").build();
	}
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.LOGISTICS_AND_REFUGEES_MANAGER)
	public Response getOneResourcesResource(@PathParam(value = "id")long id) {
		Resource res = resourceServiceLocal.FindResourceById(id);
		if (res!=null)
			return Response.status(Status.OK).entity(res).build();
		return Response.status(Status.NO_CONTENT).entity("No Resource was found").build();
	}

	@GET
	@Path("/qte")
	@Produces(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role = UserRole.LOGISTICS_AND_REFUGEES_MANAGER)
	public Response getResourcesQuantityPerTypeResource() {
		Map<String, Double> map = resourceServiceLocal.getQuantityByType();
		if (map.isEmpty() == false)
			return Response.ok(map).build();
		return Response.status(Status.NO_CONTENT).entity("No Resources were found").build();
	}

	@DELETE
	//@JWTTokenNeeded(role = UserRole.LOGISTICS_AND_REFUGEES_MANAGER)
	@Path("{id}")
	public Response removeReourcesResource(@PathParam(value = "id")long id) {
		if (resourceServiceLocal.removeResource(id))
			return Response.status(Status.ACCEPTED).entity("Resource removed successfully").build();
		return Response.status(Status.NOT_ACCEPTABLE).entity("Resource couldn't be removed").build();
	}

}
