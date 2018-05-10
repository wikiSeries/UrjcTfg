package App.Model.TvMaze;

public class EpisodioApi {
	
	private int id;
	private String name;
	private int season;
	private int number;
	private String airdate;
	private int runtime;
	private String summary;
	
	public EpisodioApi() {
		super();
	}

	public EpisodioApi(int id, String name, int season, int number, String airdate, int runtime, String summary) {
		super();
		this.id = id;
		this.name = name;
		this.season = season;
		this.number = number;
		this.airdate = airdate;
		this.runtime = runtime;
		this.summary = summary;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSeason() {
		return season;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getAirdate() {
		return airdate;
	}

	public void setAirdate(String airdate) {
		this.airdate = airdate;
	}

	public int getRuntime() {
		return runtime;
	}

	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	
}
