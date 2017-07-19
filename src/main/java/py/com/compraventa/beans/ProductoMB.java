package py.com.compraventa.beans;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.ClientResponse;

import py.com.compraventa.model.Producto;

@ManagedBean
@ViewScoped
public class ProductoMB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{restClient}")
	RestClientMB restClient;
	
	private Type type;
	
	private Producto entidad = new Producto();		
	
	private String mensajeEliminar = "Esta seguro que desea eliminar el producto";
	
	private Gson gson = new Gson();
	
	private List<Producto> listaDataModel = new ArrayList<Producto>();
	
	
	@PostConstruct	
	void postConstruct() {
		ClientResponse response = restClient.clientGetResponse("/productos");
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed service call: HTTP error code : " + response.getStatus());
        }
        // get productos as JSON        
        String productos = response.getEntity(String.class);        
		System.out.println(productos);
		type = new TypeToken<List<Producto>>() {}.getType();
		listaDataModel = gson.fromJson(productos, type);
		System.out.println(listaDataModel.size());		 
	}
	
	public RestClientMB getRestClient() {
		return restClient;
	}

	public void setRestClient(RestClientMB restClient) {
		this.restClient = restClient;
	}

	public String getMensajeEliminar() {
		return mensajeEliminar;
	}

	public void setMensajeEliminar(String mensajeEliminar) {
		this.mensajeEliminar = mensajeEliminar;
	}

	protected static enum Estado {
		LISTA, MODIFICAR, AGREGAR, ELIMINAR
	}
	
	private boolean eliminar = false;
	private boolean agregar = false;
	private boolean modificar = false;
	
	private boolean modoSeleccionFila;	
	
	public boolean isModoSeleccionFila() {
		return modoSeleccionFila;
	}

	public void setModoSeleccionFila(boolean modoSeleccionFila) {
		this.modoSeleccionFila = modoSeleccionFila;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}

	public boolean isAgregar() {
		return agregar;
	}

	public void setAgregar(boolean agregar) {
		this.agregar = agregar;
	}

	public boolean isEliminar() {
		return eliminar;
	}

	public void setEliminar(boolean eliminar) {
		this.eliminar = eliminar;
	}

	protected Estado estado;
	
	public String stringEstado() {
		if (estado == null)
			return "";
		else {
			String s = estado.toString();
			return s.substring(0, 1).toUpperCase()
					+ s.substring(1).toLowerCase() + " "
					+ entidad.getClass().getSimpleName();
		}

	}
	
	public Producto getEntidad() {
		return entidad;
	}


	public void setEntidad(Producto entidad) {
		this.entidad = entidad;
	}	

	public List<Producto> getListaDataModel() {
		return listaDataModel;
	}


	public void setListaDataModel(List<Producto> listaDataModel) {
		this.listaDataModel = listaDataModel;
	}


	public void onClickAgregar() {
		System.out.println("onClickAgregar");
		entidad = new Producto();
		estado = Estado.AGREGAR;
		agregar = true;
	}
	
	public void procesar() {

		if (isAgregar()) {
			agregar();

		}

		if (isModificar()) {
			modificar();
		}

		if (isEliminar()) {
			eliminar();

		}

	}
	
	
	public void onClickModificar() {
		System.out.println("onClickModificar");
		estado = Estado.MODIFICAR;
		modificar = true;
	}

	
	public void onClickEliminar() {
		System.out.println("onClickEliminar");
		estado = Estado.ELIMINAR;
		eliminar = true;
	}

	
	public void agregar() {		
		System.out.println("agregar");		
		//llamar a servicio de backend
		String jsonProducto = gson.toJson(entidad);
		ClientResponse response = restClient.clientPostResponse("/productos", jsonProducto);
        if (response.getStatus() != 200) {
        	addMessageInfo("Error al agregar Producto");
            throw new RuntimeException("Failed service call: HTTP error code : " + response.getStatus());            
        }
        // get productos as JSON  
        String createProducto = response.getEntity(String.class);
        type = new TypeToken<Producto>() {}.getType();
		Producto producto = gson.fromJson(createProducto, type);
		addMessageInfo("Creado exitosamente: ".concat(producto.getNombre()));
		agregar = false;
	}

	
	public void modificar() {
		System.out.println("modificar");
		String jsonProducto = gson.toJson(entidad);
		ClientResponse response = restClient.clientPutResponse("/productos/".concat(entidad.getId().toString()), jsonProducto);
        if (response.getStatus() != 200) {
        	addMessageInfo("Error al modificar Producto");
            throw new RuntimeException("Failed service call: HTTP error code : " + response.getStatus());            
        }
        // get productos as JSON  
        String modificarProducto = response.getEntity(String.class);
        type = new TypeToken<Producto>() {}.getType();
		Producto producto = gson.fromJson(modificarProducto, type);
		addMessageInfo("Creado exitosamente: ".concat(producto.getNombre()));
		//se quita fila seleccionada
		modoSeleccionFila = false;
		modificar = false;
	}
	
	public void eliminar() {			
		ClientResponse response = restClient.clientDeleteResponse("/productos/".concat(entidad.getId().toString()), entidad.getId().toString());
        if (response.getStatus() != 200) {
        	addMessageInfo("Error al modificar Producto");
            throw new RuntimeException("Failed service call: HTTP error code : " + response.getStatus());            
        }        
        entidad = null;
        estado = null;
		addMessageInfo("Eliminado exitosamente");
		eliminar = false;
	}

	
	public boolean isModificar() {
		return modificar;
	}

	public String returnList (){
		System.out.println("returnList");
		return "/resources/producto/admProducto?facesRedirect=true";
	}
	
	
	
	public void addMessageInfo(String message) {
		addMessage(FacesMessage.SEVERITY_INFO, message);
	}

	public void addMessageError(String message) {
		addMessage(FacesMessage.SEVERITY_ERROR, message);
	}

	public void addMessage(Severity severityInfo, String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(severityInfo, "Operación realizada", message));
	}
	
	public void onClickCancelar() {				
		modoSeleccionFila = false;
		estado = null;		
		eliminar = false;
		//actualizar el datatable
	}
	
	public void onRowSelect(SelectEvent event) {
		modoSeleccionFila = true;
	}
	
	public void onRowUnSelect(UnselectEvent event) {
		modoSeleccionFila = false;
	}

}
