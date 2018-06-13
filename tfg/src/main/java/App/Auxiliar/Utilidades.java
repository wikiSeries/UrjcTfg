package App.Auxiliar;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;

import App.Model.Genero;
import App.Model.Rol;
import App.Model.Serie;
import App.Model.Usuario;

public class Utilidades {
	private static Logger logger = Logger.getLogger("file");
	private Utilidades() {
		
	}
	
	public static final List<Long> getIdSeriesNoRepeat(String idSeries){
		String idSeriesDec = getTextDecoded(idSeries);
		String [] idSeriesSplit = idSeriesDec.split(",");
		List<Long> noRepeat = new ArrayList<Long>();
		
		for(String id : idSeriesSplit){
			if(noRepeat.size() <= Constantes.NUMERO_IDSERIE_COOKIE && !noRepeat.contains(Long.parseLong(id))) {
				noRepeat.add(Long.parseLong(id));
			}
		}
		
		return noRepeat;
	}
	
	
	public static final List<Genero> getGenerosSeriesCookies(List<Serie> seriesCookies){
		List<Genero> generos = new ArrayList<Genero>();
		
		for(Serie serie : seriesCookies) {
			
			if(!serie.getGeneros().isEmpty()) {
				List <Genero> generosSerie = serie.getGeneros();
				for(Genero g : generosSerie) {
					if (!generos.contains(g)) {
						generos.add(g);
						break;
					}
				}
			}
		}
				
		return generos;
	}
	
	
	public static final String codificarContraseña(String contraseña) throws NoSuchAlgorithmException {

		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] passBytes = contraseña.getBytes();
		md.reset();
		byte[] digested = md.digest(passBytes);
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<digested.length;i++){
		    sb.append(Integer.toHexString(0xff & digested[i]));
		}
		return sb.toString();
  
	}
	
	public static final boolean comprobarContraseña(String contraseña, String contraseñaCodificada) throws NoSuchAlgorithmException {
		String securePassword = codificarContraseña(contraseña);
		
		return securePassword.equals(contraseñaCodificada);
	}
	
	public static final String generarNuevaContraseñaAleatoria() {
		return RandomStringUtils.randomAlphanumeric(Constantes.LONGITUD_CONTRASENA_ALEATORIA);

	}
	
	public static final boolean validarFormatoCorreo(String correo){
		
		Matcher matcher = Constantes.VALID_EMAIL_ADDRESS_REGEX.matcher(correo);
        return matcher.find();
	}
	
	public static String formatedExceptionMessage(Exception ex) {
		
		StringBuilder exceptionMessage = new StringBuilder();
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		ex.printStackTrace(pw);
		
		exceptionMessage
			.append(String.format("Causa: %s%n", ex.getCause()))
			.append(String.format("Descripcion: %s%n", ex.getMessage()))
			.append(String.format("Rastro de la pila: %s", sw.toString()));
		
		return exceptionMessage.toString();
	}
	
	public static final void noCachearRespuestaHTTP(HttpServletResponse httpResponse) {
		
		httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		httpResponse.setHeader("Pragma", "no-cache");
		httpResponse.setDateHeader("Expires", 0);
		
	}

	public static final Cookie getCookie(HttpServletRequest httpRequest, String nombreCookie) {
		String nombreCookieCod = getTextEncoded(nombreCookie);
		Cookie [] cookies = httpRequest.getCookies();
		Cookie cookie = null;
		if(cookies != null) {
			for(Cookie c : cookies) {
				if(c.getName().equals(nombreCookieCod)) {
					cookie = c;
					break;
				}
			}
		}
		return cookie;
	}

	public static final void actualizarCookie(Cookie cookie, String extensionCookie) {
		List <Long> idSeriesNoRepeat = getIdSeriesNoRepeat(cookie.getValue());
		String newValueCookie = null;
		for(Long id : idSeriesNoRepeat) {
			if(idSeriesNoRepeat.get(0).equals(id)) {
				newValueCookie = id.toString();
			}
			else {
				newValueCookie = String.format("%s,%s", newValueCookie, id.toString()); 
			}
		}
		
		newValueCookie = String.format("%s,%s", extensionCookie, newValueCookie);
		String newValueCookieCod = getTextEncoded(newValueCookie);
		cookie.setValue(newValueCookieCod);
		//cookie.setMaxAge(Constantes.DIAS_EXPIRACION_COOKIE_SERIE_USUARIO * 3600);
		cookie.setPath("/");
	}

	public static final Cookie crearCookie(String clave, String valor) {
		String claveCod = getTextEncoded(clave);
		String valorCod = getTextEncoded(valor);
		
		Cookie cookie = new Cookie(claveCod, valorCod);
		cookie.setMaxAge(Constantes.DIAS_EXPIRACION_COOKIE_SERIE_USUARIO * 3600);
		cookie.setPath("/");
		cookie.setSecure(true);
		
		return cookie;
	}
	
	public static final String getTextEncoded(String text) {
		return Base64.getEncoder().withoutPadding().encodeToString(text.getBytes());		
	}
	
	public static final String getTextDecoded(String textEncoded) {
		byte [] bytesDecode = Base64.getDecoder().decode(textEncoded);
		
		return new String(bytesDecode);
	}
	
	public static final String getDateToday() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date today = Calendar.getInstance().getTime();
		return dateFormat.format(today);
	}
	
	public static final String logErrorAndGetPageError(Exception ex, String nameMethod,  Model model, String error, String descripcion) {
		if(ex != null && nameMethod != null) {
			logger.error(String.format("%s%n%s", nameMethod, formatedExceptionMessage(ex)));
		}
		else if(ex == null){
			logger.error(String.format("%s%n%s", nameMethod, descripcion));
		}

		model.addAttribute(Constantes.MODEL_ATT_ERROR, error);
		model.addAttribute(Constantes.MODEL_ATT_DESCRIPCION, descripcion);
		return Constantes.TEMPLATE_PAGINA_ERROR;
	}
	
	public static final void crearModeloPerfil(Model model, Usuario usuario, Rol rolAdmin) {
		String tipoUsuario = usuario.getRoles().contains(rolAdmin) ? Constantes.TIPO_ADMINISTRADOR : Constantes.TIPO_BASICO;
		model.addAttribute(Constantes.MODEL_ATT_NOMBRE, String.format("%s %s", usuario.getNombre(), usuario.getApellidos()));
		model.addAttribute(Constantes.MODEL_ATT_NOMBRE_USUARIO, usuario.getUsuario());
		model.addAttribute(Constantes.MODEL_ATT_CORREO, usuario.getCorreo());
		model.addAttribute(Constantes.MODEL_ATT_FECHA_REGISTRO, usuario.getFechaCreacion());
		model.addAttribute(Constantes.MODEL_ATT_TIPO_USUARIO, tipoUsuario);
	}
	

}
