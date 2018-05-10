package App.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import App.Model.Usuario;

public interface RepositorioUsuario extends JpaRepository<Usuario, Long> {
	public Usuario findByUsuario(String usuario);
	
	public Usuario findById(Long id);
}
