package ie.gmit.sw;

/**
 * 
 * @author Aaron
 * @version 1.0
 * @since 1.8
 * 
 * Out of place metric class <i>implements</i> Comparable interface to compare instances of itself.
 * <br>
 * This class assigns the language and computes the passed distance metric to determine the absolute value of one language compared to another. 
 */

public class OutOfPlaceMetric implements Comparable<OutOfPlaceMetric> {
	
	private Language lang;
	private int distance;
	
	/**
	 * Constructor assigns language and distance metric passed in
	 * 
	 * @param lang - Language - The given language to be compared
	 * 
	 * @param distance - the distance computed from the Database method
	 */
	
	public OutOfPlaceMetric(Language lang, int distance) {
		super();
		this.lang = lang;
		this.distance = distance;
	}
	
	/**
	 * Returns the assigned language
	 * 
	 * @return Language
	 */
	
	public Language getLanguage() {
		return lang;
	}
	
	/**
	 * Returns the absolute value for the distance metric passed in
	 * 
	 * @return int
	 */

	public int getAbsoluteDistance() {
		return Math.abs(distance);
	}
	
	/**
	 * Compares the absolute distance of itself with another instance of itself.
	 * 
	 * @return int
	 */

	@Override
	public int compareTo(OutOfPlaceMetric o) {
		return Integer.compare(this.getAbsoluteDistance(), o.getAbsoluteDistance());
	}

}
