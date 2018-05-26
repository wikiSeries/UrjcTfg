package App.Controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import App.Auxiliar.Constantes;
import App.Auxiliar.Utilidades;
import App.Auxiliar.UtilidadesLista;
import App.Model.Actor;
import App.Model.Genero;
import App.Model.Serie;
import App.Model.Usuario;
import App.Repository.RepositorioActores;
import App.Repository.RepositorioGeneros;
import App.Repository.RepositorioRoles;
import App.Repository.RepositorioSeries;
import App.Repository.RepositorioUsuario;

@Controller
public class NavegacionController {
	
	private Logger logger = Logger.getLogger("file");
	
	@Autowired
	private RepositorioUsuario repositorioUsuarios;
	
	@Autowired
	private RepositorioSeries repositorioSeries;
	
	@Autowired
	private RepositorioGeneros repositorioGeneros;
	
	@Autowired
	private RepositorioActores repositorioActores;
	
	@Autowired
	private RepositorioRoles repositorioRoles;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String getLogin() {
		return "Login";
	}
	
	@RequestMapping(value="/Registro", method = RequestMethod.GET)
	public String getRegistro() {
		
		return "RegistroUsuario";
		
	}
	
	@RequestMapping(value="/RecuperarCuenta", method = RequestMethod.GET)
	public String getCuenta() {
		
		return "RecuperarCuenta";
		
	}
	
	@RequestMapping(value="/SubirSerie", method = RequestMethod.GET)
	public String getBuscarSerie(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model) {
		Utilidades.noCachearRespuestaHTTP(httpResponse);
		HttpSession session = httpRequest.getSession(false);
		if(session != null) {
			String nombre = (String) session.getAttribute("user");
			model.addAttribute("nombreUsuario", nombre);
			return "BuscarSerie";
		}
		
		return "redirect:/login";
		
	}
	
	@RequestMapping(value = "/PaginaPrincipal/{pagina}", method = RequestMethod.GET)
	public String getPaginaPrincipal(@PathVariable int pagina,
									HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model) {
		
		try {
			Utilidades.noCachearRespuestaHTTP(httpResponse);
			HttpSession session = httpRequest.getSession(false);

			if(session != null) {
				String nombre = (String) session.getAttribute("user");
				
				PageRequest pageRequest = new PageRequest(pagina - 1, Constantes.NUMERO_SERIES_PAGINA, Sort.Direction.ASC, "id");
				Page<Serie> page = repositorioSeries.findAll(pageRequest);
				
				int paginaActual = page.getNumber() + 1;
				int paginaAnterior = paginaActual - 1;
				int paginaSiguiente = paginaActual + 1;
				
				int [] indices = new int[page.getTotalPages()];
				for(int i = 0; i < indices.length; i++) {
					indices[i] = i + 1;
				}
				
				boolean esAdministrador = repositorioUsuarios.findByUsuario(nombre).getRoles().contains(repositorioRoles.findByTipo(Constantes.TIPO_ADMINISTRADOR));
				
				model.addAttribute("nombreUsuario", nombre);
				model.addAttribute("paginaActual", paginaActual);
				model.addAttribute("series", page);
				model.addAttribute("indices", indices);
				model.addAttribute("paginaAnterior", paginaAnterior);
				model.addAttribute("paginaSiguiente", paginaSiguiente);
				model.addAttribute("esAdministrador", esAdministrador);
				
				
				UtilidadesLista<Genero> utilList = new UtilidadesLista<Genero>();
				model.addAttribute("filtroGeneros", utilList.dividirListaPorNumeroDeSubListas(repositorioGeneros.findAll(), Constantes.NUMERO_PARTICION_LISTA_GENEROS));
				
				List<Serie> seriesCookie = new ArrayList<Serie>();
				Cookie cookie = Utilidades.getCookie(httpRequest, nombre);			
				if(cookie != null) {			
					seriesCookie = getSeriesCookies(cookie);			
				}
					

				model.addAttribute("seriesCookie", seriesCookie);
				
				return "PaginaPrincipal";
			}
			
			return "redirect:/login";
		}
		catch(Exception ex) {
			logger.error(String.format("Pagina Principal%n%s", Utilidades.formatedExceptionMessage(ex)));
			model.addAttribute("error", "Pagina Principal");
			model.addAttribute("descripcion", "Se ha producido un error al cargar la pagina principal."
								+ "Vuelva a intentarlo mas tarde o pongase en contacto con el administrador.");
			return "PaginaError";
		}
		
		
	}
	
	@RequestMapping(value = "/Genero/{genero}/{pagina}", method = RequestMethod.GET)
	public String filtroGenero(@PathVariable String genero, @PathVariable int pagina,
								HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model) {
		
		try {
			Utilidades.noCachearRespuestaHTTP(httpResponse);
			HttpSession session = httpRequest.getSession(false);
			if(session != null) {
				String nombre = (String) session.getAttribute("user");
				
				PageRequest pageRequest = new PageRequest(pagina - 1, Constantes.NUMERO_SERIES_PAGINA, Sort.Direction.ASC, "id");
				List<Serie> series = repositorioSeries.getSeries(genero);
				
				int start = pageRequest.getOffset();
				int end = (start + pageRequest.getPageSize()) > series.size() ? series.size() : (start + pageRequest.getPageSize());
				
				Page<Serie> pages = new PageImpl<Serie>(series.subList(start, end), pageRequest, series.size());
				
				int paginaActual = pages.getNumber() + 1;
				int paginaAnterior = paginaActual - 1;
				int paginaSiguiente = paginaActual + 1;
				
				int [] indices = new int[pages.getTotalPages()];
				for(int i = 0; i < indices.length; i++) {
					indices[i] = i + 1;
				}
				
				String urlPaginacion = String.format("/Genero/%s", genero);
				
				model.addAttribute("paginaActual", paginaActual);
				model.addAttribute("series", pages);
				model.addAttribute("indices", indices);
				model.addAttribute("paginaAnterior", paginaAnterior);
				model.addAttribute("paginaSiguiente", paginaSiguiente);
				model.addAttribute("filtro", genero);
				model.addAttribute("tipoFiltro", "filtrar");
				model.addAttribute("nombreUsuario", nombre);
				model.addAttribute("urlPaginacion",  urlPaginacion);
				
				return "FiltroGenero";

			}
			
			return  "redirect:/login";
		}
		catch(Exception ex) {
			logger.error(String.format("Genero%n%s", Utilidades.formatedExceptionMessage(ex)));
			model.addAttribute("error", "Filtrar por genero");
			model.addAttribute("descripcion", "Se ha producido un error al filtrar las series por el genero."
								+ "Vuelva a intentarlo mas tarde o pongase en contacto con el administrador.");
			return "PaginaError";
		}
		
		
	}
	
	@RequestMapping(value = "/BuscarTitulo/{titulo}/{pagina}", method = RequestMethod.GET)
	public String buscarTitulo(@PathVariable("titulo") String titulo,
								@PathVariable("pagina") int pagina,
								HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model) {
		try {
			Utilidades.noCachearRespuestaHTTP(httpResponse);
			HttpSession session = httpRequest.getSession(false);
			if(session != null) {
				String nombre = (String) session.getAttribute("user");
				PageRequest pageRequest = new PageRequest(pagina - 1, Constantes.NUMERO_SERIES_PAGINA, Sort.Direction.ASC, "titulo");
				
				Page<Serie> pages = repositorioSeries.findByTituloContaining(titulo, pageRequest);
				String urlPaginacion = String.format("/BuscarTitulo/%s", titulo);	
					
				int paginaActual = pages.getNumber() + 1;
				int paginaAnterior = paginaActual - 1;
				int paginaSiguiente = paginaActual + 1;
				
				int [] indices = new int[pages.getTotalPages()];
				for(int i = 0; i < indices.length; i++) {
					indices[i] = i + 1;
				}
				
				model.addAttribute("paginaActual", paginaActual);
				model.addAttribute("series", pages);
				model.addAttribute("indices", indices);
				model.addAttribute("paginaAnterior", paginaAnterior);
				model.addAttribute("paginaSiguiente", paginaSiguiente);
				model.addAttribute("filtro", titulo);
				model.addAttribute("tipoFiltro", "buscar");
				model.addAttribute("nombreUsuario", nombre);
				model.addAttribute("urlPaginacion", urlPaginacion);
				
				return "FiltroGenero";
			}
			
			return "redirect:/login";
		}
		catch(Exception ex) {
			logger.error(String.format("BuscarTitulo%n%s", Utilidades.formatedExceptionMessage(ex)));
			model.addAttribute("error", "Buscar serie por titulo");
			model.addAttribute("descripcion", "Se ha producido un error al buscar la serie por el su titulo."
								+ "Vuelva a intentarlo mas tarde o pongase en contacto con el administrador.");
			return "PaginaError";
		}
		
	}
	
	@RequestMapping(value = "/BusquedaAvanzada", method = RequestMethod.GET)
	public String getBusquedaAvanzada(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model) {
		
		try {
			Utilidades.noCachearRespuestaHTTP(httpResponse);
			HttpSession session = httpRequest.getSession(false);
			if(session != null) {
				String nombre = (String) session.getAttribute("user");
				int [] estrellas = {1, 2, 3, 4, 5};
				List<String> anos = repositorioSeries.getAnos();
				List<Genero> generos = repositorioGeneros.findAll();
				
				model.addAttribute("nombreUsuario", nombre);
				model.addAttribute("titulo", "");
				model.addAttribute("nombreActor", "");
				model.addAttribute("nombrePersonaje", "");
				model.addAttribute("estrellas", estrellas);
				model.addAttribute("anos", anos);
				model.addAttribute("generos", generos);
				
				return "BusquedaAvanzada";
			}
			
			return "redirect:/login";
		}
		catch(Exception ex) {
			logger.error(String.format("BusquedaAvanzada%n%s", Utilidades.formatedExceptionMessage(ex)));
			model.addAttribute("error", "Busqueza avanzada");
			model.addAttribute("descripcion", "Se ha producido un error al utilizar la busqueda avanzada."
								+ "Vuelva a intentarlo mas tarde o pongase en contacto con el administrador.");
			return "PaginaError";
		}
		
		
	}
	
	@RequestMapping(value = "/Perfil", method = RequestMethod.GET)
	public String perfil(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model) {
		try {
			Utilidades.noCachearRespuestaHTTP(httpResponse);			
			HttpSession session = httpRequest.getSession(false);
			if(session != null) {
				String nombre = (String) session.getAttribute("user");
				Usuario usuario = repositorioUsuarios.findByUsuario(nombre);
				String tipoUsuario = usuario.getRoles().contains(repositorioRoles.findByTipo(Constantes.TIPO_ADMINISTRADOR)) ? Constantes.TIPO_ADMINISTRADOR : Constantes.TIPO_BASICO;
				
				model.addAttribute("nombre", String.format("%s %s", usuario.getNombre(), usuario.getApellidos()));
				model.addAttribute("nombreUsuario", usuario.getUsuario());
				model.addAttribute("correo", usuario.getCorreo());
				model.addAttribute("fechaRegistro", usuario.getFechaCreacion());
				model.addAttribute("tipoUsuario", tipoUsuario);
				
				return "Perfil";
				
			}
			
			return "redirect:/login";
		}
		catch(Exception ex) {
			logger.error(String.format("Perfil%n%s", Utilidades.formatedExceptionMessage(ex)));
			model.addAttribute("error", "Mi perfil");
			model.addAttribute("descripcion", "Se ha producido un error al acceder a su perfil."
								+ "Vuelva a intentarlo mas tarde o pongase en contacto con el administrador.");
			return "PaginaError";
		}
		
	}
	
	@RequestMapping(value = "/Logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException {
		
		HttpSession session = httpRequest.getSession();
		session.invalidate();
		
		return "redirect:/login";
	}
	
	@RequestMapping(value = "/CambiarPassword", method = RequestMethod.GET)
	public String perfilFromCambiarPassword() {		
		return "redirect:/Perfil";
	}
	
	@RequestMapping(value = "/CambiarCorreo", method = RequestMethod.GET)
	public String perfilFromCambiarCorreo() {		
		return "redirect:/Perfil";
	}
	
	@RequestMapping(value = "/FichaInterprete/{idInterprete}", method = RequestMethod.GET)
	public String infoInterprete(@PathVariable("idInterprete") Long idInterprete,
								HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model) {
		try {
			Utilidades.noCachearRespuestaHTTP(httpResponse);
			HttpSession session = httpRequest.getSession(false);
			if(session != null) {
				String nombre = (String) session.getAttribute("user");
				Actor interprete = repositorioActores.findOne(idInterprete);
				model.addAttribute("nombreUsuario", nombre);
				model.addAttribute("nameInterpreter", interprete.getNombre());
				model.addAttribute("urlImagenInterprete", interprete.getUrlImagen());
				model.addAttribute("countryInterpreter", interprete.getPais());
				model.addAttribute("birthdayInterpreter", interprete.getFechaNacimiento());
				model.addAttribute("deathdayInterpreter", interprete.getFechaFallecimiento());
				
				UtilidadesLista<Serie> utilList = new UtilidadesLista<Serie>();
				model.addAttribute("series", utilList.dividirListaPorNumeroElementosEnSubLista(interprete.getSeries(), Constantes.NUMERO_ELEMENTOS_FILA_SERIES));
				
				return "FichaInterprete";
			}
			
			return "redirect:/login";
		}
		catch(Exception ex) {
			logger.error(String.format("FichaInterprete%n%s", Utilidades.formatedExceptionMessage(ex)));
			model.addAttribute("error", "Ficha interprete");
			model.addAttribute("descripcion", "Se ha producido al cargar la informacion del interprete seleccionado."
								+ "Vuelva a intentarlo mas tarde o pongase en contacto con el administrador.");
			return "PaginaError";
		}
		
	}
	
	private List<Serie> getSeriesCookies(Cookie cookie){
		List<Long> idSeriesCookieNoRepeat = Utilidades.getIdSeriesNoRepeat(cookie.getValue());
		List<Serie> seriesCookiesNoRepeat = repositorioSeries.findByIdIn(idSeriesCookieNoRepeat);

		List<Genero> generosSeriesCookies = Utilidades.getGenerosSeriesCookies(seriesCookiesNoRepeat);
		PageRequest pageRequest = new PageRequest(0, Constantes.NUMERO_IDSERIE_COOKIE, Sort.Direction.ASC, "puntuacionMediaEstrella");
		return repositorioSeries.findDistinctByGenerosIn(generosSeriesCookies, pageRequest);
		
	}

	
}
