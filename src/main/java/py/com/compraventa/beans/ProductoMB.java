package py.com.compraventa.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import py.com.compraventa.model.Producto;

@ManagedBean
@ViewScoped
public class ProductoMB implements Serializable{
	private static final long serialVersionUID = 1L;	
	
	private Producto entidad = new Producto();	
	
	private String mensajeEliminar = "";
	
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
	
	
	@PostConstruct	
	void postConstruct() {
		
	}
	
	public Producto getEntidad() {
		return entidad;
	}


	public void setEntidad(Producto entidad) {
		this.entidad = entidad;
	}	
	
	private List<Producto> listaDataModel = new ArrayList<Producto>(
			Arrays.asList(
				      new Producto(1L, "BA", "Banana", 35, 1000L),
				      new Producto(1L, "BA", "Banana", 35, 1000L),
				      new Producto(1L, "BA", "Banana", 35, 1000L),
				      new Producto(1L, "BA", "Banana", 35, 1000L),
				      new Producto(1L, "BA", "Banana", 35, 1000L)
				   )); 

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
		addMessageInfo("Creado exitosamente");
	}

	
	public void modificar() {
		System.out.println("modificar");
	}

	
	public boolean isModificar() {
		return modificar;
	}

	public String returnList (){
		System.out.println("returnList");
		return "/resources/producto/admProducto?facesRedirect=true";
	}
	
	public void eliminar() {		
		addMessageInfo("Eliminado exitosamente");
		eliminar = false;
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
	}
	
	public void onRowSelect(SelectEvent event) {
		modoSeleccionFila = true;
	}
	
	public void onRowUnSelect(UnselectEvent event) {
		modoSeleccionFila = false;
	}

}
