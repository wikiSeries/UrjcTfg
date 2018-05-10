package App.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Personaje {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private int idApi;
	private String nombre;
	private String urlImagen;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "ACTOR_ID")
	private Actor actor;
	
	public Personaje() {
		
	}
	
	public Personaje(int idApi, String nombre, String urlImagen) {
		this.idApi = idApi;
		this.nombre = nombre;
		this.urlImagen = urlImagen;
		
		this.actor = new Actor();
	}
	
	public Personaje(App.Model.TvMaze.Character character) {
		this.idApi = character.getId();
		this.nombre = character.getName();
		
		if(character.getImage() != null) {
			this.urlImagen = character.getImage().getMedium().contains("https") ? character.getImage().getMedium() : character.getImage().getMedium().replace("http", "https");
		}
		else {
			this.urlImagen = "/Images/imageNoAvailable.png";
		}
		
		this.actor = new Actor();
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUrlImagen() {
		return urlImagen;
	}

	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}
	
	public void Actualizar(App.Model.TvMaze.Character character) {
		this.setIdApi(character.getId());
		this.setNombre(character.getName());
		
		String imagen = new String();
		if(character.getImage() != null) {
			imagen = character.getImage().getMedium().contains("https") ? character.getImage().getMedium() : character.getImage().getMedium().replace("http", "https");
		}
		else {
			imagen = "/Images/imageNoAvailable.png";
		}
		
		this.setUrlImagen(imagen);
	}
}
