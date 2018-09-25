package tn.undefined.universalhaven.resources;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class ResponseUtil {
	public static Response buildOk(Object message) {
		return Response.ok().entity(message).build();
	}
	public static Response buildError(Object message) {
		return Response.status(Status.BAD_REQUEST).entity(message).build();
	}
}
