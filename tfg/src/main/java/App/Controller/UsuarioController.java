package App.Controller;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import App.Auxiliar.Constantes;
import App.Auxiliar.Utilidades;

import App.Model.Comentario;
import App.Model.Conexion;
import App.Model.Rol;
import App.Model.Serie;
import App.Model.Usuario;

import App.Repository.RepositorioComentarios;
import App.Repository.RepositorioConexiones;
import App.Repository.RepositorioRoles;
import App.Repository.RepositorioSeries;
import App.Repository.RepositorioUsuario;

@Controller
public class UsuarioController {

	private Logger logger = Logger.getLogger("file");

	@Autowired
	private RepositorioUsuario repositorioUsuarios;

	@Autowired
	private RepositorioConexiones repositorioConexiones;

	@Autowired
	private RepositorioSeries repositorioSeries;

	@Autowired
	private RepositorioComentarios repositorioComentarios;

	@Autowired
	private RepositorioRoles repositorioRoles;

	@RequestMapping(value = "/IniciarSesion", method = RequestMethod.POST)
	public String login(@RequestParam("ciudad") String ciudad, @RequestParam("pais") String pais,
			@RequestParam("ip") String ip, @RequestParam("provincia") String provincia,
			@RequestParam("codigoPostal") String codigoPostal,

			@RequestParam("user") String nombreUsuario, @RequestParam("password") String contrasena,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model)
			throws NoSuchAlgorithmException {

		try {
			Usuario usuario = repositorioUsuarios.findByUsuario(nombreUsuario);
			StringBuilder errorUsuario = new StringBuilder();
			StringBuilder errorContraseña = new StringBuilder();
			StringBuilder errorDescripcion = new StringBuilder();

			if (this.validarUsuario(usuario, nombreUsuario, contrasena, errorUsuario, errorContraseña,
					errorDescripcion)) {
				Conexion conexion = new Conexion(ciudad, pais, ip, provincia, codigoPostal);

				conexion.setUsuario(usuario);
				usuario.getConexiones().add(conexion);

				repositorioConexiones.save(conexion);
				repositorioUsuarios.save(usuario);

				HttpSession session = httpRequest.getSession();
				session.setAttribute("user", nombreUsuario);
				session.setMaxInactiveInterval(300);

				return "redirect:/PaginaPrincipal/1";

			}

			if (usuario != null) {
				repositorioUsuarios.save(usuario);
			}

			model.addAttribute("errorUsuario", errorUsuario.toString());
			model.addAttribute("errorContrasena", errorContraseña.toString());
			model.addAttribute("errorDescripcion", errorDescripcion.toString());

			return "/Login";
		} catch (Exception ex) {

			logger.error(String.format("Iniciar Sesion%n%s", Utilidades.formatedExceptionMessage(ex)));

			model.addAttribute(Constantes.MODEL_ATT_ERROR, "Inicio de sesion");
			model.addAttribute(Constantes.MODEL_ATT_DESCRIPCION, "Se ha producido un error al intentar iniciar la sesion actual."
					+ Constantes.CONTACT_WITH_ADMIN);
			return Constantes.TEMPLATE_PAGINA_ERROR;
		}

	}

	private boolean validarUsuario(Usuario usuario, String nombreUsuario, String contrasena,
			StringBuilder errorUsuario, StringBuilder errorContraseña, StringBuilder errorDescripcion)
			throws NoSuchAlgorithmException {

		if (usuario != null) {

			if (Utilidades.comprobarContraseña(contrasena, usuario.getContraseña())) {
				if (!usuario.isBloqueado()) {
					usuario.setIntentos(Constantes.NUMEROINTENTOSLOGIN);
					return true;
				} else {
					errorDescripcion.append(Constantes.ERROR_BLOQ_DESC);
				}
			} else {
				if (!usuario.isBloqueado()) {
					usuario.setIntentos(usuario.getIntentos() - 1);
					usuario.setBloqueado(usuario.isBloqueado());
					if (usuario.isBloqueado()) {
						errorDescripcion.append(Constantes.ERROR_BLOQ_DESC);
					} else {
						errorContraseña.append(Constantes.ERROR_CONTRASENA);
						errorDescripcion.append(String.format(Constantes.ERROR_CONTRASENA_DESC, usuario.getIntentos()));
					}

				} else {
					errorDescripcion.append(String.format("%s Causas del error:%n-%s%n%s", Constantes.ERROR_BLOQ,
							Constantes.ERROR_BLOQ_DESC, Constantes.ERROR_ACTIVAR_DESC));
				}

			}

		} else {
			errorUsuario.append(String.format(Constantes.ERROR_USUARIO, nombreUsuario));
		}

		return false;
	}

	@RequestMapping(value = "/Registro", method = RequestMethod.POST)
	public String registrarUsuario(@RequestParam("name") String nombre, @RequestParam("surname") String apellidos,
			/** @RequestParam("phone") String telefono, **/
			@RequestParam("email") String correo, @RequestParam("username") String nombreUsuario,
			@RequestParam("password") String contraseña, @RequestParam("passwordConfirm") String confirmacionContraseña,
			Model model) throws NoSuchAlgorithmException {

		try {
			Usuario usuario = repositorioUsuarios.findByUsuario(nombreUsuario);

			if (usuario == null) {
				if(Utilidades.validarFormatoCorreo(correo)) {
					if (contraseña.equals(confirmacionContraseña)) {
						Rol rolBasico = repositorioRoles.findByTipo(Constantes.TIPO_BASICO);
						usuario = new Usuario(nombreUsuario, contraseña, nombre, apellidos, correo, null);

						rolBasico.getUsuarios().add(usuario);
						usuario.getRoles().add(rolBasico);

						Email email = new Email();
						StringBuilder cuerpoMensaje = email.crearMensajeValidacion(usuario);

						if (email.enviarCorreo(usuario.getCorreo(), "Registro usuario", cuerpoMensaje.toString())) {

							usuario.setContraseña(Utilidades.codificarContraseña(contraseña));
							repositorioUsuarios.save(usuario);
							repositorioRoles.save(rolBasico);

							model.addAttribute("mensajeOk", String.format(Constantes.REGISTRO_OK, usuario.getCorreo()));

						} else {
							model.addAttribute(Constantes.MODEL_ATT_ERROR, "Enviar correo");
							model.addAttribute(Constantes.MODEL_ATT_DESCRIPCION, Constantes.ERROR_REGISTRO);
							return Constantes.TEMPLATE_PAGINA_ERROR;
						}

					} else {
						model.addAttribute(Constantes.MODEL_ATT_MSG_ERROR, Constantes.ERROR_REGISTRO_CONFIRMACION_CONTRASENA);
					}
				}
				else {
					model.addAttribute(Constantes.MODEL_ATT_MSG_ERROR, Constantes.ERROR_EMAIL_ADDRESS);
				}

			} else {
				model.addAttribute(Constantes.MODEL_ATT_MSG_ERROR, String.format(Constantes.ERROR_REGISTRO_USUARIO, nombreUsuario));
			}

			return "RegistroUsuario";
		} catch (Exception ex) {
			logger.error(String.format("Registro de usuario%n%s", Utilidades.formatedExceptionMessage(ex)));

			model.addAttribute(Constantes.MODEL_ATT_ERROR, "Registro");
			model.addAttribute(Constantes.MODEL_ATT_DESCRIPCION, "Se ha producido un error al intentar registrar un nuevo usuario"
					+ Constantes.CONTACT_WITH_ADMIN);

			return Constantes.TEMPLATE_PAGINA_ERROR;
		}

	}

	@RequestMapping(value = "/ActivarCuentaUsuario", method = RequestMethod.GET)
	public String activarCuenta(@RequestParam("usuario") String nombreUsuario, Model model) {

		try {
			Usuario usuario = repositorioUsuarios.findByUsuario(nombreUsuario);

			String mensaje;
			String descripcion;

			if (usuario != null) {
				String fechaActual = Utilidades.getDateToday();

				if (fechaActual.equals(usuario.getFechaCreacion())) {
					usuario.setBloqueado(false);
					usuario.setIntentos(Constantes.NUMEROINTENTOSLOGIN);

					repositorioUsuarios.save(usuario);

					model.addAttribute("confirmActivacion", "cuenta activada");

					return "login";
				}

				repositorioUsuarios.delete(usuario);

				mensaje = "Error de activacion de cuenta";
				descripcion = "El tiempo de activacion para la cuenta con el nombre de usuario '" + usuario.getUsuario()
						+ "'  ha expirado.";

			}

			else {
				mensaje = "Error de activacion de cuenta";
				descripcion = "No existe el usuario '" + nombreUsuario + "'.";
			}

			logger.error(String.format("Activar cuenta: %s", descripcion));

			model.addAttribute(Constantes.MODEL_ATT_ERROR, mensaje);
			model.addAttribute(Constantes.MODEL_ATT_DESCRIPCION, descripcion);

			return Constantes.TEMPLATE_PAGINA_ERROR;
		} catch (Exception ex) {
			logger.error(String.format("ActivarCuentaUsuario%n%s", Utilidades.formatedExceptionMessage(ex)));

			model.addAttribute(Constantes.MODEL_ATT_ERROR, "Activacion de cuenta");
			model.addAttribute(Constantes.MODEL_ATT_DESCRIPCION, "Se ha producido un error al intentar activar su cuenta de usuario"
					+ Constantes.CONTACT_WITH_ADMIN);

			return Constantes.TEMPLATE_PAGINA_ERROR;
		}

	}

	@RequestMapping(value = "/RecordarCuenta", method = RequestMethod.POST)
	public String recuperarCuenta(@RequestParam("username") String nombreUsuario, Model model)
			throws NoSuchAlgorithmException {

		try {
			Usuario usuario = repositorioUsuarios.findByUsuario(nombreUsuario);

			if (usuario != null) {

				String fechaActual = Utilidades.getDateToday();

				// Si el usuario se ha registrado hoy
				if (fechaActual.equals(usuario.getFechaCreacion()) && usuario.isBloqueado()) {
					model.addAttribute(Constantes.MODEL_ATT_MSG_ERROR, Constantes.ERROR_RECU_CUENTA);

				} else {
					Email email = new Email();
					String nuevaContraseña = Utilidades.generarNuevaContraseñaAleatoria();
					usuario.setContraseña(nuevaContraseña);
					StringBuilder cuerpoMensaje = email.crearMensajeRecuperacion(usuario);

					if (email.enviarCorreo(usuario.getCorreo(), "Recuperacion cuenta", cuerpoMensaje.toString())) {
						usuario.setContraseña(Utilidades.codificarContraseña(nuevaContraseña));
						usuario.setIntentos(Constantes.NUMEROINTENTOSLOGIN);
						usuario.setBloqueado(false);

						repositorioUsuarios.save(usuario);

						model.addAttribute("mensajeOk", String.format(Constantes.RECU_CUENTA_OK, usuario.getCorreo()));
					} else {
						model.addAttribute(Constantes.MODEL_ATT_MSG_ERROR, Constantes.ERROR_RECU_CUENTA_CORREO);
					}

				}

			}

			else {
				model.addAttribute(Constantes.MODEL_ATT_MSG_ERROR, String.format(Constantes.ERROR_RECU_CUENTA_USUARIO, nombreUsuario));

			}

			return "RecuperarCuenta";
		} catch (Exception ex) {
			logger.error(String.format("RecordarCuenta%n%s", Utilidades.formatedExceptionMessage(ex)));

			model.addAttribute(Constantes.MODEL_ATT_ERROR, "Recordar cuenta");
			model.addAttribute(Constantes.MODEL_ATT_DESCRIPCION,
					"Se ha producido un error al intentar devolver los datos de su cuenta de usuario"
							+ Constantes.CONTACT_WITH_ADMIN);

			return Constantes.TEMPLATE_PAGINA_ERROR;
		}

	}

	@RequestMapping(value = "/Comentario/{publicar}/{idSerie}/{idUsuario}/{idComentario}", method = RequestMethod.GET)
	public String gestionarComentario(@PathVariable String publicar, @PathVariable Long idSerie,
			@PathVariable Long idUsuario, @PathVariable Long idComentario, Model model) {
		try {
			String mensaje;
			String descripcion;
			Comentario comentario = repositorioComentarios.findById(idComentario);
			Usuario usuario = repositorioUsuarios.findById(idUsuario);
			Serie serie = repositorioSeries.findById(idSerie);
			Email email = new Email();

			StringBuilder cuerpoMensaje;

			if (publicar.equalsIgnoreCase("Publicar")) {

				comentario.setUsuario(usuario);
				comentario.setSerie(serie);

				usuario.getComentarios().add(comentario);
				serie.getComentarios().add(comentario);

				repositorioSeries.save(serie);
				repositorioUsuarios.save(usuario);
				repositorioComentarios.save(comentario);

				mensaje = "Comentario publicado correctamente";
				descripcion = "El comentario \"" + comentario.getMensaje() + "\" se ha publicado en la serie \""
						+ serie.getTitulo() + "\" correctamente";

				cuerpoMensaje = email.crearCuerpoComentarioPublicado(usuario.getNombre(), usuario.getApellidos(),
						comentario.getMensaje());
				email.enviarCorreo(usuario.getCorreo(), "Comentario Publicado", cuerpoMensaje.toString());
			} else {
				if (publicar.equalsIgnoreCase("InaPropiado")) {
					cuerpoMensaje = email.crearCuerpoComentarioNoPermitido(usuario.getNombre(), usuario.getApellidos(),
							comentario.getMensaje());
				} else {
					cuerpoMensaje = email.crearCuerpoComentarioSpoiler(usuario.getNombre(), usuario.getApellidos(),
							comentario.getMensaje());
				}

				repositorioComentarios.delete(idComentario);

				email.enviarCorreo(usuario.getCorreo(), "Comnetario no publicado", cuerpoMensaje.toString());

				mensaje = "Comentario no publicado";
				descripcion = "El comentario \"" + comentario.getMensaje() + "\" se ha eliminado correctamente";
			}

			model.addAttribute("mensaje", mensaje);
			model.addAttribute(Constantes.MODEL_ATT_DESCRIPCION, descripcion);

			return "PaginaOk";

		} catch (Exception ex) {
			logger.error(String.format("Comentario%n%s", Utilidades.formatedExceptionMessage(ex)));

			model.addAttribute(Constantes.MODEL_ATT_ERROR, "Clasifiacion de comentario");
			model.addAttribute(Constantes.MODEL_ATT_DESCRIPCION, "Se ha producido un error al intentar gestionar el comentario."
					+ "Compruebe los registros de error.");

			return Constantes.TEMPLATE_PAGINA_ERROR;
		}

	}

	@RequestMapping(value = "/CambiarPassword", method = RequestMethod.POST)
	public String cambiarPassword(@RequestParam("currentPassword") String contraseñaActual,
			@RequestParam("newPassword") String nuevaContraseña,
			@RequestParam("confirmPassword") String confirmacionContraseña, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, Model model) throws NoSuchAlgorithmException {

		try {
			Utilidades.noCachearRespuestaHTTP(httpResponse);
			HttpSession session = httpRequest.getSession(false);
			if (session != null) {
				String nombre = (String) session.getAttribute("user");
				Usuario usuario = repositorioUsuarios.findByUsuario(nombre);
				if (usuario != null) {
					if (Utilidades.comprobarContraseña(contraseñaActual, usuario.getContraseña())) {
						if (nuevaContraseña.equals(confirmacionContraseña)) {
							String nuevaContraseñaSegura = Utilidades.codificarContraseña(nuevaContraseña);

							usuario.setContraseña(nuevaContraseñaSegura);
							repositorioUsuarios.save(usuario);

							model.addAttribute("cambiarPasswordOk", "La contraseña se cambio correctamente");
						} else {
							model.addAttribute("errorPasswordConfirm", "Error al confirmar nueva contraseña.");
						}
					} else {
						model.addAttribute("errorPasswordActual", "La contraseña actual es erronea");
					}
					String tipoUsuario = usuario.getRoles().contains(repositorioRoles.findByTipo(Constantes.TIPO_ADMINISTRADOR)) ? Constantes.TIPO_ADMINISTRADOR : Constantes.TIPO_BASICO;

					model.addAttribute(Constantes.MODEL_ATT_NOMBRE, String.format("%s %s", usuario.getNombre(), usuario.getApellidos()));
					model.addAttribute(Constantes.MODEL_ATT_NOMBRE_USUARIO, usuario.getUsuario());
					model.addAttribute(Constantes.MODEL_ATT_CORREO, usuario.getCorreo());
					model.addAttribute(Constantes.MODEL_ATT_FECHA_REGISTRO, usuario.getFechaCreacion());
					model.addAttribute(Constantes.MODEL_ATT_TIPO_USUARIO, tipoUsuario);


					return Constantes.TEMPLATE_PERFIL;
				}
				
			}

			return "redirect:/login";
		} catch (Exception ex) {
			logger.error(String.format("CambiarPassword%n%s", Utilidades.formatedExceptionMessage(ex)));

			model.addAttribute(Constantes.MODEL_ATT_ERROR, "Cambiar Contraseña");
			model.addAttribute(Constantes.MODEL_ATT_DESCRIPCION, "Se ha producido un error al cambiar la contraseña."
					+ "Pongase en contacto con el administrasdor.");

			return Constantes.TEMPLATE_PAGINA_ERROR;
		}

	}

	@RequestMapping(value = "/CambiarCorreo", method = RequestMethod.POST)
	public String cambiarCorreo(@RequestParam("email") String nuevoCorreo,
			@RequestParam("currentPassword") String contraseña, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, Model model) throws NoSuchAlgorithmException {

		try {
			Utilidades.noCachearRespuestaHTTP(httpResponse);
			HttpSession session = httpRequest.getSession(false);
			if (session != null) {
				String nombre = (String) session.getAttribute("user");
				Usuario usuario = repositorioUsuarios.findByUsuario(nombre);
				if (usuario != null) {
					if (!Utilidades.comprobarContraseña(contraseña, usuario.getContraseña())) {
						model.addAttribute("errorPasswordCorreo", Constantes.ERROR_CONTRASENA);
					} else if (!Utilidades.validarFormatoCorreo(nuevoCorreo)) {
						model.addAttribute("errorNuevoCorreo", Constantes.ERROR_EMAIL_ADDRESS);
					} else {
						
						Email email = new Email();
						StringBuilder cuerpoMensaje = email.crearMensajeConfirmacionCambioCorreo(usuario, nuevoCorreo);
						if(email.enviarCorreo(nuevoCorreo, "Confirmacion de cambio de direccion de correo", cuerpoMensaje.toString())) {
							model.addAttribute("cambiarCorreoOk", Constantes.CHANGE_EMAIL_ADDRESS_OK);
						}
						else {
							model.addAttribute("errorNuevoCorreo", Constantes.ERROR_SEND_CONFIRM_CHANGE_EAMIL);
						}

					}
					String tipoUsuario = usuario.getRoles().contains(repositorioRoles.findByTipo(Constantes.TIPO_ADMINISTRADOR)) ? Constantes.TIPO_ADMINISTRADOR : Constantes.TIPO_BASICO;

					model.addAttribute(Constantes.MODEL_ATT_NOMBRE, String.format("%s %s", usuario.getNombre(), usuario.getApellidos()));
					model.addAttribute(Constantes.MODEL_ATT_NOMBRE_USUARIO, usuario.getUsuario());
					model.addAttribute(Constantes.MODEL_ATT_CORREO, usuario.getCorreo());
					model.addAttribute(Constantes.MODEL_ATT_FECHA_REGISTRO, usuario.getFechaCreacion());
					model.addAttribute(Constantes.MODEL_ATT_TIPO_USUARIO, tipoUsuario);
					
					return Constantes.TEMPLATE_PERFIL;
				}
					
			}

			return "redirect:/login";
		} catch (Exception ex) {
			logger.error(String.format("CambiarCorreo%n%s", Utilidades.formatedExceptionMessage(ex)));

			model.addAttribute(Constantes.MODEL_ATT_ERROR, "Cambiar direccion de correo electronico");
			model.addAttribute(Constantes.MODEL_ATT_DESCRIPCION, "Se ha producido un error al cambiar la direccion de correo."
					+ "Vuelva a intentarlo mas tarde o pongase en contacto con el administrasdor.");

			return Constantes.TEMPLATE_PAGINA_ERROR;
		}

	}
	
	@RequestMapping(value = "/ConfirmarCambioCorreo/{idUsuario}/{nuevoCorreo}/{fecha}", method = RequestMethod.GET)
	public String confirmarCambioCorreo(@PathVariable("idUsuario") Long idUsuario, @PathVariable("nuevoCorreo") String nuevoCorreo,
										@PathVariable("fecha") String fechaCambio,HttpServletRequest httpRequest, HttpServletResponse httpResponse, 
										Model model) {
		try {
			String fecha = Utilidades.getDateToday();
			String fechaCambioDecodificada = Utilidades.getTextDecoded(fechaCambio);
			
			if(!fecha.equals(fechaCambioDecodificada)) {
				model.addAttribute(Constantes.MODEL_ATT_ERROR, Constantes.ERROR_LINK_EXPIRED);
				model.addAttribute(Constantes.MODEL_ATT_DESCRIPCION, Constantes.ERROR_LINK_EXPIRED_DESC);
				
				return Constantes.TEMPLATE_PAGINA_ERROR;
			}
			
			String nuevoCorreoDecodificado = Utilidades.getTextDecoded(nuevoCorreo);
			Usuario usuario = repositorioUsuarios.findById(idUsuario);
			usuario.setCorreo(nuevoCorreoDecodificado);
			repositorioUsuarios.save(usuario);

			
			Utilidades.noCachearRespuestaHTTP(httpResponse);
			HttpSession session = httpRequest.getSession(false);
			if(session != null) {
				String tipoUsuario = usuario.getRoles().contains(repositorioRoles.findByTipo(Constantes.TIPO_ADMINISTRADOR)) ? Constantes.TIPO_ADMINISTRADOR : Constantes.TIPO_BASICO;
				model.addAttribute("cambiarCorreoOk", Constantes.CHANGE_EMAIL_ADDRESS_CONFIRM_OK);
				model.addAttribute(Constantes.MODEL_ATT_NOMBRE, String.format("%s %s", usuario.getNombre(), usuario.getApellidos()));
				model.addAttribute(Constantes.MODEL_ATT_NOMBRE_USUARIO, usuario.getUsuario());
				model.addAttribute(Constantes.MODEL_ATT_CORREO, usuario.getCorreo());
				model.addAttribute(Constantes.MODEL_ATT_FECHA_REGISTRO, usuario.getFechaCreacion());
				model.addAttribute(Constantes.MODEL_ATT_TIPO_USUARIO, tipoUsuario);

	
				return Constantes.TEMPLATE_PERFIL;
				
			}
			
			model.addAttribute("confirmActivacion", Constantes.CHANGE_EMAIL_ADDRESS_CONFIRM_OK);

			return "login";			
		}
		catch(Exception ex) {
			logger.error(String.format("confirmarCmbioCorreo%n%s", Utilidades.formatedExceptionMessage(ex)));

			model.addAttribute(Constantes.MODEL_ATT_ERROR, "Confirmar cambio de direccion de correo electronico");
			model.addAttribute(Constantes.MODEL_ATT_DESCRIPCION, "Se ha producido un error al cambiar la direccion de correo."
					+ "Vuelva a intentarlo mas tarde o pongase en contacto con el administrasdor.");

			return Constantes.TEMPLATE_PAGINA_ERROR;
		}
	}

}
