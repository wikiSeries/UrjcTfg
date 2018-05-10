package App.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import App.Model.Episodio;

public interface RepositorioEpisodios extends JpaRepository<Episodio, Long> {

		public Episodio findByIdApi(int idApi);
		public Episodio findById(Long id);
}
