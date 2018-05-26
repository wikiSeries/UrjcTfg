package App;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import App.Auxiliar.Constantes;
import App.Auxiliar.Utilidades;
import App.Model.Genero;
import App.Model.Rol;
import App.Model.Serie;
import App.Model.Usuario;
import App.Repository.RepositorioGeneros;
import App.Repository.RepositorioRoles;
import App.Repository.RepositorioSeries;
import App.Repository.RepositorioUsuario;

@Component
public class DatabaseLoader {
	
	/*@Autowired
	private RepositorioRoles repositorioRoles;
	
	@Autowired
	private RepositorioUsuario repositorioUsuario;
	
	@Autowired
	private RepositorioGeneros repositorioGeneros;
	
	@Autowired
	private RepositorioSeries repositorioSeries;
	
	
	@PostConstruct
	private void initDatabase() throws NoSuchAlgorithmException {
		Rol rolAdministrador = new Rol(Constantes.TIPO_ADMINISTRADOR);
		Rol rolBasico = new Rol(Constantes.TIPO_BASICO);
		String securePassword = Utilidades.codificarContraseña("admin123");
		
		Usuario u1 = new Usuario("admin", securePassword, "Aaron", "Velasco Lopez", 
								"wikiseriestfg@gmail.com", "633583260");
		
		securePassword = Utilidades.codificarContraseña("arn94123");
		Usuario u2 = new Usuario("arn94", securePassword, "Aaron", "Velasco Lopez", 
								"tiquismiquisss@gmail.com", "633583260");
		
		List<Rol> rolesU1 = new ArrayList<Rol>();
		rolesU1.add(rolBasico);
		rolesU1.add(rolAdministrador);
		
		List<Rol> rolesU2 = new ArrayList<Rol>();
		rolesU2.add(rolBasico);
		
		List<Usuario> usuariosRA = new ArrayList<Usuario>();
		usuariosRA.add(u1);
		
		List<Usuario> usuariosRB = new ArrayList<Usuario>();
		usuariosRB.add(u1);
		usuariosRB.add(u2);
		
		rolAdministrador.setUsuarios(usuariosRA);
		u1.setRoles(rolesU1);
		
		rolBasico.setUsuarios(usuariosRB);
		u2.setRoles(rolesU2);
		
		
		/*Serie s1 = new Serie(8557, "Frontier", "Descripcion Serie", "2016-02-19", 6.9, "https://static.tvmaze.com/uploads/images/medium_portrait/81/204817.jpg");
		Serie s2 = new Serie(73, "The Walking Dead", "Descripcion Serie", "2011-05-09", 6.2, "https://static.tvmaze.com/uploads/images/medium_portrait/136/340444.jpg");
		Serie s3 = new Serie(2993, "Stranger Things", "Descripcion Serie", "2013-09-20", 8.3, "https://static.tvmaze.com/uploads/images/medium_portrait/132/330543.jpg");
		Serie s4 = new Serie(13634, "Godless", "Descripcion Serie", "2017-01-13", 8.0, "https://static.tvmaze.com/uploads/images/medium_portrait/131/329494.jpg");
		Serie s5 = new Serie(82, "Game of thrones", "Descripcion Serie", "2011-07-05", 9.1, "https://static.tvmaze.com/uploads/images/medium_portrait/124/310209.jpg");

		Genero g1 = new Genero("Drama");
		Genero g2 = new Genero("Action");
		Genero g3 = new Genero("Adventure");
		Genero g4 = new Genero("Horror");
		Genero g5 = new Genero("Fantasy");
		Genero g6 = new Genero("Science-Fiction");
		Genero g7 = new Genero("Western");



		List <Genero> generosS1 = new ArrayList <Genero>();
		generosS1.add(g1);
		generosS1.add(g2);
		generosS1.add(g3);
		s1.setGeneros(generosS1);
		
		List <Genero> generosS2 = new ArrayList <Genero>();
		generosS2.add(g1);
		generosS2.add(g2);
		generosS2.add(g4);
		s2.setGeneros(generosS2);
		
		List <Genero> generosS3 = new ArrayList <Genero>();
		generosS3.add(g1);
		generosS3.add(g5);
		generosS3.add(g6);
		s3.setGeneros(generosS3);
		
		List <Genero> generosS4 = new ArrayList <Genero>();
		generosS4.add(g1);
		generosS4.add(g7);
		s4.setGeneros(generosS4);
		
		List <Genero> generosS5 = new ArrayList <Genero>();
		generosS5.add(g1);
		generosS5.add(g3);
		generosS5.add(g5);
		s5.setGeneros(generosS5);
		
		List<Serie> seriesG1 = new ArrayList<Serie>();
		seriesG1.add(s1);
		seriesG1.add(s2);
		g1.setSeries(seriesG1);
		
		List<Serie> seriesG2 = new ArrayList<Serie>();
		seriesG2.add(s2);
		g2.setSeries(seriesG2);
		
		List<Serie> seriesG3 = new ArrayList<Serie>();
		seriesG3.add(s3);
		g3.setSeries(seriesG3);
		
		List<Serie> seriesG4 = new ArrayList<Serie>();
		seriesG4.add(s4);
		seriesG4.add(s5);
		g4.setSeries(seriesG4);
		
		List<Serie> seriesG5 = new ArrayList<Serie>();
		seriesG5.add(s5);
		g5.setSeries(seriesG5);
		
		u1.setIntentos(Constantes.NUMEROINTENTOSLOGIN);
		u1.setBloqueado(false);
		//u1.getSeries().add(s1);
		
		u2.setIntentos(Constantes.NUMEROINTENTOSLOGIN);
		u2.setBloqueado(false);
		
		
		/*repositorioSeries.save(s1);
		repositorioSeries.save(s2);
		repositorioSeries.save(s3);
		repositorioSeries.save(s4);
		repositorioSeries.save(s5);

		repositorioGeneros.save(g1);
		repositorioGeneros.save(g2);
		repositorioGeneros.save(g3);
		repositorioGeneros.save(g4);
		repositorioGeneros.save(g5);

		repositorioUsuario.save(u1);
		repositorioUsuario.save(u2);
		
		repositorioRoles.save(rolBasico);
		repositorioRoles.save(rolAdministrador);
	}
	*/
}


