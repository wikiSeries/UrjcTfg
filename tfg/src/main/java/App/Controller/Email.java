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

	public Email() {
		
	}
	
	public boolean enviarCorreo(String destinatario, String asunto, String cuerpo) {
		
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.user", Constantes.USUARIO_CORREO_APP);
		properties.put("mail.smtp.clave", Constantes.CONTRASEÑA_CORREO_APP);
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
			transport.connect("smtp.gmail.com", Constantes.USUARIO_CORREO_APP, Constantes.CONTRASEÑA_CORREO_APP);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			
			return true;
		}
		catch(MessagingException ex) {
			logger.error(String.format("enviarCorreo\n%s", Utilidades.formatedExceptionMessage(ex)));
			return false;
		}
		
		
	}
	
	public StringBuilder crearMensajeValidacion(Usuario usuario) {
		StringBuilder mensaje = new StringBuilder();

		mensaje.append("<p>Hola").append(usuario.getNombre()).append(" ").append(usuario.getApellidos()).append(",</p>")
				.append("<p>Se ha creado una nueva cuenta en WikiSeries con los siguientes datos:</p>")
				.append("<table border=1>")
					.append("<tr>")
						.append("<td>Nombre</t><td>").append(usuario.getNombre()).append("</td>")					
					 .append("</tr>")	
					 
					 .append("<tr>")
						.append("<td>Apellidos</t><td>").append(usuario.getApellidos()).append("</td>")					
					 .append("</tr>")
					 
					 /**.append("<tr>")
						.append("<td>Telefono</t><td>").append(usuario.getTelefonoMovil()).append("</td>")					
					 .append("</tr>")**/
					 
					 .append("<tr>")
						.append("<td>Nombre usuario</t><td>").append(usuario.getUsuario()).append("</td>")					
					 .append("</tr>")
					 
					 .append("<tr>")
						.append("<td>Contraseña</t><td>").append(usuario.getContraseña()).append("</td>")					
					 .append("</tr>")
				.append("</table>")		
				 			
				.append("<p>Para finalizar el proceso de nuevo registro haz click <a href=\"https://localhost:8443/ActivarCuentaUsuario?usuario=").append(usuario.getUsuario()).append("\">aqui<a> y empieza a disfrutar de WikiSeries</p>")
				.append("<p>Un saludo del equipo de <a href=\"#\">WikiSeries</a>.</p>");
				
		return mensaje;		
	}
	
	public StringBuilder crearMensajeRecuperacion(Usuario usuario) {
		
		StringBuilder mensaje = new StringBuilder();
		
		mensaje.append("<p>Hola").append(usuario.getNombre()).append(" ").append(usuario.getApellidos()).append(",</p>")
				.append("<p>Su nueva contraseña de su cuenta en WikiSeries es: '").append(usuario.getContraseña()).append("',</p>")
				.append("<br/>")
				.append("<p>Un saludo del equipo de <a href=\"#\">WikiSeries</a>.</p>");
		
		return mensaje;	
		
	}
	
	public StringBuilder crearCuerpoComentario(String nombreUsuario, String tituloSerie, String comentario, Long idSerie, Long idUsuario, Long idComentario) {
		StringBuilder cuerpo = new StringBuilder();
			
			
				cuerpo.append("<div>");
					cuerpo.append("<div>");
						cuerpo.append("<div>");
							
							cuerpo.append("<p>Nuevo comentario añadido</p>");
							cuerpo.append("<p>Usuario: ").append(nombreUsuario).append("</p>");
							cuerpo.append("<p>Serie: ").append(tituloSerie).append("</p>");
							cuerpo.append("<p>Comentario: ").append(comentario).append("</p>");
		
						cuerpo.append("</div>");
					cuerpo.append("</div>");
					
					
					cuerpo.append("<div>");
					
						cuerpo.append("<div>");
							cuerpo.append("<a href =\"https://localhost:8443/Comentario/Publicar/")
								.append(idSerie).append("/").append(idUsuario).append("/").append(idComentario)
								.append("\">Publicar</a>");
						cuerpo.append("</div>");
						
						cuerpo.append("<div>");
							cuerpo.append("<a href =\"https://localhost:8443/Comentario/Inapropiado/")
								.append(idSerie).append("/").append(idUsuario).append("/").append(idComentario)
								.append("\">No Publicar por contenido inapropiado</a>");
						cuerpo.append("</div>");
						
						cuerpo.append("<div>");
							cuerpo.append("<a href =\"https://localhost:8443/Comentario/Spolier/")
							.append(idSerie).append("/").append(idUsuario).append("/").append(idComentario)
							.append("\">No Publicar por spolier</a>");
						cuerpo.append("</div>");
						
					cuerpo.append("</div>");
				cuerpo.append("</div>");
		return cuerpo;
	}
	
	public StringBuilder crearCuerpoComentarioPublicado(String nombre, String apellidos, String comentario) {
		StringBuilder mensaje = new StringBuilder();
		
		mensaje.append("<p>Hola").append(nombre).append(" ").append(apellidos).append(",</p>")
				.append("<p>Tu comentario \"").append(comentario).append("\" ha sido publicado correctamente<p>")
				.append("<p>Gracias por usar WikiSeries</p>");
		
		return mensaje;
	}
	
	public StringBuilder crearCuerpoComentarioNoPermitido(String nombre, String apellidos, String comentario) {
		StringBuilder mensaje = new StringBuilder();
		
		mensaje.append("<p>Hola").append(nombre).append(" ").append(apellidos).append(",</p>")
		.append("<p>Tu comentario \"").append(comentario).append("\" no ha sido publicado por tener contenido inapropiado.<p>")
		.append("<p>Por favor, evite añadir este tipo de contenido nuevamente.</p>")
		.append("<p>Gracias.</p>");
		
		return mensaje;
	}
	
	public StringBuilder crearCuerpoComentarioSpoiler(String nombre, String apellidos, String comentario) {
		StringBuilder mensaje = new StringBuilder();
		
		mensaje.append("<p>Hola ").append(nombre).append(" ").append(apellidos).append(",</p>")
		.append("<p>Tu comentario \"").append(comentario).append("\" no ha sido publicado por contener spoilers.<p>")
		.append("<p>Por favor, evite añadir este tipo de contenido nuevamente.</p>")
		.append("<p>Gracias.</p>");
		
		return mensaje;
	}
	
	public StringBuilder crearMensajeConfirmacionCambioCorreo(Usuario usuario, String nuevoCorreo) {
		String nuevoCorreoCodificado = Utilidades.getTextEncoded(nuevoCorreo);
		String fechaCodificada = Utilidades.getTextEncoded(Utilidades.getDateToday());
		
		StringBuilder mensaje = new StringBuilder();
		mensaje.append("<p>Hola ").append(usuario.getNombre()).append(" ").append(usuario.getApellidos()).append(",</p>")
		.append("<p>Hemos recibido tu solicitud de cambio de direccion de correo. Por favor haz click ")
		.append("<a href = \"https://localhost:8443/ConfirmarCambioCorreo/").append(usuario.getId()).append("/").append(nuevoCorreoCodificado)
		.append("/").append(fechaCodificada).append("\">aqui</a>")
		.append(" para confirmar su nueva direccion de correo.</p>")
		.append("<p>Gracias, un saludo del equipo de  <a href = \"https://localhost:8843\"> WikiSeries</a>");
	
		
		return mensaje;
	}

}
