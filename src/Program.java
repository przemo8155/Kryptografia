import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

public class Program {
	/*
	 * Program działa dla każdego wejścia
	 */

	public static void main(String[] args) {
		/*
		 * Deklaracja zmiennych n, e, d - dane początkowe ed - 1 = 2^s * t, gdzie
		 * t-niepatrzyste nwd - zmienna używana w kroku 3 b - zmienna (o ogromnej
		 * pojemności) używana w kroku 5, 6 i 7 wynik, drugi_czynnik - czynniki pierwsze
		 * c, c_i - wyrazy ciągu rekurencyjnego
		 */
		int n = 0, d = 0, e = 0, t = 0, a = 0, nwd = 0;
		int wynik, drugi_czynnik;
		long c = 0, c_i = 0;
		BigInteger b = new BigInteger("1");

		/*
		 * Odczytywanie pliku dane.txt, który w moim przypadku wygląda następująco:
		 * n=67159 d=50131 e=331
		 */
		try {
			BufferedReader br = new BufferedReader(new FileReader("src\\dane.txt"));
			String line = br.readLine();
			while (line != null) {
				String[] tokens = line.split("=");
				for (String wyraz : tokens) {
					if (wyraz.equals("n")) {
						n = Integer.parseInt(tokens[1]);
						c = n - 1;
					} else if (wyraz.equals("e")) {
						e = Integer.parseInt(tokens[1]);
					} else if (wyraz.equals("d")) {
						d = Integer.parseInt(tokens[1]);
					}

				}
				line = br.readLine();
			}
			br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		System.out
				.println("Odczytane wartości początkowe z pliku:\n" + "N: " + n + "   E: " + e + "    D: " + d + "\n");
		/*
		 * Krok pierwszy algorytmu - wyliczanie ed-1, s, t
		 */
		long bi = ((long) e * (long) d) - 1;

		for (int s = 1; s < Math.sqrt(bi); s++) {
			for (int h = 1; h < bi; h = h + 2) {
				if ((Math.pow(2, s) * h) == bi) {
					System.out.println("Przedstawienie ed-1 w postaci 2^s*t, gdzie t - niepatrzyste:");
					System.out.println(bi + " = 2^" + s + " * " + h + "\n\n");
					t = h;
				} else if (Math.pow(2, s) > bi) {
					s = (int) bi;
				}
			}
		}

		/*
		 * Kroki 2-6 algorytmu do linii "b = ObliczamyB(a,t,n)" Wyliczanie losowej
		 * liczby, nwd oraz wyliczenie b
		 */
		while (c == n - 1) {
			while (b.toString().equals("1")) {
				a = LosowaLiczbaA(n);
				nwd = NajwiekszyWspolnyDzielnik(a, n);
				if (nwd > 1) {
					System.out.println("Krok 4 algorytmu - STOP (pojawia się niezwykle rzadko)");
					wynik = nwd;
					break;
				}
				b = ObliczamyB(a, t, n);

			}
			System.out.println("Wylosowane a: " + a);

			System.out.println("Wyliczone d: " + nwd);

			/*
			 * Obliczanie rekurencyjne ciągu
			 */
			c = b.longValue();
			c_i = (c * c) % (n);
			while (c_i != 1) {
				c = c_i;
				c_i = (c * c) % (n);

			}
			System.out.println("Wyliczone c: " + c + "\n");
		}

		/*
		 * Wyświetlanie wyniku (czynników pierwszych)
		 */
		wynik = NajwiekszyWspolnyDzielnik((int) (c - 1), n);
		System.out.println("Obliczony czynnik pierwszy liczby " + n + ": " + wynik);
		drugi_czynnik = n / wynik;
		System.out.println("Drugi czynnik liczby " + n + ": " + drugi_czynnik + "\n");

	}

	/*
	 * Metoda NWD - implementacja na wielkich liczbach
	 */

	private static int NajwiekszyWspolnyDzielnik(int a, int b) {
		BigInteger b1 = BigInteger.valueOf(a);
		BigInteger b2 = BigInteger.valueOf(b);
		BigInteger gcd = b1.gcd(b2);
		return gcd.intValue();
	}

	/*
	 * Obliczanie b = a^t mod n z kroku 6.
	 */
	private static BigInteger ObliczamyB(int a, int t, int n) {
		BigInteger bigIntegerOfA = BigInteger.valueOf(a);
		BigInteger bigIntegerOfN = BigInteger.valueOf(n);
		BigInteger aDoPotegiT = bigIntegerOfA.pow(t);
		BigInteger aDoPotegiTModuloN = aDoPotegiT.mod(bigIntegerOfN);
		return aDoPotegiTModuloN;
	}

	/*
	 * Wybieranie pseudolosowej liczby pierwszej
	 */

	private static int LosowaLiczbaA(int zakresN) {
		/*
		 * Jest_pierwsza daje na false, ponieważ zakładam, że pseudolosowo wybrana
		 * liczba nie jest pierwsza Ma_szanse_byc_pierwsza daje na true, ponieważ
		 * zakładam, że pseudolosowo wybrana liczba ma szansę być pierwsza
		 * 
		 * Uwaga! Uprzedzam, że najpierw sprawdzam, czy liczba jest na liście liczb
		 * pierwszych od 1 do 1000000, a pózniej sprawdzam, czy dzieli się ona przez
		 * inne liczby (teoretycznie powinny być większe od 1000000, ale mamy dość małe
		 * n, co nie miałoby sensu).
		 */
		boolean jest_pierwsza = false;
		boolean ma_szanse_byc_pierwsza = true;
		int wylosowana_losowo_liczba = 0;

		/*
		 * Lista liczb pierwszych do 1000000 Zczytuję je do listy z pliku
		 * "liczby_pierwsze.txt"
		 */
		ArrayList<Integer> list = new ArrayList<Integer>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("src\\liczby_pierwsze.txt"));
			String line = br.readLine();
			while (line != null) {
				String[] words = line.split("\\s+");
				for (String s : words) {

					if (!s.equals("")) {
						list.add(Integer.parseInt(s));
					}
				}
				line = br.readLine();
			}
			br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		while (!jest_pierwsza) { /* Losuje dopoki liczba nie jest pierwsza */
			wylosowana_losowo_liczba = ThreadLocalRandom.current().nextInt(2,
					zakresN); /* Losuje z przedzialu od 2 do n-1 (włącznie z 2 i n-1) */
			ma_szanse_byc_pierwsza = true; /* Daje jej szansę być pierwszą, ale sprawdzę to */

			if (ma_szanse_byc_pierwsza) { /*
											 * Liczba ma szansę być pierwsza, więc sprawdzę czy jest na liście liczb
											 * pierwszych od 1 do 1000000
											 */
				if (list.contains(wylosowana_losowo_liczba)) {
					jest_pierwsza = true; /* Jest na liście, więc jest pierwsza */
					System.out.println("Wylosowana liczba jest na liście, więc jest pierwsza, ale wykonamy dalsze testy");
				} else {
					ma_szanse_byc_pierwsza = false; /* Nie ma jej na liście, więc już nie ma szans */
				}

			}

			if (ma_szanse_byc_pierwsza) {
				/*
				* Użycie szybkiego algorytmu sprawdzenia pierwszości liczby
				 */
				ma_szanse_byc_pierwsza = CzyLiczbaJestPierwsza(wylosowana_losowo_liczba);
				if(ma_szanse_byc_pierwsza) {
					System.out.println("Liczba ma szansę być pierwsza, gdyż algorytm wskazał, że jest, ale przeprowadzimy jeszcze test Fermata");
				}
			}
			
			if(ma_szanse_byc_pierwsza) {
				jest_pierwsza = TestPierwszosciFermata(wylosowana_losowo_liczba, zakresN);
				if(jest_pierwsza) {
					System.out.println("Liczba przeszła wszystkie sprawdzenia i jest pierwsza");
				}
				
			}
			

		}

		return wylosowana_losowo_liczba;
	}

	/*
	 * Szybki algorytm sprawdzenia czy liczba jest pierwsza
	 * Uwaga! Powinien działać na liczbach większych niż 100000 (bo mam listę dla mniejszych liczb),
	 * ale n jest zbyt małe by wtedy mógł zadziałać.
	 */
	static boolean CzyLiczbaJestPierwsza(long n) {
		if (n < 2)
			return false;
		if (n == 2 || n == 3)
			return true;
		if (n % 2 == 0 || n % 3 == 0)
			return false;
		long sqrtN = (long) Math.sqrt(n) + 1;
		for (long i = 6L; i <= sqrtN; i += 6) {
			if (n % (i - 1) == 0 || n % (i + 1) == 0)
				return false;
		}
		return true;
	}
	
	/*
	 * Test pierwszości fermata dla konkretnej zmiennej losowej a
	 */
	static boolean TestPierwszosciFermata(int a, int p) {
		BigInteger bigA = BigInteger.valueOf(a);
		BigInteger bigP = BigInteger.valueOf(p);
		BigInteger potega = bigA.pow(p-1);
		BigInteger modulo = potega.mod(bigP);
		
		boolean wynik = (modulo.compareTo(BigInteger.ONE) != 0) ? true : false;
		return wynik;
	}
	
	/*
	 * Test pierwszości fermata ogólny
	 */
	static boolean TestPierwszosciFermataOgolny(int p, int t) {
		BigInteger bigT = BigInteger.valueOf(t);
		BigInteger bigP = BigInteger.valueOf(p);

		BigInteger b;
		for(int i = 1; i <= t; i++) {
			int a = LosowaLiczbaA(p);
			BigInteger bigA = BigInteger.valueOf(a);
			b = bigA.pow(p-1);
			BigInteger modulo = b.mod(bigP);
			if(modulo.compareTo(BigInteger.ONE) != 0) {
				return false;
			}
		}
		
		return true;
	}

}
