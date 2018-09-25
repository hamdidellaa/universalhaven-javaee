package tn.undefined.universalhaven.rest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import tn.undefined.universalhaven.entity.ApplicationForm;
import tn.undefined.universalhaven.services.ApplicationFormServiceLocal;

@Stateless
@Path("application")
public class ApplicationFromResource {

	@EJB
	ApplicationFormServiceLocal applicationForm;
	
	
/////////////////////
	@POST
	//@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response apply(ApplicationForm app) {
		int test = applicationForm.apply(app) ;
		if (test == 1)
			
		return Response.status(Status.OK).entity(app.getId()).build();


		if (test == -1)
			return Response.status(Status.NOT_ACCEPTABLE).entity("you already submit an application form").build();

		
		return Response.status(Status.CONFLICT).entity("please check ur applicationForm Json").build();

	}

	/*@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAPPlcationForm() {

		ApplicationForm f = new ApplicationForm();
		return Response.status(Status.OK).entity(f).build();

	}
*/
	
	
	
	
///////////////////////////////////
	@GET
	//@Path("listApplicationCountry")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listApplicationPerCountry(@QueryParam(value = "country") String country) {
		return Response.status(Status.OK).entity(applicationForm.listApplicationPerCountry(country)).build();

	}
	
	
	
////////////////////////////
	@GET
	//@Path("listApplicationPerGender")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listApplicationPerGender(@QueryParam(value = "gender") String gender) {

		return Response.status(Status.OK).entity(applicationForm.listApplicationPerGender(gender)).build();

	}
	
	
///////////////////////////
	//@Produces(MediaType.TEXT_PLAIN)
	//@Path("reviewApplication")
	@Consumes(MediaType.APPLICATION_JSON)
	@PUT
	public Response reviewApplication(ApplicationForm application, @QueryParam(value = "review") boolean review,
			@QueryParam(value = "revieww") long revieww) {
		long res = applicationForm.reviewApplication(application, review, revieww);
		if (res != -1)
			return Response.status(Status.ACCEPTED).entity(res).build();
		return Response.status(Status.FORBIDDEN).entity("error,only ICRC manger can handle this").build();
		
	}
	
	
	
///////////////////////////////////////
	// ApplicationForm application, String name
	//@Path("{applicationform}")
	@POST
	@Consumes("multipart/form-data")
	//@Produces(MediaType.TEXT_PLAIN)
	public Response addAttachment(MultipartFormDataInput input) {
		
		List<String> formatFile = new ArrayList<String>();
		formatFile.add("jpeg");
		formatFile.add("jpg");
		formatFile.add("png");
		int idApplication =0;
		
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		
		
	//	List<InputPart> inputParts = uploadForm.get("file2");
		List<InputPart> id = uploadForm.get("application");
		
		// recuperation du l id from form //
		String[] contentDisposition = new String[1000]  ;
		for (InputPart inputPart : id){
			MultivaluedMap<String, String> headers = inputPart.getHeaders();
			 contentDisposition = headers.getFirst("Content-Disposition").split(";");
			 try {
				String idRecupere = inputPart.getBodyAsString();
				idApplication = Integer.parseInt(idRecupere);
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		// recuperation du l id from form //
		
		for (String key : uploadForm.keySet()){
			if(!key.equalsIgnoreCase("application"))
			{
				List<InputPart> inputParts = uploadForm.get(key);
				addImageh(inputParts,idApplication,formatFile);
			}
			
			System.out.println(key);
		}
		
		
		
		
		
		return Response.status(Status.OK).entity("Attachemnt added  ").build();
		
		
		
	}

	private String getFileName(MultivaluedMap<String, String> headers) {
		String[] contentDisposition = headers.getFirst("Content-Disposition").split(";");

		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {

				String[] name = filename.split("=");

				String finalFileName = sanitizeFilename(name[1]);
				return finalFileName;
			}
		}
		return "unknown";
	}
	
	

	//////////////////////////////
	private String sanitizeFilename(String s) {
		return s.trim().replaceAll("\"", "");
	}
	
	
	//////////////////////////////////
	public boolean addImageh(List<InputPart> inputParts , int idApplication , List<String> formatFile)
	{
		for (InputPart inputPart : inputParts) {
			MultivaluedMap<String, String> headers = inputPart.getHeaders();
			try {
				InputStream inputStream = inputPart.getBody(InputStream.class, null);
				byte[] bytes = IOUtils.toByteArray(inputStream);
				String filename = getFileName(headers);

				// test for file format //

				String extension = "";
				int i = filename.lastIndexOf('.');
				if (i >= 0) {
					extension = filename.substring(i + 1);
				}

				if (!formatFile.contains(extension)) {
					/*return Response.status(Status.NOT_ACCEPTABLE)
							.entity("Format not supported  please use .jpeg .jpg .png  format").build();*/
					return false ;
				}
				// test for file format //

				System.out.println(filename);
				String temp =  UUID.randomUUID().toString() + filename ;
				String fileLocation = "E:\\hamdi\\D\\Hamdi\\etude\\4Twin\\pi\\dotNet\\universalhaven-dotnet\\Web\\Content\\Images\\attachments\\"
						+ temp;

				FileOutputStream fileOuputStream = new FileOutputStream(fileLocation);
				fileOuputStream.write(bytes);
				int iu = applicationForm.addAttachment(idApplication, temp);
				if(iu==-1)
				{
					//return Response.status(Status.NOT_ACCEPTABLE).entity("application does not exist ").build();
					return false ;
				}
				// saveFile(inputStream, fileLocation);
				// TODO: HERE you do whatever you want to do with the file
				// ...

			} catch (IOException e) {
				//return Response.status(Status.NOT_ACCEPTABLE).entity("this aaplication does not exist ").build();
					return false ;
			}
		}
		return true ;
	}
	
	
		///////////////////////////////////
		@GET
		@Path("listApplication")
		@Produces(MediaType.APPLICATION_JSON)
		public Response listApplication() {
				
		return Response.status(Status.OK).entity(applicationForm.listApplication()).build();
				
		}
}