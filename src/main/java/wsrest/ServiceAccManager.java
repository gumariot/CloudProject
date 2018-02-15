package wsrest;


import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.googlecode.objectify.ObjectifyService;

import metier.BankAccount;
import metier.FormatedResponse;

@Path("/accManager")
public class ServiceAccManager {
	
	static {
		ObjectifyService.register(BankAccount.class);
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBankAccounts() {
		FormatedResponse rep = new FormatedResponse();
		List<BankAccount> accounts = ofy().load().type(BankAccount.class).list();
		rep.setData(accounts);
		if(!accounts.isEmpty()) {
			rep.setMessage("Request correctly handled");
			rep.setDone(true);
		}else {
			rep.setMessage("No data to display");
			rep.setDone(false);
		}
		return Response.status(200).entity(rep).build();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBankAccount(@PathParam("id") String id) {
		FormatedResponse rep = new FormatedResponse();
		BankAccount account = ofy().load().type(BankAccount.class).id(id).now();
		rep.setData(account);
		if(account != null) {
			rep.setMessage("Request correctly handled");
			rep.setDone(true);
		}else {
			rep.setMessage("No data to display");
			rep.setDone(false);
		}
		return Response.status(200).entity(rep).build();
	}
	
	@POST
	@Path("/addAccount")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addBankAccounts(@FormParam("name") String name, 
									@FormParam("lastname") String lastname,
									@FormParam("account") String account,
									@FormParam("risk") String risk) {
		FormatedResponse rep = new FormatedResponse();
		int status = 200;
		if((risk != null) || (account != null) || (lastname != null) || (name != null)) {
			Float amount = Float.valueOf(account);
			BankAccount ba = new BankAccount(name,lastname,amount,risk);
			ofy().save().entity(ba).now();
			rep.setMessage("Request correctly handled");
			rep.setDone(true);
			rep.setData(ba);
		}else {
			rep.setMessage("Error with the parameters given");
			rep.setDone(false);
			status = 400;
		}
		return Response.status(status).entity(rep).build();
	}
	
	@DELETE
	@Path("/deleteAccount")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAccount(@FormParam("id") String id) {
		FormatedResponse rep = new FormatedResponse();
		int status = 200;
		if(id != null){			
		 	ofy().delete().type(BankAccount.class).id(id).now();
			rep.setMessage("Request correctly handled");
			rep.setDone(true);
		}else {
			rep.setMessage("Error with the parameters given");
			rep.setDone(false);
			status = 400;
		}
		return Response.status(status).entity(rep).build();
	}
	
}
