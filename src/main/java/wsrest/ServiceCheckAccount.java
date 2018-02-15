package wsrest;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import metier.BankAccount;
import metier.FormatedResponse;

@Path("/checkAccount")
public class ServiceCheckAccount {
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkAccount(@PathParam("id") String id) {
		FormatedResponse rep = new FormatedResponse();
		int status = 200;
		if(id != null) {
			Response response = ClientBuilder.newClient().target("https://inf63app11a.appspot.com/").path("accManager/" + id).request().get();
			FormatedResponse data = response.readEntity(FormatedResponse.class);
			if(response.getStatus() != 200) {
				rep.setMessage(data.getMessage());
				rep.setDone(data.getDone());
			}else {
				rep.setDone(true);
				rep.setMessage("Information correctly retrieved");
				if(data.getData() != null) {
					HashMap<String, Object> datas = (HashMap) data.getData();
					rep.setData(datas.get("risk"));
				}else {
					rep.setData((data.getData()));
				}
			}
		}else{
			rep.setMessage("Please specify an id");
			rep.setDone(false);
			status = 400;
		}
		return Response.status(status).entity(rep).build();
	}	
}
