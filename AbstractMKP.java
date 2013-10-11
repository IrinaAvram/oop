package ads1ss13.pa;

import java.util.ArrayList;

/**
 * Abstrakte Klasse zum Berechnen der Beladung des Rucksacks mittels Branch-and-Bound.
 * 
 * <p>
 * <b>WICHTIG:</b> Nehmen Sie keine &Auml;nderungen in dieser Klasse vor. Bei
 * der Abgabe werden diese &Auml;nderungen verworfen und es k&ouml;nnte dadurch
 * passieren, dass Ihr Programm somit nicht mehr korrekt funktioniert.
 * </p>
 */
public abstract class AbstractMKP implements Runnable {
	
	/** Die bisher beste L&ouml;sung */
	private BnBSolution sol;

	/**
	 * Diese Methode setzt einen neue (beste) L&ouml;sung.
	 * 
	 * <p>
	 * <strong>ACHTUNG:</strong> die L&ouml;sung wird nur &uuml;bernommen wenn
	 * <code>newLowerBound</code> h&ouml;her ist als {@link #lowerBound}.
	 * </p>
	 * 
	 * @param newLowerBound
	 *            neue untere Schranke
	 * @param newSolution
	 *            neue beste L&ouml;sung
	 * @return Wahr wenn die L&ouml;sung &uuml;bernommen wurde.
	 */
	final public synchronized boolean setSolution(int newLowerBound, ArrayList<Item> newSolution) {
		if (sol == null || newLowerBound > sol.getLowerBound()) {
			sol = new BnBSolution(newLowerBound, newSolution);
			return true;
		}
		return false;
	}
	
	/**
	 * Gibt die bisher beste gefundene L&ouml;sung zur&uuml;ck.
	 * 
	 * @return Die bisher beste gefundene L&ouml;sung.
	 */
	final public BnBSolution getSolution() {
		return sol;
	}
	
	public final class BnBSolution {

		private int lowerBound = Integer.MAX_VALUE;
		private ArrayList<Item> bestSolution;
		
		public BnBSolution(int newLowerBound, ArrayList<Item> newSolution) {
			lowerBound = newLowerBound;
			bestSolution = newSolution;
		}

		/**
		 * @return Die untere Schranke
		 */
		public int getLowerBound() {
			return lowerBound;
		}

		/**
		 * @return Die Items der bisher besten L&ouml;sung
		 */
		public ArrayList<Item> getBestSolution() {
			return bestSolution;
		}
		
	}

}
