package App.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import App.Auxiliar.Utilidades;

@Entity
public class Comentario {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String mensaje;
		
	private String fecha;
	
	@ManyToOne
	@JoinColumn(name="SERIE_ID")
	private Serie serie;
	
	@ManyToOne
	@JoinColumn(name="USUARIO_ID")
	private  Usuario usuario;
	
	public Comentario() {
		
	}
	
	public Comentario(String mensaje) {
		
		this.mensaje = mensaje;
		this.fecha = Utilidades.getDateToday();
		
		this.usuario = new Usuario();
		this.serie = new Serie();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public Serie getSerie() {
		return serie;
	}

	public void setSerie(Serie serie) {
		this.serie = serie;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	
	
	
}
