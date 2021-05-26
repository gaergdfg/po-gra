public class Klucz extends Pole {
	private int typ;


	Klucz(Kolor kolor, int typ) {
		this.typ = typ;
	}


	public int dajTyp() {
		return typ;
	}
}