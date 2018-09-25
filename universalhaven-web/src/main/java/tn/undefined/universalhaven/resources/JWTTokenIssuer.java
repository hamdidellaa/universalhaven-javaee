package tn.undefined.universalhaven.resources;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import tn.undefined.universalhaven.buisness.UserServiceLocal;
import tn.undefined.universalhaven.buisness.UserServiceLocalMehdi;
import tn.undefined.universalhaven.enumerations.UserRole;
import tn.undefined.universalhaven.util.KeyGenerator;
import tn.undefined.universalhaven.util.SimpleKeyGenerator;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class JWTTokenIssuer {
	 	
	
		@EJB
		UserServiceLocal userService;
		
	    private static KeyGenerator keyGenerator = new SimpleKeyGenerator();
	 
	    @POST
	    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	    public Response authenticateUser(@FormParam("login") String login,
	                                     @FormParam("password") String password) {
	        try {
	        	System.out.println("Authenticating "+login+" -- "+password);
	            // Authenticate the user using the credentials provided
	            UserRole role = userService.authenticate(login, password);
	           
	            // Issue a token for the user
	            String token = issueToken(login,role);
	 
	            // Return the token on the response
	            return Response.ok().entity("Bearer " + token).header("AUTHORIZED", "Bearer " + token).build();
	 
	        } catch (Exception e) {
	            return Response.status(Response.Status.UNAUTHORIZED).build();
	        }
	    }
	    
	   
	    
	    public static String issueToken(String login,UserRole role) {
	        Key key = keyGenerator.generateKey();
	        Calendar c =Calendar.getInstance();
	        c.add(Calendar.MONTH, 1);
	        String jwtToken = Jwts.builder()
	                .setSubject(login+"-"+role)
	                .setIssuer("universal-haven.com")
	                .setIssuedAt(new Date())
	                .setExpiration(c.getTime() )
	                .signWith(SignatureAlgorithm.HS512, key)
	                .compact();
	        return jwtToken;
	    }
}
