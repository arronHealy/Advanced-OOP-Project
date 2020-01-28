package ie.gmit.sw;

/**
 * 
 * @author Aaron
 * @version 1.0
 * @since 1.8
 * 
 * Language Entry class <i>implements</i> Comparable interface.
 * <br>
 * This class is used to store the kmer value based on it's int hash code, frequency of occurrence in language text and it's rank then in the database.
 * <br>
 * Comparable interface then compares it to another instance of itself based on frequency of occurrence.
 */

public class LanguageEntry implements Comparable<LanguageEntry> {
	private int kmer;
	private int frequency;
	private int rank;
	
	/**
	 * Constructor sets up the object instance and assigns the kmer and frequency values.
	 * 
	 * @param kmer - int - hash code of the kmer Char Sequence tile.
	 *  
	 * @param frequency - int - the frequency this kmer occurs for a given language.
	 */

	public LanguageEntry(int kmer, int frequency) {
		super();
		this.kmer = kmer;
		this.frequency = frequency;
	}
	
	/**
	 * Returns the int hash code value for the assigned kmer.
	 * 
	 * @return int
	 */

	public int getKmer() {
		return kmer;
	}
	
	/**
	 * Sets the value for the given kmer
	 * 
	 * @param kmer - int - sets the kmer value based char sequence hash code.
	 */

	public void setKmer(int kmer) {
		this.kmer = kmer;
	}
	
	/**
	 * Returns the int value for the frequency of occurrence for a given kmer.
	 * 
	 * @return int
	 */

	public int getFrequency() {
		return frequency;
	}
	
	/**
	 * Sets the frequency of ocurrence for a given kmer
	 * 
	 * @param kmer - int - sets the frequency of occurrence a kmer appears in a language.
	 */

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	/**
	 * Gets the rank value for a kmer in a database
	 * 
	 * @return int
	 */

	public int getRank() {
		return rank;
	}
	
	/**
	 * Sets the rank value for the given kmer in the database
	 * 
	 * @param rank - int - sets the rank value for a kmer in a database.
	 */

	public void setRank(int rank) {
		this.rank = rank;
	}
	
	/**
	 * Override implementation of comparable interfaces compareTo method. Compares frequency of occurrence of one language entry compared to another.
	 */

	@Override
	public int compareTo(LanguageEntry next) {
		return - Integer.compare(frequency, next.getFrequency());
	}

}