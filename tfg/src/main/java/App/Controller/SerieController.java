package App.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import App.Auxiliar.Constantes;
import App.Auxiliar.Utilidades;
import App.Auxiliar.UtilidadesLista;
import App.Model.Genero;
import App.Model.Personaje;
import App.Model.Serie;
import App.Model.Usuario;
import App.Repository.RepositorioGeneros;
import App.Repository.RepositorioRoles;
import App.Repository.RepositorioSeries;
import App.Repository.RepositorioUsuario;

@Controller
public class SerieController {
	private Logger logger = Logger.getLogger("file");
	
	@Autowired
	private RepositorioSeries repositorioSeries;
	
	@Autowired
	private RepositorioGeneros repositorioGeneros;
	
	@Autowired
	private RepositorioUsuario repositorioUsuarios;
	
	@Autowired
	private RepositorioRoles repositorioRoles;
	
	@Autowired
	private EntityManager entityManager;
	
	@RequestMapping(value="/InfoSerie/{id}", method = RequestMethod.GET)
	public String getInfoSerie(@PathVariable Long id, HttpServletRequest httpRequest, HttpServletResponse httpResponse,  Model model) {
		
		try {
			Utilidades.noCachearRespuestaHTTP(httpResponse);
			HttpSession session = httpRequest.getSession(false);
			
			if(session != null) {
				String nombre = (String) session.getAttribute("user");
				Serie serie = repositorioSeries.findOne(id);
				Usuario usuario = repositorioUsuarios.findByUsuario(nombre);
				crearModeloInfoSerie(serie, usuario, model);				
				Cookie cookie = Utilidades.getCookie(httpRequest, nombre);
				if(cookie != null) {
					Utilidades.actualizarCookie(cookie, serie.getId().toString());
				}
				else {
					cookie = Utilidades.crearCookie(nombre, serie.getId().toString());
				}
				 
				httpResponse.addCookie(cookie);			
				return "FichaSerie";
				
			}		
			return Constantes.REDIRECT_LOGIN;
		}
		catch(Exception ex) {
			logger.error(String.format("InfoSerie%n%s", Utilidades.formatedExceptionMessage(ex)));	
			model.addAttribute(Constantes.MODEL_ATT_ERROR, "Informacion de la serie");
			model.addAttribute(Constantes.MODEL_ATT_DESCRIPCION, "Se ha producido un error al obtener la informacion de la serie."
					+ Constantes.CONTACT_WITH_ADMIN);
			
			return Constantes.TEMPLATE_PAGINA_ERROR;		
		}
		
	}
	
	@RequestMapping(value = "/BuscarTitulo", method = RequestMethod.GET)
	public String buscarTitulo(@RequestParam("titulo") String titulo,HttpServletRequest httpRequest, Model model) {
		try {
			HttpSession session = httpRequest.getSession(false);
			if(session != null) {
				String nombre = (String) session.getAttribute("user");
				PageRequest pageRequest = new PageRequest(0, Constantes.NUMERO_SERIES_PAGINA, Sort.Direction.ASC, "titulo");
				
				Page<Serie> pages = repositorioSeries.findByTituloContaining(titulo, pageRequest);
				String urlPaginacion = String.format("/BuscarTitulo/%s", titulo);
				
				int paginaActual = pages.getNumber() + 1;
				int paginaAnterior = paginaActual - 1;
				int paginaSiguiente = paginaActual + 1;
				
				int [] indices = new int[pages.getTotalPages()];
				for(int i = 0; i < indices.length; i++) {
					indices[i] = i + 1;
				}
				
				boolean esAdministrador = repositorioUsuarios.findByUsuario(nombre).getRoles().contains(repositorioRoles.findByTipo(Constantes.TIPO_ADMINISTRADOR));
				model.addAttribute("esAdministrador", esAdministrador);
				model.addAttribute("paginaActual", paginaActual);
				model.addAttribute(Constantes.MODEL_ATT_SERIES, pages);
				model.addAttribute("indices", indices);
				model.addAttribute("paginaAnterior", paginaAnterior);
				model.addAttribute("paginaSiguiente", paginaSiguiente);
				model.addAttribute("filtro", titulo);
				model.addAttribute("tipoFiltro", "buscar");
				model.addAttribute(Constantes.MODEL_ATT_NOMBRE_USUARIO, nombre);
				model.addAttribute("urlPaginacion", urlPaginacion);
				
				return "FiltroGenero";
			}
			
			return Constantes.REDIRECT_LOGIN;
		}
		catch(Exception ex) {
			logger.error(String.format("BuscarTtitulo%n%s", Utilidades.formatedExceptionMessage(ex)));
			model.addAttribute(Constantes.MODEL_ATT_ERROR, "Buscar serie por titulo");
			model.addAttribute(Constantes.MODEL_ATT_DESCRIPCION, "Se ha producido un error al realizar la busqueda por titulo."
					+ Constantes.CONTACT_WITH_ADMIN);
			
			return Constantes.TEMPLATE_PAGINA_ERROR;	
		}
		
	}
	
	@RequestMapping(value = "/BusquedaAvanzada/Resultados", method = RequestMethod.GET)
	public String busquedaAvanzada(@RequestParam("titulo") String tituloSerie, 
									@RequestParam("ano") String ano, @RequestParam("nombreActor") String nombreActor,
									@RequestParam("tituloGenero") String tituloGenero, 
									@RequestParam("nombrePersonaje") String nombrePersonaje, 
									@RequestParam("estrella") int estrella, HttpServletRequest httpRequest,
									HttpServletResponse httpResponse, Model model) {
		try {
			Utilidades.noCachearRespuestaHTTP(httpResponse);		
			HttpSession session = httpRequest.getSession(false);
			if(session != null) {
				String nombre = (String) session.getAttribute("user");
				int [] estrellas = {1, 2, 3, 4, 5};
				List<String> anos = repositorioSeries.getAnos();
				List<Genero> generos = repositorioGeneros.findAll();
				
				String q = crearQueryAvanzada(tituloSerie, ano, nombreActor, nombrePersonaje, tituloGenero, estrella);
				Query  query = entityManager.createNativeQuery(q, Serie.class);
				int contParameters = setValueParametersQuery(query, tituloSerie, ano, nombreActor, nombrePersonaje, tituloGenero, estrella);
				
				List<Serie> series = new ArrayList<Serie>();
				if(contParameters == 0) {
					model.addAttribute("mensaje", "Introduce algun parametro de busqueda para obtener resultados.");
				}
				else {
					series = query.getResultList();
				}
				
				boolean esAdministrador = repositorioUsuarios.findByUsuario(nombre).getRoles().contains(repositorioRoles.findByTipo(Constantes.TIPO_ADMINISTRADOR));
				model.addAttribute("esAdministrador", esAdministrador);
				model.addAttribute(Constantes.MODEL_ATT_NOMBRE_USUARIO, nombre);
				model.addAttribute("titulo", tituloSerie);
				model.addAttribute("nombreActor", nombreActor);
				model.addAttribute("nombrePersonaje", nombrePersonaje);
				model.addAttribute("estrellas", estrellas);
				model.addAttribute("estrella", estrella);
				model.addAttribute("anos", anos);
				model.addAttribute("ano", ano);
				model.addAttribute("generos", generos);
				model.addAttribute("genero", tituloGenero);
				model.addAttribute(Constantes.MODEL_ATT_SERIES, series);
				
				return "BusquedaAvanzada";
			}
			
			return Constantes.REDIRECT_LOGIN;	
		}
		catch(Exception ex) {
			logger.error(String.format("BuscarTtitulo%n%s", Utilidades.formatedExceptionMessage(ex)));	
			model.addAttribute(Constantes.MODEL_ATT_ERROR, "Buscar serie por titulo");
			model.addAttribute(Constantes.MODEL_ATT_DESCRIPCION, "Se ha producido un error al realizar la busqueda por titulo."
					+ Constantes.CONTACT_WITH_ADMIN);
			
			return Constantes.TEMPLATE_PAGINA_ERROR;	
		}
		
		
	}
	private int setValueParametersQuery(Query query, String tituloSerie, String ano, String nombreActor, String nombrePersonaje, String tituloGenero, int estrella) {
		int cont = 0;
		if(tituloSerie != null && !tituloSerie.isEmpty()) {
			query.setParameter("tituloSerie", tituloSerie);
			cont++;
		}
		if(ano != null && !ano.isEmpty()) {
			query.setParameter("ano", ano);
			cont++;
		}
		if(nombreActor != null && !nombreActor.isEmpty()) {
			query.setParameter("nombreActor", nombreActor);
			cont++;
		}
		
		if(nombrePersonaje != null && !nombrePersonaje.isEmpty()) {
			query.setParameter("nombrePersonaje", nombrePersonaje);
			cont++;
		}
		if(tituloGenero != null && !tituloGenero.isEmpty()) {
			query.setParameter("tituloGenero", tituloGenero);
			cont++;
		}
		if(estrella > -1) {
			query.setParameter("puntuacion", estrella);
			cont++;
		}
		return cont;
	}
	
	private String crearQueryAvanzada(String tituloSerie, String ano, String nombreActor, String nombrePersonaje, String tituloGenero, int estrella) {
		StringBuilder query = new StringBuilder();
		query.append(Constantes.ADVANCE_SEARCH_QUERY);
		Map <String, Boolean> control = new HashMap<String, Boolean>();
		control.put(Constantes.WHERE_SQL, true);
		control.put(Constantes.AND_SQL, false);
		
		construirQueryAvanzadaAux(query, control, tituloSerie, Constantes.ADVANCE_SEARCH_QUERY_TITULO);
		construirQueryAvanzadaAux(query, control, ano, Constantes.ADVANCE_SEARCH_QUERY_ANO);
		construirQueryAvanzadaAux(query, control, nombreActor, Constantes.ADVANCE_SEARCH_QUERY_INTERPRETE);
		construirQueryAvanzadaAux(query, control, nombrePersonaje, Constantes.ADVANCE_SEARCH_QUERY_PERSONAJE);
		construirQueryAvanzadaAux(query, control, tituloGenero, Constantes.ADVANCE_SEARCH_QUERY_GENERO);
		construirQueryAvanzadaAux(query, control, estrella, Constantes.ADVANCE_SEARCH_QUERY_PUNTUACION);

		return query.toString();
	}
	
	private int [] construirQueryAvanzadaAux(StringBuilder query, Map<String, Boolean> control, String parametro, String extensionQuery) {
		int [] cont = new int[2];
		
		if(parametro != null && !parametro.isEmpty()) {
			if(control.get(Constantes.WHERE_SQL)) {
				query.append(" " + Constantes.WHERE_SQL);
				control.put(Constantes.WHERE_SQL, false);
			}
			if(control.get(Constantes.AND_SQL)) {
				query.append(" " + Constantes.AND_SQL);
			}
			
			query.append(" ");
			query.append(extensionQuery);
			control.put(Constantes.AND_SQL, true);
		}
		return cont;
	}
	private void construirQueryAvanzadaAux(StringBuilder query, Map<String, Boolean> control, int parametro, String extensionQuery) {
		if(parametro > -1) {
			if(control.get(Constantes.WHERE_SQL)) {
				query.append(" " + Constantes.WHERE_SQL);
				control.put(Constantes.WHERE_SQL, false);
			}
			if(control.get(Constantes.AND_SQL)) {
				query.append(" " + Constantes.AND_SQL);
			}
			
			query.append(" ");
			query.append(extensionQuery);
			control.put(Constantes.AND_SQL, true);
		}
	}
	
	@RequestMapping(value = "/MisSeries", method = RequestMethod.GET)
	public String misSeries(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model) {
		
		try {
			Utilidades.noCachearRespuestaHTTP(httpResponse);
			HttpSession session = httpRequest.getSession(false);
			if(session != null) {
				String nombre = (String) session.getAttribute("user");
				Usuario usuario = repositorioUsuarios.findByUsuario(nombre);
				
				model.addAttribute(Constantes.MODEL_ATT_NOMBRE_USUARIO, nombre);
				model.addAttribute(Constantes.MODEL_ATT_SERIES, usuario.getSeries());
				model.addAttribute("pendientes", usuario.getEpisodiosPendientes());
				model.addAttribute("vistos", usuario.getEpisodiosVistos());
				
				return "ListaSeries";
			}
			
			return Constantes.REDIRECT_LOGIN;
		}
		catch(Exception ex) {
			logger.error(String.format("MisSeries%n%s", Utilidades.formatedExceptionMessage(ex)));	
			model.addAttribute(Constantes.MODEL_ATT_ERROR, "Mis series");
			model.addAttribute(Constantes.MODEL_ATT_DESCRIPCION, "Se ha producido un error al cargar sus listas de series."
					+ Constantes.CONTACT_WITH_ADMIN);
			
			return Constantes.TEMPLATE_PAGINA_ERROR;
		}
		
		
		
	}
	
	private void crearModeloInfoSerie(Serie serie, Usuario usuario, Model model) {
		model.addAttribute("urlImagenSerie", serie.getUrlImage());
		model.addAttribute("tituloSerie", serie.getTitulo());
		model.addAttribute("descripcionSerie", serie.getDescripcion());
		model.addAttribute("puntuacion", serie.getPuntuacion());
		model.addAttribute("estreno", serie.getFechaEstreno());
		String idVideo = serie.getIdlVideo() != null && serie.getIdlVideo() != "" ? "https://www.youtube.com/embed/" + serie.getIdlVideo() : null;
		model.addAttribute("idVideo", idVideo);
		model.addAttribute("idSerie", serie.getId());
		model.addAttribute("idSerieApi", serie.getIdApi());
		
		UtilidadesLista<Personaje> utilList = new UtilidadesLista<Personaje>();
		model.addAttribute("personajes", utilList.dividirListaPorNumeroElementosEnSubLista(serie.getPersonajes(), Constantes.NUMERO_ELEMENTOS_FILA_PERSONAJES));
		
		model.addAttribute("temporadas", serie.getTemporadas());
		model.addAttribute("media", serie.getPuntuacionMediaEstrella());
		model.addAttribute("comentarios", serie.getComentarios());
		model.addAttribute("generos", serie.getGenerosToString());
		model.addAttribute("contadorLike", serie.getLikes());
		model.addAttribute("canalWeb", serie.getCanalWeb());
		model.addAttribute("canalTv", serie.getCanalTv());
		model.addAttribute("sitioWeb", serie.getSitioWeb());
		model.addAttribute("idioma", serie.getIdioma());
		model.addAttribute("estado", serie.getEstado());
		
		int puntuado = serie.getPuntuaciones().containsKey(usuario.getId()) ? 1 : 0;	
		boolean esAdministrador = usuario.getRoles().contains(repositorioRoles.findByTipo(Constantes.TIPO_ADMINISTRADOR));

		model.addAttribute(Constantes.MODEL_ATT_NOMBRE_USUARIO, usuario.getUsuario());
		model.addAttribute("puntuado", puntuado);
		model.addAttribute("controlLike", usuario.getLikeSeries().get(serie.getId()));
		model.addAttribute("esAdministrador", esAdministrador);
		
	}
}
