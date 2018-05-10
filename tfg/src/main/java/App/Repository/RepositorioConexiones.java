package App.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import App.Model.Conexion;

public interface RepositorioConexiones extends JpaRepository<Conexion, Long> {

}
