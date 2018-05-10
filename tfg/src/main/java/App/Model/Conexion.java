package App.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import App.Auxiliar.Utilidades;

@Entity
public class Conexion {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String ip;
	private String fecha;
	private String ciudad;
	private String Pais;
	private String Provincia;
	private String codigoPostal;
	
	@ManyToOne
	@JoinColumn(name="USUARIO_ID")
	private Usuario usuario;
	
	public Conexion() {
		
	}
	
	public Conexion(String ciudad, String pais, String ip, String provincia, String codigoPostal) {
		
		this.ciudad = ciudad;
		this.Pais = pais;
		this.ip = ip;
		this.Provincia = provincia;
		this.codigoPostal = codigoPostal;
		this.fecha = Utilidades.getDateToday();
		this.usuario = new Usuario();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getPais() {
		return Pais;
	}

	public void setPais(String pais) {
		Pais = pais;
	}

	public String getProvincia() {
		return Provincia;
	}

	public void setProvincia(String provincia) {
		Provincia = provincia;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
}
