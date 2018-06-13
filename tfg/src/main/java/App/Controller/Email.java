package App.Controller;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import App.Auxiliar.Constantes;
import App.Auxiliar.Utilidades;
import App.Model.Usuario;

public class Email {
	
	private Logger logger = Logger.getLogger("file");
	
	public static final String YOUR_COMENT = "<p>Tu comentario\"";
	public static final String HI = "<p>Hola";
	
	public static final String DIV = "<div>";
	public static final String CLOSE_TR = "</tr>";
	public static final String CLOSE_TD = "</td>";
	public static final String CLOSE_DIV = "</div>";
	public static final String CLOSE_P = "</p>";

	public Email() {
		super();
	}
	
	public boolean enviarCorreo(String destinatario, String asunto, String cuerpo) {
		
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.user", Constantes.USUARIO_CORREO_APP);
		properties.put("mail.smtp.clave", Constantes.CONTRASENA_CORREO_APP);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.port", "587");

		Session session = Session.getDefaultInstance(properties);
		MimeMessage message = new MimeMessage(session);
		
		try {
			message.setFrom(new InternetAddress(Constantes.USUARIO_CORREO_APP));
			message.addRecipients(Message.RecipientType.TO, destinatario);
			message.setSubject(asunto);
			message.setContent(cuerpo, "text/html; charset=utf-8");
			
			Transport transport = session.getTransport("smtp");
			transport.connect("smtp.gmail.com", Constantes.USUARIO_CORREO_APP, Constantes.CONTRASENA_CORREO_APP);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			
			return true;
		}
		catch(MessagingException ex) {
			logger.error(String.format("enviarCorreo%n%s", Utilidades.formatedExceptionMessage(ex)));
			return false;
		}
		
		
	}
	
	public StringBuilder crearMensajeValidacion(Usuario usuario) {
		StringBuilder mensaje = new StringBuilder();

		mensaje.append(HI).append(usuario.getNombre()).append(" ").append(usuario.getApellidos()).append("," + CLOSE_P)
				.append("<p>Se ha creado una nueva cuenta en WikiSeries con los siguientes datos:</p>")
				.append("<table border=1>")
					.append("<tr>")
						.append("<td>Nombre</t><td>").append(usuario.getNombre()).append(CLOSE_TD)					
					 .append(CLOSE_TR)	
					 
					 .append("<tr>")
						.append("<td>Apellidos</t><td>").append(usuario.getApellidos()).append(CLOSE_TD)					
					 .append(CLOSE_TR)
					 
					 /**.append("<tr>")
						.append("<td>Telefono</t><td>").append(usuario.getTelefonoMovil()).append(CLOSE_TD)					
					 .append(CLOSE_TR)**/
					 
					 .append("<tr>")
						.append("<td>Nombre usuario</t><td>").append(usuario.getUsuario()).append(CLOSE_TD)					
					 .append(CLOSE_TR)
					 
					 .append("<tr>")
						.append("<td>Contraseña</t><td>").append(usuario.getContraseña()).append(CLOSE_TD)					
					 .append(CLOSE_TR)
				.append("</table>")		
				 			
				.append("<p>Para finalizar el proceso de nuevo registro haz click <a href=\"https://localhost:8443/ActivarCuentaUsuario?usuario=").append(usuario.getUsuario()).append("\">aqui<a> y empieza a disfrutar de WikiSeries</p>")
				.append("<p>Un saludo del equipo de <a href=\"#\">WikiSeries</a>.</p>");
				
		return mensaje;		
	}
	
	public StringBuilder crearMensajeRecuperacion(Usuario usuario) {
		
		StringBuilder mensaje = new StringBuilder();
		
		mensaje.append(HI).append(usuario.getNombre()).append(" ").append(usuario.getApellidos()).append("," + CLOSE_P)
				.append("<p>Su nueva contraseña de su cuenta en WikiSeries es: '").append(usuario.getContraseña()).append("',</p>")
				.append("<br/>")
				.append("<p>Un saludo del equipo de <a href=\"#\">WikiSeries</a>.</p>");
		
		return mensaje;	
		
	}
	
	public StringBuilder crearCuerpoComentario(String nombreUsuario, String tituloSerie, String comentario, Long idSerie, Long idUsuario, Long idComentario) {
		StringBuilder cuerpo = new StringBuilder();
			
			
				cuerpo.append(DIV);
					cuerpo.append(DIV);
						cuerpo.append(DIV);
							
							cuerpo.append("<p>Nuevo comentario añadido</p>");
							cuerpo.append("<p>Usuario: ").append(nombreUsuario).append("</p>");
							cuerpo.append("<p>Serie: ").append(tituloSerie).append("</p>");
							cuerpo.append("<p>Comentario: ").append(comentario).append("</p>");
		
						cuerpo.append(CLOSE_DIV);
					cuerpo.append(CLOSE_DIV);
					
					
					cuerpo.append(DIV);
					
						cuerpo.append(DIV);
							cuerpo.append("<a href =\"https://localhost:8443/Comentario/Publicar/")
								.append(idSerie).append("/").append(idUsuario).append("/").append(idComentario)
								.append("\">Publicar</a>");
						cuerpo.append(CLOSE_DIV);
						
						cuerpo.append(DIV);
							cuerpo.append("<a href =\"https://localhost:8443/Comentario/Inapropiado/")
								.append(idSerie).append("/").append(idUsuario).append("/").append(idComentario)
								.append("\">No Publicar por contenido inapropiado</a>");
						cuerpo.append(CLOSE_DIV);
						
						cuerpo.append(DIV);
							cuerpo.append("<a href =\"https://localhost:8443/Comentario/Spolier/")
							.append(idSerie).append("/").append(idUsuario).append("/").append(idComentario)
							.append("\">No Publicar por spolier</a>");
						cuerpo.append(CLOSE_DIV);
						
					cuerpo.append(CLOSE_DIV);
				cuerpo.append(CLOSE_DIV);
		return cuerpo;
	}
	
	public StringBuilder crearCuerpoComentarioPublicado(String nombre, String apellidos, String comentario) {
		StringBuilder mensaje = new StringBuilder();
		
		mensaje.append(HI).append(nombre).append(" ").append(apellidos).append("," + CLOSE_P)
				.append(YOUR_COMENT).append(comentario).append("\" ha sido publicado correctamente<p>")
				.append("<p>Gracias por usar WikiSeries</p>");
		
		return mensaje;
	}
	
	public StringBuilder crearCuerpoComentarioNoPermitido(String nombre, String apellidos, String comentario) {
		StringBuilder mensaje = new StringBuilder();
		
		mensaje.append(HI).append(nombre).append(" ").append(apellidos).append("," + CLOSE_P)
		.append(YOUR_COMENT).append(comentario).append("\" no ha sido publicado por tener contenido inapropiado.<p>")
		.append("<p>Por favor, evite añadir este tipo de contenido nuevamente.</p>")
		.append("<p>Gracias.</p>");
		
		return mensaje;
	}
	
	public StringBuilder crearCuerpoComentarioSpoiler(String nombre, String apellidos, String comentario) {
		StringBuilder mensaje = new StringBuilder();
		
		mensaje.append("<p>Hola ").append(nombre).append(" ").append(apellidos).append("," + CLOSE_P)
		.append(YOUR_COMENT).append(comentario).append("\" no ha sido publicado por contener spoilers.<p>")
		.append("<p>Por favor, evite añadir este tipo de contenido nuevamente.</p>")
		.append("<p>Gracias.</p>");
		
		return mensaje;
	}
	
	public StringBuilder crearMensajeConfirmacionCambioCorreo(Usuario usuario, String nuevoCorreo) {
		String nuevoCorreoCodificado = Utilidades.getTextEncoded(nuevoCorreo);
		String fechaCodificada = Utilidades.getTextEncoded(Utilidades.getDateToday());
		
		StringBuilder mensaje = new StringBuilder();
		mensaje.append("<p>Hola ").append(usuario.getNombre()).append(" ").append(usuario.getApellidos()).append("," + CLOSE_P)
		.append("<p>Hemos recibido tu solicitud de cambio de direccion de correo. Por favor haz click ")
		.append("<a href = \"https://localhost:8443/ConfirmarCambioCorreo/").append(usuario.getId()).append("/").append(nuevoCorreoCodificado)
		.append("/").append(fechaCodificada).append("\">aqui</a>")
		.append(" para confirmar su nueva direccion de correo.</p>")
		.append("<p>Gracias, un saludo del equipo de  <a href = \"https://localhost:8843\"> WikiSeries</a>");
	
		
		return mensaje;
	}
	
	public StringBuilder crearMensajeContacto(Usuario usuario, String mensaje) {
		StringBuilder cuerpoMensaje = new StringBuilder();
		cuerpoMensaje.append(DIV)
				.append(String.format("<p><b>%s : </b><span>%s %s</span></p>", "Nombre", usuario.getNombre(), usuario.getApellidos()))
				.append(String.format("<p><b>%s : </b><span>%s</span></p>", "Usuario", usuario.getUsuario()))
				.append(String.format("<p><b>%s : </b><span>%s</span></p>", "Direccion de correo", usuario.getCorreo()))
				.append("<br />")
				.append(String.format("<p>%s</p>",  mensaje))
				.append(CLOSE_DIV);
		
		return cuerpoMensaje;
	}

}
