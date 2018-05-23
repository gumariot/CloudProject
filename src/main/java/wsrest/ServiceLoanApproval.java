package wsrest;

import java.util.HashMap;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import metier.Approval;
import metier.BankAccount;
import metier.FormatedResponse;
import metier.ManualResponse;

@Path("/loanApproval")
public class ServiceLoanApproval {
	
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response loanRequest(@FormParam("id") String id, @FormParam("name") String name, @FormParam("amount") String amount) {
		FormatedResponse rep = new FormatedResponse();
		int status = 200;
		if(id != null) {
			if(Integer.parseInt(amount) < 10000) {
				Response responseAcc = ClientBuilder.newClient().target("https://appcloud-195409.appspot.com/").path("accManager/" + id).request(MediaType.APPLICATION_JSON).get();
				FormatedResponse data = responseAcc.readEntity(FormatedResponse.class);
				if(responseAcc.getStatus() != 200) {
					rep.setMessage(data.getMessage());
					rep.setDone(data.getDone());
				}else{
					HashMap<String, Object> datas = (HashMap) data.getData();
					if(datas != null){
						if(datas.get("risk").equals("high")){
							Response responseApp = ClientBuilder.newClient().target("https://rocky-sierra-35610.herokuapp.com/").path("appManager/getApproval/" + id).request(MediaType.APPLICATION_JSON).get();
							FormatedResponse dataResponse = responseApp.readEntity(FormatedResponse.class);
							if(responseApp.getStatus() != 200) {
								rep.setMessage(dataResponse.getMessage());
								rep.setDone(dataResponse.getDone());
							}else{
								HashMap<String, Object> datasReponse = (HashMap) dataResponse.getData();
								if(ManualResponse.valueOf((String) datasReponse.get("manualResponse")) == ManualResponse.REFUSED){
									rep.setMessage("You are not allowed to perform this loan");
									rep.setDone(true);
								}else{
									rep = requestUpdateAccount(amount, id);
								}
							}
						}else{
							rep = requestUpdateAccount(amount, id);
						}
					}else{
						rep.setMessage("This account doesn't exist");
						rep.setDone(false);
					}
				}
			}else{
				Response responseApp = ClientBuilder.newClient().target("https://rocky-sierra-35610.herokuapp.com/").path("appManager/getApproval/" + id).request(MediaType.APPLICATION_JSON).get();
				FormatedResponse dataResponse = responseApp.readEntity(FormatedResponse.class);
				if(responseApp.getStatus() != 200) {
					rep.setMessage(dataResponse.getMessage());
					rep.setDone(dataResponse.getDone());
				}else{
					HashMap<String, Object> datas = (HashMap) dataResponse.getData();
					if(ManualResponse.valueOf((String) datas.get("manualResponse")) == ManualResponse.REFUSED){
						rep.setMessage("You are not allowed to perform this loan");
						rep.setDone(true);
					}else{
						rep = requestUpdateAccount(amount, id);
					}					
				}
			}
		}else{
			rep.setMessage("Please specify an id");
			rep.setDone(false);
		}
		return Response.status(status).entity(rep).build();
	}
	
	private FormatedResponse requestUpdateAccount(String amount, String id) {
		FormatedResponse rep = new FormatedResponse();
		Client client = ClientBuilder.newClient();
    	WebTarget target = client.target("https://appcloud-195409.appspot.com/").path("accManager/updateAccount");
    	
    	Form formData = new Form();
    	formData.param("id", id);
    	formData.param("amount",amount);
    	 
    	Response responseUpdate = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(formData, MediaType.APPLICATION_FORM_URLENCODED));
    	FormatedResponse dataResponseUpdate = responseUpdate.readEntity(FormatedResponse.class);
		rep.setMessage(dataResponseUpdate.getMessage());
		rep.setDone(dataResponseUpdate.getDone());
		return rep;
	}
}
