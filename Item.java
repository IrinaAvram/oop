package ads1ss13.pa;

/**
 * Ein Item der Eingabe-Instanz.
 * 
 * <p>
 * Auf die Id des Items, den Profit und die Ressourcen kann einfach &uuml;ber die
 * &ouml;ffentlichen Variablen {@link #id}, {@link #profit} und
 * {@link #resources} zugegriffen werden. Allerdings k&ouml;nnen diese Werte nicht
 * ver&auml;ndert werden.
 * </p>
 * 
 * <p>
 * <b>WICHTIG:</b> Nehmen Sie keine &Auml;nderungen an dieser Klasse vor! Diese
 * werden vom Abgabesystem verworfen und es k&ouml;nnte sein, dass Ihr Programm
 * dann nicht mehr korrekt funktioniert.
 * </p>
 */
public class Item implements Comparable<Item> {

	/** Die Id des Items. */
	public final int id;
	/** Der Profit des Items. */
	public final int profit;
	/** Die Ressourcen des Items als Array. */
	public final int[] resources;

	/**
	 * Erzeugt ein neues Item mit Id <code>id</code>, Wert <code>profit</code>
	 * und Ressourcen <code>resources</code>.
	 * 
	 * <p>
	 * Das Erzeugen eines neuen Item-Objekts hat den Aufwand <i>O(1)</i>.
	 * </p>
	 * 
	 * @param id
	 *            Die Id des Items.
	 * @param p
	 *            Der Profit des Items.
	 * @param r
	 *            Die Ressourcen des Items als Array.
	 */
	public Item(int id, int p, int[] r) {
		this.id = id;
		this.profit = p;
		this.resources = r;
	}
	
	/**
	 * Liefert eine String-Repr&auml;sentation dieses Items, d.h. die Id.
	 * 
	 * Der Aufwand dieser Operation ist in <i>O(1)</i>.
	 * 
	 * @return Ein {@link String} der dieses Item beschreibt.
	 */
	@Override
	public String toString() {
		return ""+id;
	}
	
	/**
	 * Testet zwei Items auf Gleichheit.
	 * 
	 * <p>
	 * Zwei Items sind gleich, wenn Ihre Ids, Profite und Ressourcen gleich sind.
	 * </p>
	 * 
	 * <p>
	 * Der Aufwand des Vergleichs ist in <i>O(|Ressourcen|)</i>.
	 * </p>
	 * 
	 * @param other
	 *            Das andere Item mit dem verglichen werden soll.
	 * @return <code>true</code> wenn die beiden Items gleich sind,
	 *         <code>false</code> sonst.
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof Item) {
			Item o = (Item) other;
			boolean equal = true;
			
			// Vergleich der Ids
			if (this.id != o.id) {
				return false;
			}
			
			// Vergleich der Profite
			if (this.profit != o.profit) {
				return false;
			}
			
			// Vergleich der Ressourcen
			if (this.resources.length != o.resources.length) {
				return false;
			} else {
				for (int i=0;i<this.resources.length;i++) {
					if (this.resources[i] != o.resources[i]) {
						return false;
					}
				}
			}
			
			// wenn alles gleich ist
			return true;
			
		} else {
			return false;
		}
	}

	/**
	 * Vergleicht zwei Items gemäß ihrer Profite (<code>profit</code>).
	 */
	@Override
	public int compareTo(Item o) {
		if (this.profit < o.profit) {
			return -1;
		} else if (this.profit > o.profit) {
			return 1;
		} else {
			return 0;
		}
	}

}
