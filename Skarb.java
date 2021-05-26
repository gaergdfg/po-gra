public class Skarb extends Pole {
	private int wartosc;


	Skarb(Kolor kolor) {
		super(kolor);
		wartosc = znajdzWartoscSkarbu(kolor);
	}
	
	
	// funkcja zwracajaca wartosc skarbu na podstawie jego koloru
	private int znajdzWartoscSkarbu(Kolor kolor) {
		return 3;
	}


	public int dajWartosc() {
		return wartosc;
	}


	public String toString() {
		return "Skarb";
	}
}