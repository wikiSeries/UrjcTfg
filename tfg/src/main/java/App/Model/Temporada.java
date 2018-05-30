package App.Model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Temporada {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private int idApi;
	private int numero;
	private String estreno;
	private String finEstreno;
	private String canalTv;
	private String canalWeb;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name= "SERIE_ID")
	private Serie serie;
	
	@JsonIgnore
	@OneToMany(mappedBy = "temporada")
	private List<Episodio> episodios;
	
	public Temporada() {
		
	}
	
	public Temporada(int idApi, int numero, String estreno, String finEstreno, String canalTv, String canalWeb) {
		this.idApi = idApi;
		this.numero = numero;
		this.estreno = estreno;
		this.finEstreno = finEstreno;
		this.canalTv = canalTv;
		this.canalWeb = canalWeb;
		
		this.serie = new Serie();
		this.episodios = new ArrayList<Episodio>();
	}
	
	public Temporada (App.Model.TvMaze.TemporadaApi temporadaApi) {
		this.idApi = temporadaApi.getId();
		this.numero = temporadaApi.getNumber();
		this.estreno = temporadaApi.getPremiereDate();
		this.finEstreno = temporadaApi.getEndDate();
		this.canalTv = temporadaApi.getNetwork() != null ? temporadaApi.getNetwork().getName() : "";
		this.canalWeb = temporadaApi.getWebChannel() != null ? temporadaApi.getWebChannel().getName() : "";
		
		this.serie = new Serie();
		this.episodios = new ArrayList<Episodio> ();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getIdApi() {
		return idApi;
	}

	public void setIdApi(int idApi) {
		this.idApi = idApi;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getEstreno() {
		return estreno;
	}

	public void setEstreno(String estreno) {
		this.estreno = estreno;
	}

	public String getFinEstreno() {
		return finEstreno;
	}

	public void setFinEstreno(String finEstreno) {
		this.finEstreno = finEstreno;
	}

	public String getCanalTv() {
		return canalTv;
	}

	public void setCanalTv(String canalTv) {
		this.canalTv = canalTv;
	}

	public String getCanalWeb() {
		return canalWeb;
	}

	public void setCanalWeb(String canalWeb) {
		this.canalWeb = canalWeb;
	}

	public Serie getSerie() {
		return serie;
	}

	public void setSerie(Serie serie) {
		this.serie = serie;
	}

	public List<Episodio> getEpisodios() {
		return episodios;
	}

	public void setEpisodios(List<Episodio> episodios) {
		this.episodios = episodios;
	}
	
	public void Actualizar(App.Model.TvMaze.TemporadaApi temporadaApi) {
		this.setIdApi(temporadaApi.getId());
		this.setNumero(temporadaApi.getNumber());
		this.setEstreno(temporadaApi.getPremiereDate());
		this.setFinEstreno(temporadaApi.getEndDate());
		String canalTvToSet = temporadaApi.getNetwork() != null ? temporadaApi.getNetwork().getName() : "";
		this.setCanalTv(canalTvToSet);
		String canalWebToSet = temporadaApi.getWebChannel() != null ? temporadaApi.getWebChannel().getName() : "";
		this.setCanalWeb(canalWebToSet);
		
	}
}
