package tn.undefined.universalhaven.resources;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import tn.undefined.universalhaven.buisness.FundraisingEventServiceLocal;
import tn.undefined.universalhaven.dto.FundraisingEventDto;
import tn.undefined.universalhaven.entity.FundraisingEvent;
import tn.undefined.universalhaven.entity.User;
import tn.undefined.universalhaven.enumerations.Urgency;
import tn.undefined.universalhaven.enumerations.UserRole;
import tn.undefined.universalhaven.jwt.JWTTokenNeeded;
/*http://localhost:18080/universalhaven-web/rest/fundraisingEvent/*/
@Path("fundraisingEvent")
@RequestScoped
public class FundraisingEventResource {
	
	@EJB
	FundraisingEventServiceLocal fundraisingEventService;
	
	/*{"title":"ddd","description":"eeee","goal":500,"imagePath":"aa.png","state":"In progress",
	  "urgency":"HIGH","publisher":1,"camp":2}*/
	@POST
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Produces(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role=UserRole.CAMP_MANAGER)
	public Response addEvent(FundraisingEvent event) {
		if(fundraisingEventService.startEvent(event)==true){
			return Response.status(Status.CREATED).entity(event).build();
		}
		return Response.status(Status.NOT_ACCEPTABLE).entity("event added failed").build();
		
	}
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded(role=UserRole.CAMP_MANAGER)
	public Response updateEvent(FundraisingEvent event) {
		if(fundraisingEventService.updateEvent(event)==true){
			return Response.status(Status.ACCEPTED).entity("event updated succesfully").build();
		}
		return Response.status(Status.NOT_ACCEPTABLE).entity("event update failed").build();
		
	}
	//@JWTTokenNeeded(role=UserRole.SUPER_ADMIN)
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(){
		if(fundraisingEventService.listActiveEventsDto()==null)
			return null;
		return Response.status(Status.OK).entity(fundraisingEventService.listActiveEventsDto()).build();
	}
	
	@Path("avgCompletionEvent")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAvgCompletionEvent(){
		if(fundraisingEventService.getAverageCompletionDate()==null)
			return null;
		return Response.status(Status.OK).entity(fundraisingEventService.getAverageCompletionDate()).build();
		
	}
	//http://localhost:18080/universalhaven-web/rest/fundraisingEvent/stat	
	@Path("eventByCountry")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCountEventPerCountry(){
		if(fundraisingEventService.getEventCountByCountry()==null)
			return null;
		return Response.status(Status.OK).entity(fundraisingEventService.getEventCountByCountry()).build();
		
	}
	/*{"id":1}*/
	@Path("user")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEventByUser(User user){
		if(fundraisingEventService.listEventsByUserDto(user)==null)
			return null;
		return Response.status(Status.ACCEPTED).entity(fundraisingEventService.listEventsByUserDto(user)).build();
		
	}
	
	@Path("event")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEvent(@QueryParam("state") String state,@QueryParam("urgency") Urgency urgency,
			@QueryParam("year") int year,@QueryParam("month") int month,@QueryParam("annee") int annee,@QueryParam("idEvent")long id
			,@QueryParam("idFundraisingEvent")long idEvent,@QueryParam("idUser")long idUser){
		if(state!=null){
			if(fundraisingEventService.listEventsByState(state)==null)
				Response.status(Status.NOT_ACCEPTABLE).build();
			return Response.status(Status.OK).entity(fundraisingEventService.listEventsByState(state)).build();
		}
		if(urgency!=null){
			if(fundraisingEventService.listEventsByUrgency(urgency)==null)
				Response.status(Status.NOT_ACCEPTABLE).build();
			return Response.status(Status.OK).entity(fundraisingEventService.listEventsByUrgency(urgency)).build();
			
		}
		if(year!=0){
			if(fundraisingEventService.getCountEventByMonth(year)==null)
				return null;
			return Response.status(Status.OK).entity(fundraisingEventService.getCountEventByMonth(year)).build();
			
		}
		if((month!=0)||(annee!=0)){
			if(fundraisingEventService.listEventsByYearAndMonth(month,annee)==null)
				return null;
			return Response.status(Status.OK).entity(fundraisingEventService.listEventsByYearAndMonth(month,annee)).build();
		}
		if(id!=0){
			if(fundraisingEventService.findFundraisingEvent(id)==null)
				return null;
			return Response.status(Status.OK).entity(fundraisingEventService.findFundraisingEvent(id)).build();
		}
		if(idEvent!=0){
			if(fundraisingEventService.getSumAmountByEvent(idEvent)<0)
				return null;
			return Response.status(Status.OK).entity(fundraisingEventService.getSumAmountByEvent(idEvent)).build();
		}
		if(idUser!=0){
			if(fundraisingEventService.listEventsByUserIdDto(idUser)==null)
				return null;
			return Response.status(Status.OK).entity(fundraisingEventService.listEventsByUserIdDto(idUser)).build();
		}
		return Response.status(Status.NOT_FOUND).build();
		
	}
	
	public void genererExcel() throws IOException,WriteException{
		List<FundraisingEventDto> liste=fundraisingEventService.listActiveEventsDto();
		try{
			String filename="C:\\Users\\Dell\\Desktop\\4TWIN\\excel.xls";
			/*WritableWorkbook workbook=Workbook.createWorkbook(new File(filename));
			WritableSheet sheet=workbook.createSheet("Sheet1",0 );*/
			WritableWorkbook book= Workbook.createWorkbook(new File(filename)); 
			String title[]={"title","description","publish date","camp","user","amount","state","urgency"};
		       WritableSheet sheet=book.createSheet("???",0); 
		       sheet.setColumnView(0, 35);
		       sheet.setColumnView(1, 50);
		       sheet.setColumnView(2, 15);
		       sheet.setColumnView(3, 15);
		       sheet.setColumnView(4, 15);
		       sheet.setColumnView(5, 15);
		       sheet.setColumnView(6, 15);
		       sheet.setColumnView(7, 100);
		       for(int i=0;i<8;i++) {
		           sheet.addCell(new Label(i,0,title[i])); 
		       }
			for(int j=0; j<liste.size(); j++) {
				double goalDouble = liste.get(j).getGoal();
				String goalString = String.valueOf(goalDouble);
		   		sheet.addCell(new Label(0, j+1, liste.get(j).getTitle()));
		   		sheet.addCell(new Label(1, j+1, liste.get(j).getDescription()));
		   		sheet.addCell(new Label(2, j+1, liste.get(j).getPublishDate().toString()));
		   		sheet.addCell(new Label(3, j+1, liste.get(j).getCamp().getAddress()));
		   		sheet.addCell(new Label(4, j+1, liste.get(j).getUser().getLogin()));
		   		sheet.addCell(new Label(5, j+1, goalString));
		   		sheet.addCell(new Label(6, j+1, liste.get(j).getState()));
		   		sheet.addCell(new Label(7, j+1, liste.get(j).getUrgency().toString()));
		   	}
			
			
			book.write();
			book.close();
			
		}catch(WriteException e){
			
		}
	}

    @GET  
    @Path("excel")  
    @Produces("application/vnd.ms-excel")
    public Response getFile() throws WriteException, IOException {
    	genererExcel();
        File file = new File("C:\\Users\\Dell\\Desktop\\4TWIN\\excel.xls");  
        ResponseBuilder response = Response.ok((Object) file);  
        response.header("Content-Disposition","attachment; filename=listFundraisingEvents.xls");  
        return response.build();  
    }  

}
