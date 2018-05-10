package App.Model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import App.Auxiliar.Utilidades;

@Entity
public class Usuario {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String usuario;
	private String contraseña;
	private String nombre;
	private String apellidos;
	private String correo;
	private String telefonoMovil;
	private int intentos;
	private boolean bloqueado;
	private String fechaCreacion;
	
	@ManyToMany(mappedBy = "usuarios")
	private List<Serie> series;
	
	@OneToMany(mappedBy="usuario")
	private List<Conexion> conexiones;
	
	@OneToMany(mappedBy="usuario")
	private List<Comentario> comentarios;
	
	@ElementCollection
	Map <Long, Boolean> likeSeries;
	
	@ManyToMany(mappedBy = "usuarios")
	private List<Episodio> episodiosPendientes;
	
	@ManyToMany(mappedBy = "usuariosV")
	private List<Episodio> episodiosVistos;
	
	@ManyToMany(mappedBy = "usuarios")
	private List<Rol> roles;
	
	public Usuario() {
		
	}

	public Usuario(String usuario, String contraseña, String nombre, String apellidos, String correo,
			String telefonoMovil) {
		this.usuario = usuario;
		this.contraseña = contraseña;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.correo = correo;
		this.telefonoMovil = telefonoMovil;
		this.intentos = 0;
		this.bloqueado = true;
		this.series = new ArrayList<Serie>();
		this.likeSeries = new HashMap <Long, Boolean>();
		this.episodiosPendientes = new ArrayList<Episodio>();
		this.episodiosVistos = new ArrayList<Episodio>();
		this.roles = new ArrayList<Rol>();
		this.fechaCreacion = Utilidades.getDateToday();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefonoMovil() {
		return telefonoMovil;
	}

	public void setTelefonoMovil(String telefonoMovil) {
		this.telefonoMovil = telefonoMovil;
	}

	public int getIntentos() {
		return intentos;
	}

	public void setIntentos(int intentos) {
		this.intentos = intentos;
	}

	public boolean isBloqueado() {
		return (this.intentos == 0);
	}

	public void setBloqueado(boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	public List<Serie> getSeries() {
		return series;
	}

	public void setSeries(List<Serie> series) {
		this.series = series;
	}

	public List<Conexion> getConexiones() {
		return conexiones;
	}

	public void setConexiones(List<Conexion> conexiones) {
		this.conexiones = conexiones;
	}

	public List<Comentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Map<Long, Boolean> getLikeSeries() {
		return likeSeries;
	}

	public void setLikeSeries(Map<Long, Boolean> likeSeries) {
		this.likeSeries = likeSeries;
	}

	public List<Episodio> getEpisodiosPendientes() {
		return episodiosPendientes;
	}

	public void setEpisodiosPendientes(List<Episodio> episodiosPendientes) {
		this.episodiosPendientes = episodiosPendientes;
	}

	public List<Episodio> getEpisodiosVistos() {
		return episodiosVistos;
	}

	public void setEpisodiosVistos(List<Episodio> episodiosVistos) {
		this.episodiosVistos = episodiosVistos;
	}

	public List<Rol> getRoles() {
		return roles;
	}

	public void setRoles(List<Rol> roles) {
		this.roles = roles;
	}
	
	
}
