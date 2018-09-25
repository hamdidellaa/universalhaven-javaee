package tn.undefined.universalhaven.resources;

import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
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

import tn.undefined.universalhaven.buisness.StripeServiceLocal;
import tn.undefined.universalhaven.buisness.SubsriptionServiceLocal;
import tn.undefined.universalhaven.entity.Subscription;
import tn.undefined.universalhaven.enumerations.UserRole;
import tn.undefined.universalhaven.jwt.JWTTokenNeeded;
import tn.undefined.universalhaven.util.SimpleKeyGenerator;

@RequestScoped
@Path("subscription")
public class SubscriptionResource {

	@EJB
	private StripeServiceLocal serviceStripe;

	@EJB
	private SubsriptionServiceLocal serviceSub;

	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response cancelSubscription(@FormParam(value = "customer_email") String customerEmail) {
		Subscription sub = serviceSub.getSubscriptionByEmail(customerEmail);
		if (sub == null) {
			return Response.status(Status.NOT_FOUND).entity("Invalid email").build();
		}

		serviceSub.notifyUser(sub.getEmail(), SimpleKeyGenerator.crypt(sub.getId()));

		return Response.ok("Confirmation email sent").build();
	}

	@GET
	@Path("unsubscribe")
	@Produces(MediaType.TEXT_HTML)
	public Response confirm(@QueryParam(value = "token") String token) {
		System.out.println(token);
		System.out.println(SimpleKeyGenerator.decrypt(token));
		Subscription sub = serviceSub.getById(SimpleKeyGenerator.decrypt(token));
		if (sub == null) {
			return Response.status(Status.NOT_FOUND).entity("<h1>Invalid token</h1>").build();
		}
		if (serviceStripe.cancelSubscription(sub.getCustomer_id())) {
			serviceSub.cancelSubscription(sub.getId());
			return Response.ok("<h1>Subscription canceled</h1>").build();
		}
		return Response.status(Status.BAD_REQUEST).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response subscribe(@FormParam(value = "plan") String plan, @FormParam(value = "email") String email,
			@FormParam(value = "name") String name, @FormParam(value = "token") String token) {

		String custId = serviceStripe.addSubscription(plan, email, name, token);
		if ((custId == null) || (custId.equals(""))) {
			return Response.status(Status.BAD_REQUEST).entity("Invalid card token").build();
		}
		Subscription sub = new Subscription();
		sub.setCustomer_id(custId);
		sub.setPlan(plan);
		sub.setName(name);
		sub.setEmail(email);
		serviceSub.addSubscription(sub);
		return Response.ok("Subscription succesful").build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response getSubscriptionsByPlan() {
		return Response.ok(serviceSub.getNumberOfSubscriptionsByPlan()).build();
	}

}
