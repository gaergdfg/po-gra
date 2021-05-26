public class Drzwi extends Pole {
	private int typ;
	private boolean czyOtworzone;
	private int[] pozycjaWyjscia;


	Dzrwi(Kolor kolor, int x, int y) {
		super(kolor);
		this.czyOtworzone = false;
		pozycjaWyjscia = new int[] {y, x};
	}


	public boolean sprawdzCzyOtworzone() {
		return czyOtworzone;
	}


	public void otworz() {
		czyOtworzone = true;
	}


	public int[] dajPozycjeWyjscia() {
		return pozycjaWyjscia;
	}


	public String toString() {
		return "Drzwi";
	}
}