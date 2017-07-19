package py.com.compraventa.model;

import java.io.Serializable;


public class OrdenCompra implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;

	private Producto producto;

	private Integer cantidad;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}		

}
