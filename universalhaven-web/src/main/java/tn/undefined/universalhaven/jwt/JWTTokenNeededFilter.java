package tn.undefined.universalhaven.jwt;

import java.io.IOException;
import java.security.Key;

import javax.annotation.Priority;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import io.jsonwebtoken.Jwts;
import tn.undefined.universalhaven.enumerations.UserRole;
import tn.undefined.universalhaven.util.CallLimitExceededException;
import tn.undefined.universalhaven.util.KeyGenerator;
import tn.undefined.universalhaven.util.RestCallLoggerInterface;
import tn.undefined.universalhaven.util.SimpleKeyGenerator;

@Provider
@JWTTokenNeeded
@Priority(Priorities.AUTHENTICATION)

public class JWTTokenNeededFilter implements ContainerRequestFilter {
	
	
	RestCallLoggerInterface logger;
	private RestCallLoggerInterface getLogger() {
		if (logger==null) {
			InitialContext context;
			try {
				context = new InitialContext();
				logger= (RestCallLoggerInterface) context
						.lookup("java:global/universalhaven-ear/universalhaven-ejb/RestCallLogger!tn.undefined.universalhaven.util.RestCallLoggerInterface");
				
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return logger;
	}
	
	
	
	 @Context
	 private HttpServletRequest sr;
	
	@Context
	private ResourceInfo resourceInfo;

	private KeyGenerator keyGenerator = new SimpleKeyGenerator();

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		
		// Get the HTTP Authorization header from the request
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		System.out.println("#### authorizationHeader : " + authorizationHeader);

		String ip = sr.getRemoteAddr();
		try {
			String method = sr.getMethod();
			String endpoint = requestContext.getUriInfo().getPath().toString();
			System.out.println(method+" "+endpoint);
			getLogger().log(ip,method+" "+endpoint);
		}
		catch(CallLimitExceededException ex) {
			requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity("Call limit exceeded").build());
		}
		// Check if the HTTP Authorization header is present and formatted correctly
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			System.err.println("#### invalid authorizationHeader : " + authorizationHeader);
			throw new NotAuthorizedException("Authorization header must be provided");
		}

		// Extract the token from the HTTP Authorization header
		String token = authorizationHeader.substring("Bearer".length()).trim();

		try {

			// Validate the token
			UserRole role = (resourceInfo.getResourceMethod().getAnnotation(JWTTokenNeeded.class).role());

			Key key = keyGenerator.generateKey();
			String subj = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
			System.out.println("SUBJECT : " + subj);
			System.out.println("ROLE ANNOTATION : " + role);
			if (subj.contains(role.toString()) == false) {
				throw new Exception();
			}
			System.out.println("#### valid token : " + token);

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("#### invalid token : " + token);
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Invalid JWT Token").build());
		}
	}
}
