package App.Model.Post;

public class ComentarioPost {
	
	private String comentario;
	private String nombreUsuario;
	private Long idSerie;
	
	public ComentarioPost() {
		super();
	}

	public ComentarioPost(String comentario, String nombreUsuario, Long idSerie) {
		this.comentario = comentario;
		this.nombreUsuario = nombreUsuario;
		this.idSerie = idSerie;
		
	}


	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public Long getIdSerie() {
		return idSerie;
	}

	public void setIdSerie(Long idSerie) {
		this.idSerie = idSerie;
	}

}
