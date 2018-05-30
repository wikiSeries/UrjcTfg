package App.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import App.Auxiliar.Constantes;

@Entity
public class Personaje {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private int idApi;
	private String nombre;
	private String urlImagen;
	
	@ManyToOne
	@JoinColumn(name = "ACTOR_ID")
	private Actor actor;
	
	@ManyToOne
	@JoinColumn(name = "SERIE_ID")
	private Serie serie;
	
	public Personaje() {
		
	}
	
	public Personaje(int idApi, String nombre, String urlImagen) {
		this.idApi = idApi;
		this.nombre = nombre;
		this.urlImagen = urlImagen;
		
		this.actor = new Actor();
		this.serie = new Serie();
	}
	
	public Personaje(App.Model.TvMaze.Character character) {
		this.idApi = character.getId();
		this.nombre = character.getName();
		
		if(character.getImage() != null) {
			this.urlImagen = character.getImage().getMedium().contains(Constantes.URL_HTTPS) ? character.getImage().getMedium() : character.getImage().getMedium().replace("http", Constantes.URL_HTTPS);
		}
		else {
			this.urlImagen = "/Images/imageNoAvailable.png";
		}
		
		this.actor = new Actor();
		this.serie = new Serie();
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

	public Serie getSerie() {
		return serie;
	}

	public void setSerie(Serie serie) {
		this.serie = serie;
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
		
		String imagen;
		if(character.getImage() != null) {
			imagen = character.getImage().getMedium().contains(Constantes.URL_HTTPS) ? character.getImage().getMedium() : character.getImage().getMedium().replace("http", Constantes.URL_HTTPS);
		}
		else {
			imagen = "/Images/imageNoAvailable.png";
		}
		
		this.setUrlImagen(imagen);
	}
}
