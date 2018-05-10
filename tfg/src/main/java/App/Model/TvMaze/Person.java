package App.Model.TvMaze;

public class Person {
	
	private int id;
	private String name;
	private Country country;
	private String birthday;
	private String deathday;
	private String gender;
	private Image image;
	
	public Person() {
		super();
	}

	public Person(int id, String name, Country country, String birthday, String deathday, String gender, Image image) {
		super();
		this.id = id;
		this.name = name;
		this.country = country;
		this.birthday = birthday;
		this.deathday = deathday;
		this.gender = gender;
		this.image = image;
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

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getDeathday() {
		return deathday;
	}

	public void setDeathday(String deathday) {
		this.deathday = deathday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	
}
