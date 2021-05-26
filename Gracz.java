public class Gracz extends Pole {
	private int nrGracza;

	
	Gracz(Kolor kolor, int nrGracza) {
		super(kolor);
		this.nrGracza = nrGracza;
	}


	public String toString() {
		return "Gracz";
	}
}