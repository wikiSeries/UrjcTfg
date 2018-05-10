package App.Model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Episodio {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private int idApi;
	private String titulo;
	private int numero;
	private String estreno;
	private int duracion;
	
	@Column(columnDefinition = "VARCHAR(1024)")
	private String descripcion;
	
	@ManyToOne
	@JoinColumn(name = "TEMPORADA_ID")
	private Temporada temporada;
	
	@JsonIgnore
	@ManyToMany
	private List<Usuario> usuarios;
	
	@JsonIgnore
	@ManyToMany
	private List<Usuario> usuariosV;
	
	public Episodio() {

	}
	
	
	public Episodio(int idApi, String titulo, int numero, String estreno, int duracion, String descripcion) {
		this.idApi = idApi;
		this.titulo = titulo;
		this.numero = numero;
		this.estreno = estreno;
		this.duracion = duracion;
		this.descripcion = descripcion;
		
		this.temporada = new Temporada();
		this.usuarios = new ArrayList<Usuario>();
		this.usuariosV = new ArrayList<Usuario>();
	}
	
	public Episodio(App.Model.TvMaze.EpisodioApi episodioApi) {
		this.idApi = episodioApi.getId();
		this.titulo = episodioApi.getName();
		this.numero = episodioApi.getNumber();
		this.estreno = episodioApi.getAirdate();
		this.duracion = episodioApi.getRuntime();
		this.descripcion = episodioApi.getSummary();
		
		this.temporada = new Temporada();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getIdApi() {
		return idApi;
	}

	public void setIdApi(int idApi) {
		this.idApi = idApi;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getEstreno() {
		return estreno;
	}

	public void setEstreno(String estreno) {
		this.estreno = estreno;
	}

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}
	
	
	
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
	

	public List<Usuario> getUsuariosV() {
		return usuariosV;
	}

	public void setUsuariosV(List<Usuario> usuariosV) {
		this.usuariosV = usuariosV;
	}

	public void actualizar(App.Model.TvMaze.EpisodioApi episodioApi) {
		this.setIdApi(episodioApi.getId());
		this.setTitulo(episodioApi.getName());
		this.setNumero(episodioApi.getNumber());
		this.setEstreno(episodioApi.getAirdate());
		this.setDuracion(episodioApi.getRuntime());
		this.setDescripcion(episodioApi.getSummary());
	}
	
	
	
}
