package py.com.compraventa.model;


public class Producto {
	
	
	private Long id;

	private String nombre;
	
	private Integer existencia;
	
	private String codigo;
	
	private Long precio;
	public Producto(){}
	public Producto(Long id, String nombre, String codigo, Integer existencia, Long precio) {
		this.id = id;
		this.nombre = nombre;
		this.codigo = codigo;
		this.existencia = existencia;
		this.precio = precio;
	}
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getExistencia() {
		return existencia;
	}

	public void setExistencia(Integer existencia) {
		this.existencia = existencia;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Long getPrecio() {
		return precio;
	}

	public void setPrecio(Long precio) {
		this.precio = precio;
	}

}
