package tn.undefined.universalhaven.resources;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.undefined.universalhaven.buisness.ResourcesHistoryServiceLocal;
import tn.undefined.universalhaven.entity.ResourcesHistory;
import tn.undefined.universalhaven.enumerations.UserRole;
import tn.undefined.universalhaven.jwt.JWTTokenNeeded;

@Path("resourcesHistory")
@RequestScoped
@ManagedBean
public class ResourcesHistoryResource {
	@EJB
	private ResourcesHistoryServiceLocal resourcesHistoryServiceLocal;

	/*
	 * @POST
	 * 
	 * @Consumes(MediaType.APPLICATION_JSON) public Response
	 * addResourcesResource(ResourcesHistory resHis) { if
	 * (resourcesHistoryServiceLocal.addResourcesHistory(resHis)) return
	 * Response.status(Status.OK).build(); return
	 * Response.status(Status.NOT_ACCEPTABLE).build(); }
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(role = UserRole.LOGISTICS_AND_REFUGEES_MANAGER)
	public Response getListHistory() {
		List<ResourcesHistory> list = resourcesHistoryServiceLocal.getResourcesHistory();
		if (list.isEmpty())

			return Response.status(Status.NO_CONTENT).entity("No Resource History was found").build();
		return Response.ok(list).build();

	}
}
