package py.com.compraventa.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


@ManagedBean(name="restClient", eager=true)
@SessionScoped
public class RestClientMB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private transient Client client;
	
	public String SERVICE_BASE_URL;	

	@PostConstruct
    protected void initialize() {
        FacesContext fc = FacesContext.getCurrentInstance();
        SERVICE_BASE_URL = fc.getExternalContext().getInitParameter("metadata.serviceBaseURL");
        client = Client.create();
    }

    public WebResource getWebResource(String relativeUrl) {
        if (client == null) {
            initialize();
        }

        return client.resource(SERVICE_BASE_URL + relativeUrl);
    }

    public ClientResponse clientGetResponse(String relativeUrl) {
        WebResource webResource = client.resource(SERVICE_BASE_URL + relativeUrl);
        return webResource.accept("application/json").get(ClientResponse.class);
    }
    
    public ClientResponse clientPostResponse(String relativeUrl, String entidad) {
        WebResource webResource = client.resource(SERVICE_BASE_URL + relativeUrl);
        return webResource.accept("application/json").type("application/json").post(ClientResponse.class, entidad);
    }
    
    public ClientResponse clientPutResponse(String relativeUrl, String entidad) {
        WebResource webResource = client.resource(SERVICE_BASE_URL + relativeUrl);
        return webResource.accept("application/json").type("application/json").put(ClientResponse.class, entidad);
    }
    
    public ClientResponse clientDeleteResponse(String relativeUrl, String id) {
        WebResource webResource = client.resource(SERVICE_BASE_URL + relativeUrl);
        return webResource.accept("application/json").type("application/json").delete(ClientResponse.class, id);
    }
    
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public String getSERVICE_BASE_URL() {
		return SERVICE_BASE_URL;
	}

	public void setSERVICE_BASE_URL(String sERVICE_BASE_URL) {
		SERVICE_BASE_URL = sERVICE_BASE_URL;
	}

}
