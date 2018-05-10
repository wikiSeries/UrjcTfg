package App.Model.TvMaze;

public class Elenco {
	private Person person;
	private Character character;
	
	public Elenco() {
		super();
	}

	public Elenco(Person person, Character character) {
		this.person = person;
		this.character = character;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		this.character = character;
	}
	
	

}
