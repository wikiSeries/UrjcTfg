package App.Model.TvMaze;

import java.util.List;

public class SerieApiFull {
	
	private int idSerie;
	private List<Elenco> elenco;
	private List<TemporadaApi> temporadas;
	private List<EpisodioApi> episodios;
	
	public SerieApiFull() {
		super();
	}

	public SerieApiFull(int idSerie, List<Elenco> elenco, List<TemporadaApi> temporadas, List<EpisodioApi> episodios) {
		this.idSerie = idSerie;
		this.elenco = elenco;
		this.temporadas = temporadas;
		this.episodios = episodios;
	}

	public int getIdSerie() {
		return idSerie;
	}

	public void setIdSerie(int idSerie) {
		this.idSerie = idSerie;
	}

	public List<Elenco> getElenco() {
		return elenco;
	}

	public void setElenco(List<Elenco> elenco) {
		this.elenco = elenco;
	}

	public List<TemporadaApi> getTemporadas() {
		return temporadas;
	}

	public void setTemporadas(List<TemporadaApi> temporadas) {
		this.temporadas = temporadas;
	}

	public List<EpisodioApi> getEpisodios() {
		return episodios;
	}

	public void setEpisodios(List<EpisodioApi> episodios) {
		this.episodios = episodios;
	}
	
	
	
	

}
