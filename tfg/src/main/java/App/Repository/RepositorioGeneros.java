package App.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import App.Model.Genero;
public interface RepositorioGeneros extends JpaRepository<Genero, Long> {
	public Genero findByTitulo(String titulo);
}
