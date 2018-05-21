package App.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import App.Model.Actor;

public interface RepositorioActores extends JpaRepository <Actor, Long> {

	public Actor findByIdApi(int idApi);
}
