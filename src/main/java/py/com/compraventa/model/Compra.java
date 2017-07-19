package py.com.compraventa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Compra implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private List<CompraDetalle> detalles = new ArrayList<>();
		
	private String fecha;
	
	private Long total;
	
	public Compra() {
		// TODO Auto-generated constructor stub
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<CompraDetalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<CompraDetalle> detalles) {
		this.detalles = detalles;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

}
