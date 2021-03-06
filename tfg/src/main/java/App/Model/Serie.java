package App.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import App.Auxiliar.Constantes;
import App.Model.TvMaze.SerieApi;

@Entity
public class Serie {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
		
	private String titulo;
	
	@Column(columnDefinition = "TEXT")
	private String descripcion;
	private String urlImage;
	private String idVideo;
	
	private int idApi;
	private String fechaEstreno;
	private Double puntuacion;
	private String canalWeb;
	private String canalTv;
	private String sitioWeb;
	private String idioma;
	private String estado;
	
	@ManyToMany(mappedBy="series")
	private List<Genero> generos;
	
	
	@OneToMany(mappedBy="serie")
	private List<Comentario> comentarios;
	
	
	@ManyToMany(mappedBy = "series")
	private List<Actor> actores;
	
	
	@OneToMany(mappedBy  = "serie")
	private List<Temporada> temporadas;
	
	@OneToMany(mappedBy = "serie")
	private List<Personaje> personajes;
	
	@ManyToMany
	private List<Usuario> usuarios;
	
	private int likes;
	
	@ElementCollection
	private Map<Long, Integer> puntuaciones;
	
	private int puntuacionMediaEstrella;
	
	public Serie() {
		
	}
	
	public Serie(int idApi, String titulo, String descripcion, String fechaEstreno, Double puntuacion, String urlImage) {
		
		this.idApi = idApi;
		this.titulo = titulo;
		
		this.descripcion = descripcion.length() > Constantes.NUMERO_LIMITE_COLUMNA_TEXTO ? descripcion.substring(0, Constantes.NUMERO_LIMITE_COLUMNA_TEXTO - Constantes.FINAL_PARRAFO.length()) + Constantes.FINAL_PARRAFO : descripcion;
		this.fechaEstreno = fechaEstreno;
		this.puntuacion = puntuacion;
		this.urlImage = urlImage;
		this.generos = new ArrayList<Genero>();
		this.actores = new ArrayList<Actor>();
		this.personajes = new ArrayList<Personaje>();
		this.puntuaciones = new HashMap<Long, Integer>();
		this.usuarios = new ArrayList<Usuario>();
		
		
	}
	
	public Serie(SerieApi serieApi) {
		this.idApi = serieApi.getId();
		this.titulo = serieApi.getName();
		this.descripcion = serieApi.getSummary().length() > Constantes.NUMERO_LIMITE_COLUMNA_TEXTO ? serieApi.getSummary().substring(0, Constantes.NUMERO_LIMITE_COLUMNA_TEXTO - Constantes.FINAL_PARRAFO.length()) + Constantes.FINAL_PARRAFO : serieApi.getSummary();
		this.fechaEstreno = serieApi.getPremiered();
		this.puntuacion = serieApi.getRating() != null ? serieApi.getRating().getAverage() : 0; 
		
		if(serieApi.getImage() != null) {
			this.urlImage = serieApi.getImage().getMedium().contains(Constantes.URL_HTTPS) ? serieApi.getImage().getMedium() : serieApi.getImage().getMedium().replace("http", Constantes.URL_HTTPS);
		}
		else {
			this.urlImage = "/Images/imageNoAvailable.png";
		}
		
		this.canalWeb = serieApi.getWebChannel() != null ? serieApi.getWebChannel().getName() : null;
		this.canalTv = serieApi.getNetwork() != null ? serieApi.getNetwork().getName() : null;
		this.sitioWeb = serieApi.getOfficialSite();
		this.idioma = serieApi.getLanguage();
		this.estado = serieApi.getStatus();
		
		this.generos = new ArrayList<Genero>();
		this.actores = new ArrayList<Actor>();
		this.personajes = new ArrayList<Personaje>();
		this.puntuaciones = new HashMap<Long, Integer>();
		this.usuarios = new ArrayList<Usuario>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Genero> getGeneros() {
		return generos;
	}

	public void setGeneros(List<Genero> generos) {
		this.generos = generos;
	}

	public List<Comentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	public String getIdlVideo() {
		return idVideo;
	}

	public void setIdVideo(String idVideo) {
		this.idVideo = idVideo;
	}

	public int getIdApi() {
		return idApi;
	}

	public void setIdApi(int idApi) {
		this.idApi = idApi;
	}

	public String getFechaEstreno() {
		return fechaEstreno;
	}

	public void setFechaEstreno(String fechaEstreno) {
		this.fechaEstreno = fechaEstreno;
	}

	public Double getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(Double puntuacion) {
		this.puntuacion = puntuacion;
	}
	

	public String getCanalWeb() {
		return canalWeb;
	}

	public void setCanalWeb(String canalWeb) {
		this.canalWeb = canalWeb;
	}

	public String getCanalTv() {
		return canalTv;
	}

	public void setCanalTv(String canalTv) {
		this.canalTv = canalTv;
	}

	public String getSitioWeb() {
		return sitioWeb;
	}

	public void setSitioWeb(String sitioWeb) {
		this.sitioWeb = sitioWeb;
	}

	public String getIdioma() {
		return idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<Actor> getActores() {
		return actores;
	}

	public void setActores(List<Actor> actores) {
		this.actores = actores;
	}

	public List<Temporada> getTemporadas() {
		return temporadas;
	}

	public void setTemporadas(List<Temporada> temporadas) {
		this.temporadas = temporadas;
	}

	public Map<Long, Integer> getPuntuaciones() {
		return puntuaciones;
	}

	public void setPuntuaciones(Map<Long, Integer> puntuaciones) {
		this.puntuaciones = puntuaciones;
	}

	public int getPuntuacionMediaEstrella() {
		return puntuacionMediaEstrella;
	}

	public void setPuntuacionMediaEstrella(int puntuacionMediaEstrella) {
		this.puntuacionMediaEstrella = puntuacionMediaEstrella;
	}
	
	public List<Personaje> getPersonajes() {
		return personajes;
	}

	public void setPersonajes(List<Personaje> personajes) {
		this.personajes = personajes;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public void actualizar(SerieApi serieApi) {
		this.setIdApi(serieApi.getId());
		this.setTitulo(serieApi.getName());
		
		String descripcionSet = serieApi.getSummary().length() > Constantes.NUMERO_LIMITE_COLUMNA_TEXTO ? serieApi.getSummary().substring(0, Constantes.NUMERO_LIMITE_COLUMNA_TEXTO - Constantes.FINAL_PARRAFO.length()) + Constantes.FINAL_PARRAFO : serieApi.getSummary();
		this.setDescripcion(descripcionSet);
		this.setFechaEstreno(serieApi.getPremiered());
		
		double puntuacionSet = serieApi.getRating() != null ? serieApi.getRating().getAverage() : 0;
		this.setPuntuacion(puntuacionSet);
		
		String imagen;
		if(serieApi.getImage() != null) {
			imagen = serieApi.getImage().getMedium().contains(Constantes.URL_HTTPS) ? serieApi.getImage().getMedium() : serieApi.getImage().getMedium().replace("http", Constantes.URL_HTTPS);
		}
		else {
			imagen = "/Images/imageNoAvailable.png";
		}
		
		this.setUrlImage(imagen);
		
		String canalWebSet = serieApi.getWebChannel() != null ? serieApi.getWebChannel().getName() : null;
		this.setCanalWeb(canalWebSet);
		
		String canalTvSet = serieApi.getNetwork() != null ? serieApi.getNetwork().getName() : null;
		this.setCanalTv(canalTvSet);
		
		this.setSitioWeb(serieApi.getOfficialSite());
		this.setIdioma(serieApi.getLanguage());
		this.setEstado(serieApi.getStatus());
	}
	
	public String getGenerosToString() {
		StringBuilder generosString = new StringBuilder();
		for(Genero g : this.generos) {
			if(g.equals(this.generos.get(this.generos.size() -1 ))) {
				generosString.append(g.getTitulo()).append(".");
			}
			else {
				generosString.append(g.getTitulo()).append(", ");
			}
		}
		return generosString.toString();
	}
	
	
}
