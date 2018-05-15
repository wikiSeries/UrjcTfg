package App.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import App.Model.Personaje;

public interface RepositorioPersonajes extends JpaRepository <Personaje, Long>{
	
	Personaje findByIdApi(int idApi);
	
}
