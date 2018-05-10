package App.Controller;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import App.Auxiliar.Utilidades;
import App.Model.Actor;
import App.Model.Comentario;
import App.Model.Episodio;
import App.Model.Genero;
import App.Model.Personaje;
import App.Model.Serie;
import App.Model.Temporada;
import App.Model.Usuario;
import App.Model.Post.ComentarioPost;
import App.Model.TvMaze.Elenco;
import App.Model.TvMaze.SerieApi;
import App.Model.TvMaze.SerieApiFull;
import App.Repository.RepositorioActores;
import App.Repository.RepositorioComentarios;
import App.Repository.RepositorioEpisodios;
import App.Repository.RepositorioGeneros;
import App.Repository.RepositorioPersonajes;
import App.Repository.RepositorioSeries;
import App.Repository.RepositorioTemporadas;
import App.Repository.RepositorioUsuario;

@RestController
public class ApiWikiSeries {
	
	private Logger logger = Logger.getLogger("file");
	
	@Autowired
	private RepositorioSeries repositorioSeries;
	
	@Autowired
	private RepositorioGeneros repositorioGeneros;
	
	@Autowired
	private RepositorioActores repositorioActores;
	
	@Autowired
	private RepositorioPersonajes repositorioPersonajes;
	
	@Autowired
	private RepositorioTemporadas repositorioTemporadas;
	
	@Autowired
	private RepositorioEpisodios repositorioEpisodios;
	
	@Autowired
	private RepositorioUsuario repositorioUsuarios;
	
	@Autowired
	private RepositorioComentarios repositorioComentarios;
	
	@RequestMapping(value="/GuardarSerieApi", method = RequestMethod.POST )
	public String guardarSerieApi(@RequestBody SerieApi serieApi) {
		try {

			Serie serie = repositorioSeries.findByIdApi(serieApi.getId());
			
			if(serie == null) {				
				serie = new Serie(serieApi);				
			}
			else {
				serie.actualizar(serieApi);
			}
			//Revisar
			this.relacionarSerieGeneros(serie, serieApi);
			repositorioSeries.save(serie);
			
			List<Genero> listaGeneros = serie.getGeneros();
			for(Genero g : listaGeneros) {
				repositorioGeneros.save(g);
			}
						
		}
		catch(Exception ex) {
			logger.error(String.format("GuardarSerieApi\n%s", Utilidades.formatedExceptionMessage(ex)));
			return "error";
			
		}
		
		return Integer.toString(serieApi.getId());
	}
	
	private void relacionarSerieGeneros(Serie serie, SerieApi serieApi){
		List<Genero> listaGeneros = new ArrayList<Genero>();
		List<String> listaNombreGeneros = serieApi.getGenres();
		
		for(String tituloGenero : listaNombreGeneros) {
			if(tituloGenero != null && !tituloGenero.isEmpty()) {
				Genero genero = repositorioGeneros.findByTitulo(tituloGenero);
				if(genero == null) {
					 genero = new Genero(tituloGenero);
				}
				
				if(!genero.getSeries().contains(serie)) {
					genero.getSeries().add(serie);
				}
				
				listaGeneros.add(genero);
			}	
		}
		
		serie.setGeneros(listaGeneros);
	}
	
		
	@RequestMapping(value="/GuardarIdVideo", method = RequestMethod.PUT)
	public String guardarIdVideo(@RequestBody String json) {
		try {
			String [] jsonSplit = json.split("&");
			String idVideo = jsonSplit[0].split("=")[1];
			String idSerie = jsonSplit[1].split("=")[1];
			
			Serie serie = repositorioSeries.findOne(Long.parseLong(idSerie));
			if(serie != null) {
				serie.setIdVideo(idVideo);
				repositorioSeries.save(serie);
				
				return "Se ha guardado el trailer de la serie '" + serie.getTitulo() + "'.";
			}
			
			
			return "Error: La serie no existe";
			
		}
		catch(Exception ex) {
			logger.error(String.format("GuardarIdVideo\n%s", Utilidades.formatedExceptionMessage(ex)));
			return "Error al guardar el trailer: " + ex.getMessage();
		}
		
	}
	
	@RequestMapping(value = "/DamePuntuacionMediaSerie", method = RequestMethod.GET)
	public Integer damePuntuacionMediaSerie(@RequestParam("puntuacion") int puntuacion, 
										@RequestParam("idSerie") Long idSerie,
										@RequestParam("nombreUsuario") String nombreUsuario) {
		try {
			
			Usuario usuario = repositorioUsuarios.findByUsuario(nombreUsuario);
			Serie serie = repositorioSeries.findById(idSerie);
			int puntuacionUsuario = puntuacion;
			
			Integer nuevaMedia = puntuacionUsuario;
			if(!serie.getPuntuaciones().isEmpty()) {
				 nuevaMedia = (nuevaMedia + serie.getPuntuacionMediaEstrella()) / 2;
			}
			
			serie.getPuntuaciones().put(usuario.getId(), puntuacionUsuario);
			serie.setPuntuacionMediaEstrella(nuevaMedia);
			
			repositorioSeries.save(serie);
			
			return nuevaMedia;	

		}
		catch(Exception ex) {
			logger.error(String.format("DamePuntuacionMediaSerie\n%s", Utilidades.formatedExceptionMessage(ex)));
			return -1;
		}
		
				
	}
	
	@RequestMapping(value = "/GuardarDatosSerie", method = RequestMethod.POST)
	public void guardarDatosSerie(@RequestBody SerieApiFull serieApiFull) {
		int idSerieApi = serieApiFull.getIdSerie();
		
		Serie serie = repositorioSeries.findByIdApi(idSerieApi);
		guardarPersonajes(serie, serieApiFull.getElenco());
		guardarTemporadas(serie, serieApiFull.getTemporadas());
		guardarEpisodios(serie, serieApiFull.getEpisodios());
	}
	
	private void guardarPersonajes(Serie serie, List<Elenco> listaElenco) {
		for(Elenco e : listaElenco) {
			try{
				Actor actor = repositorioActores.findByIdApi(e.getPerson().getId());
				if(actor == null) {
					actor = new Actor(e.getPerson());
					serie.getActores().add(actor);
					actor.getSeries().add(serie);
				}
				else {
					actor.Actualizar(e.getPerson());
				}
				
				Personaje personaje = repositorioPersonajes.findByIdApi(e.getCharacter().getId());
				if(personaje == null) {
					personaje = new Personaje(e.getCharacter());
					personaje.setActor(actor);
					actor.getPersonajes().add(personaje);
				}
				else {
					personaje.Actualizar(e.getCharacter());
				}
				
				repositorioSeries.save(serie);
				repositorioActores.save(actor);
				repositorioPersonajes.save(personaje);
			}
			catch(Exception ex) {
				logger.error(String.format("GuardarPersonajes\n%s", Utilidades.formatedExceptionMessage(ex)));
			}
			
		}
	}
	
	private void guardarTemporadas(Serie serie, List<App.Model.TvMaze.TemporadaApi> temporadas) {
		for(App.Model.TvMaze.TemporadaApi t : temporadas) {
			try {
				Temporada temporada = repositorioTemporadas.findByIdApi(t.getId());
				if(temporada == null) {
					temporada = new Temporada(t);
					temporada.setSerie(serie);
					serie.getTemporadas().add(temporada);
				}
				else {
					temporada.Actualizar(t);
				}
				
				repositorioSeries.save(serie);
				repositorioTemporadas.save(temporada);
			}
			catch(Exception ex) {
				logger.error(String.format("GuardarTemporadas\n%s", Utilidades.formatedExceptionMessage(ex)));
			}
			
		}
	}
	
	private void guardarEpisodios(Serie serie, List<App.Model.TvMaze.EpisodioApi> episodios) {
		for(App.Model.TvMaze.EpisodioApi e : episodios) {
			try {
				Temporada temporada = repositorioTemporadas.findBySerieAndNumero(serie, e.getSeason());
				if(temporada != null) {
					Episodio episodio = repositorioEpisodios.findByIdApi(e.getId());
					if(episodio == null) {
						episodio = new Episodio(e);
						episodio.setTemporada(temporada);
					}
					else {
						episodio.actualizar(e);
					}
					
					repositorioTemporadas.save(temporada);
					repositorioEpisodios.save(episodio);
				}
			}
			catch(Exception ex) {
				logger.error(String.format("GuardarEpisodios\n%s", Utilidades.formatedExceptionMessage(ex)));
			}
		}
	}
	
	@RequestMapping(value = "/EnviarComentario", method = RequestMethod.POST)
	public String enviarComentario(@RequestBody ComentarioPost comentarioPost) {
		try {
			
			Comentario comentario = new Comentario(comentarioPost.getComentario());
			comentario.setUsuario(null);
			comentario.setSerie(null);
			
			repositorioComentarios.save(comentario);
			
			Usuario usuario = repositorioUsuarios.findByUsuario(comentarioPost.getNombreUsuario());
			Serie serie = repositorioSeries.findById(comentarioPost.getIdSerie());
			
			
			Email email = new Email();
			
			StringBuilder cuerpo = email.crearCuerpoComentario(usuario.getUsuario(), serie.getTitulo(), comentario.getMensaje(),
														serie.getId(), usuario.getId(), comentario.getId());
			
			Usuario admin = repositorioUsuarios.findByUsuario("admin");
			if(email.enviarCorreo(admin.getCorreo(), "Validar comentario", cuerpo.toString())) {
				return "ok";
			}
			
			
		}
		
		catch(Exception ex) {
			logger.error(String.format("EnviarComentario\n%s", Utilidades.formatedExceptionMessage(ex)));
		}
		
		return "error";
	}
	
	@RequestMapping(value = "/ActualizarContador/{idSerie}/{nombreUsuario}/{operacion}", method = RequestMethod.GET)
	public int actualizarLikesSerie(@PathVariable Long idSerie, @PathVariable String nombreUsuario, @PathVariable String operacion) {
		int newContador = 0;

		try {
			Serie serie = repositorioSeries.findById(idSerie);
			Usuario usuario = repositorioUsuarios.findByUsuario(nombreUsuario);
			
			if(operacion.equalsIgnoreCase("Sumar")) {
				newContador = serie.getLikes() + 1;
				serie.setLikes(newContador);
				
				usuario.getLikeSeries().put(serie.getId(), true);
			}
			else if(serie.getLikes() > 0) {
				newContador = serie.getLikes() - 1;
				serie.setLikes(newContador);
				
				usuario.getLikeSeries().put(serie.getId(), false);
			}
			
			repositorioSeries.save(serie);
			repositorioUsuarios.save(usuario);
			
			return newContador;
		}
		catch(Exception ex) {
			logger.error(String.format("ActualizarContador\n%s", Utilidades.formatedExceptionMessage(ex)));
			return -1;
		}
	}
	
	
	@RequestMapping(value = "/AñadirSerie/{idSerie}/{nombre}", method = RequestMethod.POST)
	public String añadirSerie(@PathVariable("idSerie") Long idSerie, @PathVariable("nombre") String nombreUsuario) {
		
		try {
			Usuario usuario = repositorioUsuarios.findByUsuario(nombreUsuario);
			Serie serie = repositorioSeries.findById(idSerie);
			
			if(!usuario.getSeries().contains(serie)) {
				usuario.getSeries().add(serie);
				serie.getUsuarios().add(usuario);
				
				List<Episodio> episodios =  new ArrayList<Episodio>();
				for(Temporada t : serie.getTemporadas()) {
					episodios.addAll(t.getEpisodios());
				}
				
				usuario.setEpisodiosPendientes(episodios);
				
				for(Episodio e : episodios) {
					e.getUsuarios().add(usuario);
					repositorioEpisodios.save(e);
				}
				
				repositorioUsuarios.save(usuario);
				repositorioSeries.save(serie);
				
				return "Serie añadida correctamente";
			}
			
			return "La serie ya pertenece a tu lista de series";
			
		}
		catch(Exception ex) {
			logger.error(String.format("AñadirSerie\n%s", Utilidades.formatedExceptionMessage(ex)));
			return String.format("Error al añadir la serie");
		}
				
	}
	
	@RequestMapping(value = "/Episodio/{accion}/{idEpisodio}/{nombre}", method = RequestMethod.POST)
	public void marcarEpisodio(@PathVariable("accion") String accion, @PathVariable("idEpisodio") Long idEpisodio, @PathVariable("nombre") String nombreUsuario) {
		
		try {
			
			Usuario usuario = repositorioUsuarios.findByUsuario(nombreUsuario);
			Episodio episodio = repositorioEpisodios.findById(idEpisodio);
			
			List<Episodio> pendientes = usuario.getEpisodiosPendientes();
			List<Episodio> vistos = usuario.getEpisodiosVistos();

			List<Usuario> usuarios = episodio.getUsuarios();
			List<Usuario> usuariosV = episodio.getUsuariosV();
			
			//Si accion == "visto" -> Pasa de pendiente a visto
			if(accion.equalsIgnoreCase("Visto")) {
				pendientes.remove(episodio);
				vistos.add(episodio);

				usuarios.remove(usuario);
				usuariosV.add(usuario);
			}
			
			//Pasa de visto a pendiente
			else {
				vistos.remove(episodio);
				pendientes.add(episodio);
				
				usuariosV.remove(usuario);
				usuarios.add(usuario);
			}
			
			usuario.setEpisodiosPendientes(pendientes);
			usuario.setEpisodiosVistos(vistos);
			episodio.setUsuarios(usuarios);
			episodio.setUsuariosV(usuariosV);
			
			repositorioEpisodios.save(episodio);
			repositorioUsuarios.save(usuario);
	
		}
		catch(Exception ex) {
			logger.error(String.format("Episodio - accion: pasar a la lista '%s'\n%s", accion, Utilidades.formatedExceptionMessage(ex)));
		}
			
	}
	
	@RequestMapping(value = "/EliminarMiSerie/{idSerie}/{nombre}", method = RequestMethod.POST)
	public void eliminarMiSerie(@PathVariable("idSerie") Long idSerie, @PathVariable("nombre") String nombreUsuario) {
		try {
			Usuario usuario = repositorioUsuarios.findByUsuario(nombreUsuario);
			Serie serie = repositorioSeries.findById(idSerie);
			
			usuario.setEpisodiosPendientes(new ArrayList<Episodio>());
			usuario.setEpisodiosVistos(new ArrayList<Episodio>());
			
			for(Temporada t : serie.getTemporadas()) {
				for(Episodio e : t.getEpisodios()) {
					try {
						e.getUsuarios().remove(usuario);
						e.getUsuariosV().remove(usuario);
						
						repositorioEpisodios.save(e);
					}
					catch(Exception ex) {
						throw ex;
					}
					
				}
			}
			
			usuario.getSeries().remove(serie);
			serie.getUsuarios().remove(usuario);
			
			repositorioUsuarios.save(usuario);
			repositorioSeries.save(serie);
		}
		catch(Exception ex) {
			logger.error(String.format("EliminarMiSerie\n%s", Utilidades.formatedExceptionMessage(ex)));
		}
		
	}
	
}
