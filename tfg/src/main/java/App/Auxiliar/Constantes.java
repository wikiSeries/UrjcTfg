package App.Auxiliar;

import java.util.regex.Pattern;

public class Constantes {
	
	private Constantes() {
		
	}
	public static final String TIPO_ADMINISTRADOR = "Administrador";
	public static final String TIPO_BASICO = "Basico";
	
	public static final String USUARIO_CORREO_APP = "wikiseriestfg";
	public static final String CONTRASENA_CORREO_APP = "wikiSeries123";
	
	public static final int  NUMEROINTENTOSLOGIN = 3;
	public static final int  NUMERO_SERIES_PAGINA = 6;
	public static final int NUMERO_PARTICION_LISTA_GENEROS = 3;
	public static final int TIEMPO_COOKIE_SERIE_USUARIO = 600;
	public static final int NUMERO_IDSERIE_COOKIE = 5;
	public static final int LONGITUD_CONTRASENA_ALEATORIA = 8;
	public static final int NUMERO_ELEMENTOS_FILA_PERSONAJES = 6;
	public static final int NUMERO_ELEMENTOS_FILA_SERIES = 6;
	public static final int NUMERO_LIMITE_COLUMNA_TEXTO = 65535;
	
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	public static final String FINAL_PARRAFO = "...</p>";
	
	public static final String CONTACT_WITH_ADMIN = "Vuelva a intentarlo mas tarde o pongase en contacto con el administrador.";
	public static final String ERROR_USUARIO = "Usuario incorrecto. No existe el usuario '%s'";
	public static final String ERROR_CONTRASENA = "Contraseña incorrecta";
	public static final String ERROR_SERVIDOR_DESC = "Error en el servidor. Vuelva a intentar conectarse a WikiSeries y en caso de que el error continue pongase en contacto con el administrador.";
	public static final String ERROR_BLOQ_DESC = "No le quedan mas intentos, su cuenta esta bloqueada. Pongase en contacto con el administrador.";
	public static final String ERROR_CONTRASENA_DESC = "La contraseña introducida es incorrecta. Le quedan %d intentos. Si agota el numero de intentos, la cuenta se bloqueara por motivos de seguridad.";
	public static final String ERROR_ACTIVAR_DESC = "Su cuenta aun no ha sido activa, consulte su correo.";
	public static final String ERROR_BLOQ = "Cuenta bloqueada.";
	
	public static final String REGISTRO_OK = "Registro realizado correctamente.Se le ha enviado un mensaje de correo con un enlace de activacion de cuenta al correo %s. Por favor compruebe su correo para commenzar a disfrutar de WikiSeries."
											+ "Despues de 24 horas el enlace de activacion expirara.";
	public static final String ERROR_REGISTRO = "Error al enviar el correo de confirmacion de nuevo registro de cuenta. Vuelva a intentarlo mas tarde.";
	public static final String ERROR_REGISTRO_CONFIRMACION_CONTRASENA = "Error de confirmacion de contraseña. Por favor introduzca la misma clave en el campo 'contraseña' y 'confirmar contraseña'";
	public static final String ERROR_REGISTRO_USUARIO = "Ya existe una cuenta con el nombre de usuario '%s'. Vuelva a introducir los datos del registro con un nombre de usuario diferente.";
	
	public static final String ERROR_RECU_CUENTA = "Su cuenta aun no esta activa, por favor compruebe su correo para finalizar el proceso de registro."
			+ "										En caso de haberse registrado el dia de hoy y haber bloqeuado la cuenta, debera de esperar un maximo de un dia"
			+ "										para poder recuperar su cuenta.";
	public static final String RECU_CUENTA_OK = "Recuperacion de cuenta realizada con exito. Se le ha enviado los datos de recuperacion de cuenta a la direccion de correo '%s'.";
	public static final String ERROR_RECU_CUENTA_CORREO = "Error al enviar el correo de recuperacion de cuenta";
	public static final String ERROR_RECU_CUENTA_USUARIO = "No existe la cuenta con el nombre de usuario '%s'.";
	
	public static final String ADVANCE_SEARCH_QUERY = "SELECT * FROM serie s";
	public static final String ADVANCE_SEARCH_QUERY_TITULO = "(s.titulo LIKE CONCAT('%', :tituloSerie, '%'))";
	public static final String ADVANCE_SEARCH_QUERY_ANO = "(s.fecha_estreno LIKE CONCAT('%', :ano, '%'))";
	public static final String ADVANCE_SEARCH_QUERY_INTERPRETE = "(s.id in (SELECT series_id FROM actor_series acs WHERE acs.actores_id IN (SELECT id FROM actor a WHERE (a.nombre LIKE CONCAT('%', :nombreActor, '%')))))";
	public static final String ADVANCE_SEARCH_QUERY_PERSONAJE = "(s.id in (SELECT series_id FROM actor_series acs WHERE acs.actores_id IN (SELECT ACTOR_id FROM personaje p WHERE (p.nombre LIKE CONCAT('%', :nombrePersonaje, '%')))))";
	public static final String ADVANCE_SEARCH_QUERY_GENERO = "(s.id IN (SELECT series_id FROM genero_series gs WHERE gs.generos_id IN (SELECT id FROM genero g WHERE g.titulo = :tituloGenero)))";
	public static final String ADVANCE_SEARCH_QUERY_PUNTUACION = "(s.puntuacion_media_estrella = :puntuacion)";
	
	public static final String CHANGE_EMAIL_ADDRESS_OK = "Se le ha enviado una mensaje de confirmacion de cambio de direccion de correo. Por favor compruebe su correo y verifique la nueva direccion email.";
	public static final String CHANGE_EMAIL_ADDRESS_CONFIRM_OK = "Se ha cambiado la direccion de correo correctamente";
	public static final String ERROR_EMAIL_ADDRESS = "La direccion de correo no es valida.";
	public static final String ERROR_SEND_CONFIRM_CHANGE_EAMIL = "Se ha producido un error al enviar el correo de confirmacion de cambio de direccion de correo. Por favor asegurese de que su nueva direccion de correo es correcta e intentelo mas tarde.";
	
	public static final String ERROR_LINK_EXPIRED = "Error: enlace de confirmacion";
	public static final String ERROR_LINK_EXPIRED_DESC = "Se ha producido un error al acceder a un enlace de confirmacion que ha expirado tras 24 horas de su generación.";

	public static final String WHERE_SQL = "WHERE";
	public static final String AND_SQL = "AND";
	
	public static final String MODEL_ATT_TIPO_USUARIO = "tipoUsuario";
	public static final String MODEL_ATT_SERIES = "series";
	public static final String MODEL_ATT_NOMBRE_USUARIO = "nombreUsuario";
	public static final String MODEL_ATT_ERROR = "error";
	public static final String MODEL_ATT_DESCRIPCION = "descripcion";
	public static final String MODEL_ATT_NEXT_PAG = "paginaSiguiente";
	public static final String MODEL_ATT_PREV_PAG = "paginaAnterior";
	public static final String MODEL_ATT_CURRENT_PAG = "paginaActual";
	public static final String MODEL_ATT_NOMBRE = "nombre";
	public static final String MODEL_ATT_MSG_ERROR = "mensajeError";
	public static final String MODEL_ATT_INDICES_PAG = "indices";
	public static final String MODEL_ATT_FECHA_REGISTRO = "fechaRegistro";
	public static final String MODEL_ATT_CORREO = "correo";



	public static final String URL_HTTPS ="https";

	public static final String REDIRECT_LOGIN = "redirect:/login";
	
	public static final String TEMPLATE_PAGINA_ERROR = "PaginaError";
	public static final String TEMPLATE_PERFIL = "Perfil";

	

}
