package App.Model.TvMaze;

public class TemporadaApi {
	
	private int id;
	private int number;
	private String premiereDate;
	private String endDate;
	private Network network;
	private WebChannel webChannel;
	
	public TemporadaApi() {
		super();
	}

	public TemporadaApi(int id, int number, String premiereDate, String endDate, Network network,
			WebChannel webChannel) {
		this.id = id;
		this.number = number;
		this.premiereDate = premiereDate;
		this.endDate = endDate;
		this.network = network;
		this.webChannel = webChannel;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getPremiereDate() {
		return premiereDate;
	}

	public void setPremiereDate(String premiereDate) {
		this.premiereDate = premiereDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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
	
	
	
}
