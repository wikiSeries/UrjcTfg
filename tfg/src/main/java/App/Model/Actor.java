package App.Model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import App.Auxiliar.Constantes;
import App.Model.TvMaze.Person;

@Entity
public class Actor {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private int idApi;
	private String nombre;
	private String pais;
	private String fechaNacimiento;
	private String fechaFallecimiento;
	private String genero;
	private String urlImagen;
	
	
	@ManyToMany
	private List<Serie> series;
	
	@OneToMany(mappedBy = "actor")
	private List<Personaje> personajes;
	
	public Actor() {
		
	}
	
	public Actor(int idApi, String nombre, String pais, String fechaNacimiento, String fechaFallecimiento, String genero, String urlImagen) {
		this.idApi = idApi;
		this.nombre = nombre;
		this.pais = pais;
		this.fechaNacimiento = fechaNacimiento;
		this.fechaFallecimiento = fechaFallecimiento;
		this.genero = genero;
		this.urlImagen = urlImagen;
		this.series = new ArrayList<Serie>();
		this.personajes = new ArrayList<Personaje>();
		
	}
	
	public Actor(Person person) {
		this.idApi = person.getId();
		this.nombre = person.getName();
		this.pais = person.getCountry() != null ? person.getCountry().getName() : "";
		this.fechaNacimiento = person.getBirthday();
		this.fechaFallecimiento = person.getDeathday();
		this.genero = person.getGender();
		this.series = new ArrayList<Serie>();
		this.personajes = new ArrayList<Personaje>();
		
		if(person.getImage() != null) {
			this.urlImagen = person.getImage().getMedium().contains(Constantes.URL_HTTPS) ? person.getImage().getMedium() : person.getImage().getMedium().replace("http", Constantes.URL_HTTPS);
		}
		else {
			this.urlImagen = "/Images/imageNoAvailable.png";
		}

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

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}
	

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getFechaFallecimiento() {
		return fechaFallecimiento;
	}

	public void setFechaFallecimiento(String fechaFallecimiento) {
		this.fechaFallecimiento = fechaFallecimiento;
	}
	

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getUrlImagen() {
		return urlImagen;
	}

	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}

	public List<Serie> getSeries() {
		return series;
	}

	public void setSeries(List<Serie> series) {
		this.series = series;
	}

	public List<Personaje> getPersonajes() {
		return personajes;
	}

	public void setPersonajes(List<Personaje> personajes) {
		this.personajes = personajes;
	}
	
	public void Actualizar(Person p) {
		this.setIdApi(p.getId());
		this.setNombre(p.getName());
		String paisSet = p.getCountry() != null ? p.getCountry().getName() : "";
		this.setPais(paisSet);
		this.setFechaNacimiento(p.getBirthday());
		this.setFechaFallecimiento(p.getDeathday());
		this.setGenero(p.getGender());
		
		String imagen;
		if(p.getImage() != null) {
			imagen = p.getImage().getMedium().contains(Constantes.URL_HTTPS) ? p.getImage().getMedium() : p.getImage().getMedium().replace("http", Constantes.URL_HTTPS);
		}
		else {
			imagen = "/Images/imageNoAvailable.png";
		}

		this.setUrlImagen(imagen);
	}
	
}
