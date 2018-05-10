package App.Model.TvMaze;

public class Image {
	private String medium;
	private String original;
	
	public Image() {
		super();
	}

	public Image(String medium, String original) {
		this.medium = medium;
		this.original = original;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}
	
	
	
	
}
