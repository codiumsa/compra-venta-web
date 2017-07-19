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

import py.com.compraventa.model.Compra;
import py.com.compraventa.model.CompraDetalle;

@ManagedBean
@ViewScoped
public class CompraMB implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{restClient}")
	RestClientMB restClientCompra;

	private Type type;

	private Compra entidad = new Compra();

	private String mensajeEliminar = "Esta seguro que desea eliminar el producto";

	private Gson gson = new Gson();

	private List<Compra> listaDataModel = new ArrayList<Compra>();
	
	private List<CompraDetalle> listaDetalle = new ArrayList<CompraDetalle>();
	
	protected static enum Estado {
		LISTA, MODIFICAR, AGREGAR, ELIMINAR
	}
	protected Estado estado;
	private boolean eliminar = false;
	private boolean agregar = false;
	private boolean modificar = false;
	private boolean modoSeleccionFila;	
	
	@PostConstruct	
	void postConstruct() {		
		ClientResponse response = restClientCompra.clientGetResponse("/compras");
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed service call: HTTP error code : " + response.getStatus());
        }
        // get productos as JSON        
        String compras = response.getEntity(String.class);        
		System.out.println(compras);
		type = new TypeToken<List<Compra>>() {}.getType();
		listaDataModel = gson.fromJson(compras, type);
		System.out.println(listaDataModel.size());				 
	}
	
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
	
	public void onClickAgregar() {
		System.out.println("onClickAgregar");		
		estado = Estado.AGREGAR;
		agregar = true;
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
	
	public void onClickVerDetalle () {
		//lamar al servicio para obtener detalle
		System.out.println("onClickVerDetalle");
		ClientResponse response = restClientCompra.clientGetResponse("/compras/".concat(entidad.getId().toString()).concat("/detalles"));
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed service call: HTTP error code : " + response.getStatus());
        }
        // get productos as JSON        
        String compraDetalles = response.getEntity(String.class);        
		System.out.println(compraDetalles);
		type = new TypeToken<List<CompraDetalle>>() {}.getType();
		listaDetalle = gson.fromJson(compraDetalles, type);
	}
	
	public void onClickCancelar() {				
		modoSeleccionFila = false;
		estado = null;		
		eliminar = false;
		//actualizar el datatable
	}
	
	//llamada que se hace al confirmar boton form
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
	
	public void agregar() {				
		addMessageInfo("Creado exitosamente: ");
		agregar = false;
	}

	
	public void modificar() {
		addMessageInfo("Creado exitosamente: ");
		//se quita fila seleccionada
		modoSeleccionFila = false;
		modificar = false;
	}
	
	public void eliminar() {					
		addMessageInfo("Eliminado exitosamente");
		eliminar = false;
	}
	
	public String returnList (){
		System.out.println("returnList");
		return "/resources/compra/admCompra?facesRedirect=true";
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
	
	public void onRowSelect(SelectEvent event) {
		modoSeleccionFila = true;
	}
	
	public void onRowUnSelect(UnselectEvent event) {
		modoSeleccionFila = false;
	}

	public RestClientMB getRestClientCompra() {
		return restClientCompra;
	}

	public void setRestClientCompra(RestClientMB restClient) {
		this.restClientCompra = restClient;
	}

	public Compra getEntidad() {
		return entidad;
	}

	public void setEntidad(Compra entidad) {
		this.entidad = entidad;
	}

	public String getMensajeEliminar() {
		return mensajeEliminar;
	}

	public void setMensajeEliminar(String mensajeEliminar) {
		this.mensajeEliminar = mensajeEliminar;
	}

	public Gson getGson() {
		return gson;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}

	public List<Compra> getListaDataModel() {
		return listaDataModel;
	}

	public void setListaDataModel(List<Compra> listaDataModel) {
		this.listaDataModel = listaDataModel;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public boolean isEliminar() {
		return eliminar;
	}

	public void setEliminar(boolean eliminar) {
		this.eliminar = eliminar;
	}

	public boolean isAgregar() {
		return agregar;
	}

	public void setAgregar(boolean agregar) {
		this.agregar = agregar;
	}

	public boolean isModificar() {
		return modificar;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}

	public boolean isModoSeleccionFila() {
		return modoSeleccionFila;
	}

	public void setModoSeleccionFila(boolean modoSeleccionFila) {
		this.modoSeleccionFila = modoSeleccionFila;
	}

	public List<CompraDetalle> getListaDetalle() {
		return listaDetalle;
	}

	public void setListaDetalle(List<CompraDetalle> listaDetalle) {
		this.listaDetalle = listaDetalle;
	}	


}
