package tn.undefined.universalhaven.resources;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.undefined.universalhaven.buisness.DonationServiceLocal;
import tn.undefined.universalhaven.buisness.FundraisingEventServiceLocal;
import tn.undefined.universalhaven.buisness.PaypalServiceLocal;
import tn.undefined.universalhaven.buisness.PaypalServiceRemote;
import tn.undefined.universalhaven.buisness.StripeServiceLocal;
import tn.undefined.universalhaven.buisness.StripeServiceRemote;
import tn.undefined.universalhaven.entity.Donation;
import tn.undefined.universalhaven.enumerations.UserRole;
import tn.undefined.universalhaven.jwt.JWTTokenNeeded;
import tn.undefined.universalhaven.util.DonationParam;

@Path("donation")
@RequestScoped
public class DonationResource {

	@EJB
	PaypalServiceLocal servicePaypal;

	@EJB
	StripeServiceLocal serviceStripe;

	@EJB
	DonationServiceLocal serviceDonation;
	@EJB
	FundraisingEventServiceLocal fundraisingEventService;
	
	
	
	
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Donation> getAll() {
		return serviceDonation.getAll();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response donate(DonationParam param) {
		param.getDonation().setDonationDate(new Date());
		if (param.getMethod().equals("paypal")) {
			System.out.println(String.valueOf(param.getDonation().getAmount()));
			String reference = servicePaypal.pay(String.valueOf(param.getDonation().getAmount()), param.getCreditCardType()
					, param.getCreditCardNumber(),
					param.getExpireMonth(), param.getExpireYear(), param.getCvv2(), param.getDonation().getContributorName(), param.getDonation().getContributorName());
			if (reference.equals("") == false) {
				param.getDonation().setPaymentMethod("paypal");
				param.getDonation().setTransactionReference(reference);
				serviceDonation.add(param.getDonation());
				if (param.getDonation().getFundraisingEvent()!=null)
					fundraisingEventService.changeEventState(param.getDonation().getFundraisingEvent());
				return ResponseUtil.buildOk("Paypal payment successful and donation persisted");

			} else {
				return Response.status(Status.EXPECTATION_FAILED).entity("Paypal payment failed").build();
			}
		} else {
			Double amountDouble = param.getDonation().getAmount();
			int amountInt = amountDouble.intValue();
			String reference = serviceStripe.pay(param.getToken(), amountInt, param.getDonation().getContributorName());
			if (reference.equals("") == false) {
				param.getDonation().setPaymentMethod("stripe");
				param.getDonation().setTransactionReference(reference);
				serviceDonation.add(param.getDonation());
				if (param.getDonation().getFundraisingEvent()!=null)
					fundraisingEventService.changeEventState(param.getDonation().getFundraisingEvent());
				return ResponseUtil.buildOk("Stripe payment successful and donation persisted");
			}
			return Response.status(Status.EXPECTATION_FAILED).entity("Stripe payment failed").build();
		}
	}

	@GET
	@Path("bycountry")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response donationsPerCountry() {
		return ResponseUtil.buildOk(serviceDonation.getDonationsByCountry());
	}

	@GET
	@Path("/bycountry/average")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response countriesDonationAverage() {
		return ResponseUtil.buildOk(serviceDonation.getCountriesDonationAverage());
	}

	@GET
	@Path("bydate/overall")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response overAllDonationsByDate() {
		Map<String, Double> stats = new HashMap<>();
		stats.put("thismonth", serviceDonation.getDonationsThisMonth());
		stats.put("thisyear", serviceDonation.getDonationsThisYear());
		stats.put("today", serviceDonation.getDonationsToday());
		return ResponseUtil.buildOk(stats);
	}

	@GET
	@Path("bydate")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response donationsPerDay() {
		return ResponseUtil.buildOk(serviceDonation.getDonationsByDay());
	}

	@GET
	@Path("bymethod")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response donationsByMethod() {
		return ResponseUtil.buildOk(serviceDonation.getDonationsByPaymentMethod());
	}

	@GET
	@Path("average")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response averageDonation() {
		return ResponseUtil.buildOk(serviceDonation.getAverage());
	}
	
	@GET
	@Path("bydate/bymonth")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(role = UserRole.ICRC_MANAGER)
	public Response donationsByMonth() {
		return ResponseUtil.buildOk(serviceDonation.getDonationsByMonth());
	}


}
