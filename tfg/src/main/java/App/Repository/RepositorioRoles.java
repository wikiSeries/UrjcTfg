package App.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import App.Model.Rol;

public interface RepositorioRoles extends JpaRepository<Rol, Long> {
	public Rol findByTipo(String tipo);
	
}
