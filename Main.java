package ads1ss13.pa;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Diese Klasse enth&auml;lt nur die {@link #main main()}-Methode zum Starten
 * des Programms, sowie {@link #printDebug(String)} und
 * {@link #printDebug(Object)} zum Ausgeben von Debug Meldungen.
 * 
 * <p>
 * <b>WICHTIG:</b> Nehmen Sie keine &Auml;nderungen in dieser Klasse vor. Bei
 * der Abgabe werden diese &Auml;nderungen verworfen und es k&ouml;nnte dadurch
 * passieren, dass Ihr Programm somit nicht mehr korrekt funktioniert.
 * </p>
 */
public class Main {

	/**
	 * Der Name der Datei, aus der die Testinstanz auszulesen ist. Ist <code>
	 * null</code>, wenn von {@link System#in} eingelesen wird.
	 */
	private static String fileName = null;

	/** Der abgeschnittene Pfad */
	private static String choppedFileName;

	/**
	 * Mit diesem flag kann verhindert werden, dass der Thread nach Ablauf der
	 * Zeit beendet wird.
	 */
	private static boolean dontStop = false;

	/** Test flag f&uuml;r Laufzeit Ausgabe */
	private static boolean test = false;

	/** Debug flag f&uuml;r zus&auml;tzliche Debug Ausgaben */
	private static boolean debug = false;
	
	/** Die Anzahl der Items */
	private static Integer numItems;

	/** Die Anzahl der Ressourcen */
	private static Integer numResources;
	
	/** Die zur Verfügung stehenden Kapazitäten jeder Resource */
	private static Integer[] resourceCapacities;
	
	/** Der Schwellwert f&uuml;r die gelbe Schranke */
	private static Integer threshold;

	/**
	 * Liest die Daten einer Testinstanz ein und &uuml;bergibt sie an die
	 * entsprechenden Methoden der MKP Implementierung.
	 * 
	 * <p>
	 * Wenn auf der Kommandozeile die Option <code>-d</code> angegeben wird,
	 * werden s&auml;mtliche Strings, die an {@link Main#printDebug(String)}
	 * &uuml;bergeben werden, ausgegeben.
	 * </p>
	 * 
	 * <p>
	 * Der erste String in <code>args</code>, der <em>nicht</em> mit <code>-d
	 * </code>, <code>-t</code>, oder <code>-s</code> beginnt, wird als der Pfad
	 * zur Datei interpretiert, aus der die Testinstanz auszulesen ist. Alle
	 * nachfolgenden Parameter werden ignoriert. Wird kein Dateiname angegeben,
	 * wird die Testinstanz &uuml;ber {@link System#in} eingelesen.
	 * </p>
	 * 
	 * @param args
	 *            Die von der Kommandozeile &uuml;bergebenen Argumente. Die
	 *            Option <code>-d</code> aktiviert debug-Ausgaben &uuml;ber
	 *            {@link #printDebug(String)}, <code>-t</code> gibt
	 *            zus&auml;tzlich Dateiname und Laufzeit aus und <code>-s</code>
	 *            verhindert, dass Ihr Algorithmus nach 30 Sekunden beendet
	 *            wird. Der erste andere String wird als Dateiname
	 *            interpretiert.
	 */
	public static void main(String[] args) {

		Scanner is = processArgs(args);

		SecurityManager oldsm = null;
		try {
			oldsm = System.getSecurityManager();
			SecurityManager sm = new ADS1SecurityManager();
			System.setSecurityManager(sm);
		} catch (SecurityException e) {
			bailOut("Error: could not set security manager: " + e);
		}

		try {
			run(readInput(is));
			// Security Manager ruecksetzen
			System.setSecurityManager(oldsm);
		} catch (SecurityException se) {
			bailOut("Unerlaubter Funktionsaufruf: \"" + se.toString() + "\"");
		} catch (NumberFormatException e) {
			bailOut("Falsches Inputformat: \"" + e.toString() + "\"");
		} catch (Exception e) {
			e.printStackTrace();
			bailOut("Ausnahme \"" + e.toString() + "\"");
		}

	}

	/**
	 * Liest einen Testfall vom &uuml;bergebenen {@link Scanner} ein.
	 * 
	 * @param is
	 *            Der {@link Scanner}
	 * @return die Menge der Items
	 * @throws Exception
	 *             falls der Testfall nicht der Spezifikation entspricht.
	 */
	protected static ArrayList<Item> readInput(Scanner is) throws Exception {
		// erste Zeile: Anzahl an Items
		numItems = Integer.valueOf(is.nextLine());
		
		// zweite Zeile: Anzahl an Ressourcen
		numResources = Integer.valueOf(is.nextLine());
		
		// dritte Zeile: einzelne Kapazitäten der Ressourcen
		String val[] = is.nextLine().split(" ");
		if (val.length != numResources) {
			throw new Exception("Fehlerhafte Zeile: Anzahl der Ressourcen stimmt nicht");
		} else {
			resourceCapacities = new Integer[numResources];
			for (int i=0;i<numResources;i++) {
				resourceCapacities[i] = Integer.valueOf(val[i]);
			}
		}
		
		// vierte Zeile: gelbe Schranke fürs Framework
		threshold = Integer.valueOf(is.nextLine());
		
		// speichere Profite (Spalte 0) und Ressourcen in einer Matrix
		int[][] values = new int[numItems][numResources+1];
		int count = 0;
		
		// erste Block: Werte der Items
		while (count < numItems) {
			val = is.nextLine().split(" ");
			
			if ((val.length + count) > numItems) {
				throw new Exception("Fehlerhafte Zeile: Anzahl der Profite stimmt nicht");
			} else {
				
				for (int i=0;i<val.length;i++) {
					values[count][0] = Integer.valueOf(val[i]);
					count++;
				}
			}
		}
		
		// Rest: Ressourcen der Items
		for (int k=1;k<numResources+1;k++) {
		count = 0;
			while (count < numItems) {
				val = is.nextLine().split(" ");
				
				if ((val.length + count) > numItems) {
					throw new Exception("Fehlerhafte Zeile: Anzahl der Resourcen "+k+" stimmt nicht");
				} else {
					
					for (int i=0;i<val.length;i++) {
						values[count][k] = Integer.valueOf(val[i]);
						count++;
					}
				}
			}
		}
		
		// erstelle Items
		ArrayList<Item> items = new ArrayList<Item>(numItems);
		
		for (int i=0;i<numItems;i++) {
			int res[] = new int[numResources];
			for (int j=0;j<numResources;j++) {
				res[j] = values[i][j+1];
			}
			Item it = new Item(i, values[i][0], res);
			items.add(it);
		}
		
		return items;
	}

	/**
	 * Startet Ihre MKP Implementierung mit einem Testfall und
	 * &uuml;berpr&uuml;ft danach Ihre L&ouml;sung.
	 * 
	 * @param items
	 *            Menge an Items
	 * 
	 * @throws Exception
	 *             Signalisiert eine Ausnahme
	 */
	@SuppressWarnings("deprecation")
	protected static void run(ArrayList<Item> items) throws Exception {
		long start = System.currentTimeMillis();
		long end = System.currentTimeMillis();
		long offs = end - start;
		long timeout = 30000; // 30 Sekunden

		chopFileName();
		
		AbstractMKP mkp = new MKP(numItems, numResources, resourceCapacities,
				items);

		// erzeuge mkp thread
		Thread thread = new Thread(mkp, "MKP Thread");
		// starte mkp thread
		thread.start();

		if (dontStop)
			thread.join(0);
		else {
			// beende mkp thread nach timeout millisecs
			thread.join(timeout);
			if (thread.isAlive())
				thread.stop();
		}

		// speichere Endzeit
		end = System.currentTimeMillis();
		
		// speichere Lösung
		AbstractMKP.BnBSolution sol = mkp.getSolution();
		// checke Lösung
		if (sol == null) {
			bailOut("keine gueltige Loesung!");
		}
		
		// speichere Beladung
		ArrayList<Item> solution = sol.getBestSolution();

		// speichere untere Schranke
		int lower_bound = sol.getLowerBound();

		// lösche user object
		mkp = null;


		// checke Items
		boolean[] occurrance = new boolean[numItems];
		for (int i=0;i<numItems;i++) {
			occurrance[i] = false;
		}
		for (Item s : solution) {
			if (s.id >= numItems) {
				bailOut("Loesung enthaelt Gegenstand, der nicht Teil der Eingabe ist.");
			} if (occurrance[s.id]) {
				bailOut("Loesung enthaelt Gegenstand "+s.id+" mehrmals.");
			} else {
				occurrance[s.id] = true;
			}
		}
		
		// checke Kapazitäten
		int testCapacities = 0;
		for (int i=0;i<numResources;i++) {
			testCapacities = 0;
			for (Item s : solution) {
				testCapacities += items.get(s.id).resources[i];
			}
			if (testCapacities > resourceCapacities[i]) {
				bailOut("Ihre Resource "+i+": "+testCapacities+
						" liegt über der Kapazität "+resourceCapacities[i]);
			}
		}
		
		// checke Profit
		int sumProfits = 0;

		for (Item s : solution) {
			sumProfits += items.get(s.id).profit;
		}

		if (sumProfits != lower_bound) {
			bailOut("Die untere Schanke muss immer gleich der aktuell besten Loesung sein!");
		}

		// Ergebnis ausgeben
		StringBuffer msg = new StringBuffer(test ? choppedFileName + ": " : "");

		long sum = end - start - offs;

		printDebug("Loesung: " + solution);
		if (lower_bound < threshold)
			bailOut("zu schlechte Loesung: Ihr Ergebnis " + lower_bound
					+ " liegt unter dem Schwellwert (" + threshold + ")");

		msg.append("Schwellwert = " + threshold + "." + " Ihr Ergebnis ist OK mit " +
				"\n" + lower_bound);

		if (test)
			msg.append(", Zeit: "
					+ (sum > 1000 ? sum / 1000 + "s" : sum + "ms"));

		System.out.println();
		System.out.println(msg.toString());
	}

	/**
	 * &Ouml;ffnet die Eingabedatei und gibt einen {@link Scanner} zur&uuml;ck,
	 * der von ihr liest. Falls kein Dateiname angegeben wurde, wird von
	 * {@link System#in} gelesen.
	 * 
	 * @return Einen {@link Scanner} der von der Eingabedatei liest.
	 */
	private static Scanner openInputFile() {
		if (fileName != null)
			try {
				return new Scanner(new FileInputStream(fileName));
			} catch (NoSuchElementException e) {
				bailOut("\"" + fileName + "\" is empty");
			} catch (Exception e) {
				bailOut("could not open \"" + fileName + "\" for reading");
			}

		return new Scanner(System.in);

	}

	/**
	 * Interpretiert die Parameter, die dem Programm &uuml;bergeben wurden und
	 * gibt einen {@link Scanner} zur&uuml;ck, der von der Testinstanz liest.
	 * 
	 * @param args
	 *            Die Eingabeparameter
	 * @return Einen {@link Scanner} der von der Eingabedatei liest.
	 */
	protected static Scanner processArgs(String[] args) {
		for (String a : args) {
			if (a.equals("-s")) {
				dontStop = true;
			} else if (a.equals("-t")) {
				test = true;
			} else if (a.equals("-d")) {
				debug = test = true;
			} else {
				fileName = a;

				break;
			}
		}

		return openInputFile();
	}

	/**
	 * Gibt die Meldung <code>msg</code> aus und beendet das Programm.
	 * 
	 * @param msg
	 *            Die Meldung die ausgegeben werden soll.
	 */
	private static void bailOut(String msg) {
		System.out.println();
		System.err.println((test ? choppedFileName + ": " : "") + "ERR " + msg);
		System.exit(1);
	}

	/**
	 * Generates a chopped String representation of the filename.
	 */
	private static void chopFileName() {
		if (fileName == null) {
			choppedFileName = "System.in";
			return;
		}

		int i = fileName.lastIndexOf(File.separatorChar);

		if (i > 0)
			i = fileName.lastIndexOf(File.separatorChar, i - 1);
		if (i == -1)
			i = 0;

		choppedFileName = ((i > 0) ? "..." : "") + fileName.substring(i);
	}

	/**
	 * Gibt eine debugging Meldung aus. Wenn das Programm mit <code>-d</code>
	 * gestartet wurde, wird <code>msg</code> zusammen mit dem Dateinamen der
	 * Inputinstanz ausgegeben, ansonsten macht diese Methode nichts.
	 * 
	 * @param msg
	 *            Text der ausgegeben werden soll.
	 */
	public static synchronized void printDebug(String msg) {
		if (!debug)
			return;

		System.out.println(choppedFileName + ": DBG " + msg);
	}

	/**
	 * Gibt eine debugging Meldung aus. Wenn das Programm mit <code>-d</code>
	 * gestartet wurde, wird <code>msg</code> zusammen mit dem Dateinamen der
	 * Inputinstanz ausgegeben, ansonsten macht diese Methode nichts.
	 * 
	 * @param msg
	 *            Object das ausgegeben werden soll.
	 */
	public static void printDebug(Object msg) {
		printDebug(msg.toString());
	}

	/**
	 * The constructor is private to hide it from JavaDoc.
	 * 
	 */
	private Main() {
	}

}
