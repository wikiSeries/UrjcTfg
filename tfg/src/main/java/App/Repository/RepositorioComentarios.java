package App.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import App.Model.Comentario;

public interface RepositorioComentarios extends JpaRepository<Comentario, Long> {
	
	public Comentario findById(Long id);
}
