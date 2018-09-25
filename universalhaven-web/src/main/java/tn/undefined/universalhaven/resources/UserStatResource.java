package tn.undefined.universalhaven.resources;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.undefined.universalhaven.buisness.UserServiceLocal;

@Stateless
@Path("userstat")
public class UserStatResource {

	@EJB
	UserServiceLocal userService;
	

	// getAgeAverage
		@Path("getAgeAverage")
		@Produces(MediaType.APPLICATION_JSON)
		@GET

		public Response getAgeAverage() {

			return Response.status(Status.ACCEPTED).entity(userService.getAgeAverage()).build();

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
		
		
		@Path("getAvailableUser")
		@Produces(MediaType.APPLICATION_JSON)
		@GET

		public Response getAvailableVolenteers() {

			return Response.status(Status.OK).entity(userService.getAvailableVolenteers()).build();

		}

	
}
