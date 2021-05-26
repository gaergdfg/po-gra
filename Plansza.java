import java.util.Random;


public class Plansza {
	private Pole[][] pole;
	private int rozmiar;
	private int liczbaGraczy;
	private int[][] pozycjaGracza;
	private int pozostaleSkarby;
	private int[] zebraneSkarbyGracza;
	private int[][] liczbaKluczyGracza;

	private Random rand = new Random();


	Plansza(
		int wymiarPlanszy,
		int liczbaDrzwi,
		int liczbaGraczy,
		int liczbaSkarbow,
		int liczbaScian
	) {
		this.pole = new Pole[wymiarPlanszy][wymiarPlanszy];
		this.rozmiar = wymiarPlanszy;
		this.liczbaGraczy = liczbaGraczy;
		this.pozycjaGracza = new int[liczbaGraczy][2];
		this.pozostaleSkarby = liczbaSkarbow;
		this.zebraneSkarbyGracza = new int[liczbaGraczy];
		this.liczbaKluczyGracza = new int[liczbaGraczy][4];

		// inicjalizowanie pustej planszy
		for (int y = 0; y < rozmiar; y++) {
			for (int x = 0; x < rozmiar; x++) {
				pole[y][x] = new Puste(Kolor.dajLosowy());
			}
		}

		// dodanie drzwi
		for (int i = 0; i < liczbaDrzwi; i++) {
			int x, y;
			do {
				x = rand.nextInt(rozmiar);
				y = rand.nextInt(rozmiar);
			} while (pole[y][x].toString() != "Puste");

			int wyjscie_x, wyjscie_y;
			do {
				wyjscie_x = rand.nextInt(rozmiar);
				wyjscie_y = rand.nextInt(rozmiar);
			} while (pole[y][x].toString() != "Sciana" && x != wyjscie_x && y != wyjscie_y);

			int typ = rand.nextInt(4);

			pole[y][x] = new Drzwi(pole[y][x].kolor, wyjscie_x, wyjscie_y, typ);

			// dodanie odpowiedniego klucza
			do {
				x = rand.nextInt(rozmiar);
				y = rand.nextInt(rozmiar);
			} while (pole[y][x].toString() != "Puste");

			pole[y][x] = new Skarb(pole[y][x].kolor, typ);
		}

		// dodanie skarbow
		for (int i = 0; i < liczbaSkarbow; i++) {
			int x, y;

			do {
				x = rand.nextInt(rozmiar);
				y = rand.nextInt(rozmiar);
			} while (pole[y][x].toString() != "Puste");

			pole[y][x] = new Skarb(Kolor.dajLosowy());
		}

		// dodanie scian
		for (int i = 0; i < liczbaScian; i++) {
			int x, y;

			do {
				x = rand.nextInt(rozmiar);
				y = rand.nextInt(rozmiar);
			} while (pole[y][x].toString() != "Puste");

			pole[y][x] = new Sciana(Kolor.dajLosowy());
		}

		// dodanie pionkow graczy
		for (int i = 0; i < liczbaGraczy; i++) {
			int x, y;

			do {
				x = rand.nextInt(rozmiar);
				y = rand.nextInt(rozmiar);
			} while (pole[y][x].toString() != "Puste");

			pole[y][x] = new Gracz(Kolor.dajLosowy(), i + 1);
			pozycjaGracza[i][0] = y;
			pozycjaGracza[i][1] = x;
		}
	}

	public int dajRozmiar() {
		return rozmiar;
	}


	public int dajLiczbeGraczy() {
		return liczbaGraczy;
	}


	public boolean czyRozgrywkaSkonczona() {
		return pozostaleSkarby > 0;
	}


	public int dajWartoscSkarbowGracza(int gracz) {
		return zebraneSkarbyGracza[gracz - 1];
	}


	public int[] dajPozycjeGracza(int gracz) {
		return pozycjaGracza[gracz - 1];
	}


	// funkcja zwracajaca kod odpowiadajacy kolorowi
	// czerwony: 0, magenta: 1, niebieski: 2, zielony: 3
	private KolorToInt(Kolor kolor) {
		return 1;
	}


	public boolean czyPoleDostepne(int gracz, int x, int y) {
		if (x < 0 || y < 0 || x >= rozmiar || y >= rozmiar) {
			return false;
		}
		if (pole[y][x].toString() == "Sciana") {
			return false;
		}
		if (pole[y][x].toString() == "Drzwi") {
			if (liczbaKluczyGracza[gracz - 1][pole[y][x].typ] <= 0) {
				return false;
			}

			int[] pozycjaWyjscia = pole[y][x].dajPozycjeWyjscia();
			int nowy_y = pozycjaWyjscia[0];
			int nowy_x = pozycjaWyjscia[1];
			if (
				pole[nowy_y][nowy_x].toString() == "Drzwi" &&
				liczbaKluczyGracza[gracz - 1][pole[y][x].typ] <= 0
			) {
				return false;
			}
		}
		return true;
	}


	public int[] znajdzPozycjeWyjscia(int x, int y) {
		do {
			int[] pozycjaWyjscia = pole[y][x].dajPozycjaWyjscia();
			y = pozycjaWyjscia[0];
			x = pozycjaWyjscia[1];
		} while (pole[y][x].toString() == "Drzwi");

		return new int[] {y, x};
	}


	public void ruszGraczaNaPole(int gracz, int x, int y) {
		if (!czyPoleDostepne(gracz, x, y)) {
			return;
		}

		int[] pozycja = dajPozycjeGracza(gracz);
		int gracz_x = pozycja[1];
		int gracz_y = pozycja[0];
		pole[gracz_y][gracz_x] = new Puste(pole[gracz_y][gracz_x].kolor);

		switch (pole[y][x].toString()) {
			case "Puste":
				pole[y][x] = new Gracz(pole[y][x].kolor, gracz);
				break;

			case "Klucz":
				liczbaKluczyGracza[gracz - 1][pole[y][x].typ]++;
				pole[y][x] = new Gracz(pole[y][x].kolor, gracz);
				break;

			case "Skarb":
				zebraneSkarbyGracza[gracz - 1] += pole[y][x].dajWartosc();
				break;

			case "Drzwi":
				if (!pole[y][x].sprawdzCzyOtworzone()) {
					liczbaKluczyGracza[gracz - 1][pole[y][x].typ]--;
					pole[y][x].otworz();
				}

				int[] pozycjaWyjscia = znajdzPozycjeWyjscia(x, y);
				int nowy_y = pozycjaWyjscia[0];
				int nowy_x = pozycjaWyjscia[1];

				ruszGraczaNaPole(gracz, nowy_x, nowy_y);

				break;
		}
	}
}
