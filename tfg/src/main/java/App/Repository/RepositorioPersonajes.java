package App.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import App.Model.Personaje;

public interface RepositorioPersonajes extends JpaRepository <Personaje, Long>{
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaje p where p.actor_id in (SELECT actores_id FROM actor_series actser where actser.series_id = :idSerie)")
	List<Personaje> findPersonajes(@Param("idSerie") Long idSerie);
	
	Personaje findByIdApi(int idApi);
	
}
