package App.Model.TvMaze;

import java.util.List;

public class SerieApi {
	
	private int id;
	private String name;
	private String language;
	private List<String> genres;
	private String status;
	private int runtime;
	private String premiered;
	private String officialSite;
	private Rating rating;
	private Network network;
	private WebChannel webChannel;
	private Image image;
	private String summary;
	
	public SerieApi() {
		super();
	}

	public SerieApi(int id, String name, String language, List<String> genres, String status, int runtime,
			String premiered, String officialSite, Rating rating, Network network, WebChannel webChannel, Image image,
			String summary) {
		super();
		this.id = id;
		this.name = name;
		this.language = language;
		this.genres = genres;
		this.status = status;
		this.runtime = runtime;
		this.premiered = premiered;
		this.officialSite = officialSite;
		this.rating = rating;
		this.network = network;
		this.webChannel = webChannel;
		this.image = image;
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<String> getGenres() {
		return genres;
	}

	public void setGenres(List<String> genres) {
		this.genres = genres;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getRuntime() {
		return runtime;
	}

	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}

	public String getPremiered() {
		return premiered;
	}

	public void setPremiered(String premiered) {
		this.premiered = premiered;
	}

	public String getOfficialSite() {
		return officialSite;
	}

	public void setOfficialSite(String officialSite) {
		this.officialSite = officialSite;
	}

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public WebChannel getWebChannel() {
		return webChannel;
	}

	public void setWebChannel(WebChannel webChannel) {
		this.webChannel = webChannel;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	

}
