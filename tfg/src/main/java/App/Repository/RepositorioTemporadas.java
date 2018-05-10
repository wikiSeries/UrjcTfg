package App.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import App.Model.Serie;
import App.Model.Temporada;

public interface RepositorioTemporadas extends JpaRepository<Temporada, Long> {

	public Temporada findByIdApi(int idApi);
	public Temporada findBySerieAndNumero(Serie serie, int numero);
	
}
