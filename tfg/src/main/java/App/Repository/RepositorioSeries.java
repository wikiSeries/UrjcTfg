package App.Repository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import App.Model.Genero;
import App.Model.Serie;

public interface RepositorioSeries extends JpaRepository<Serie, Long> {

	public Serie findByIdApi(int idApi);
	public Serie findById(Long id);
	
	@Query( nativeQuery = true, value = "SELECT * FROM serie s WHERE s.id IN (SELECT series_id FROM genero_series gs where gs.generos_id = (SELECT id FROM genero g WHERE g.titulo = :tituloGenero))")
	public List<Serie> getSeries(@Param("tituloGenero") String tituloGenero);
	
	public Page<Serie> findByTituloContaining(String titulo, Pageable page);
	
	@Query(nativeQuery = true, value = "SELECT DISTINCT substring_index(fecha_estreno, '-', 1) fecha_Estreno FROM serie")
	public List<String> getAnos();
	
	public List<Serie> findByIdIn(List<Long> listaIds);
	public List<Serie> findDistinctByGenerosIn(List<Genero> generos, Pageable page);
}
