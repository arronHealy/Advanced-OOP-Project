package ie.gmit.sw;

import java.util.Map;

/**
 * 
 * @author Aaron
 * @version 1.0
 * @since 1.8
 * 
 * Language Database interface defines the common functionality between the Database and Database Proxy.
 */

public interface LanguageDatabase {
	
	/**
	 * Defines method to be implemented to add language and kmer to database
	 * 
	 * @param seq - CharSequence - Kmer to be stored
	 * @param lang - Language - Language to be stored
	 */
	
	public void add(CharSequence seq, Language lang);
	
	/**
	 * Defiens method to implemented to resize the database based on highest ranking kmers for a given language
	 * 
	 * @param max - int - the max value to resize the database
	 */
	
	public void resize(int max);
	
	/**
	 * Defines the method to be implemented to rank the kmers in descending order in a database based on the frequency of occurence
	 * 
	 * @param max - int - the number of entries in a database to be ranked
	 * 
	 * @param lang - Language - The given language to identify the kmers in the database
	 * 
	 * @return Map<Integer, LanguageEntry>
	 */
	
	public Map<Integer, LanguageEntry> getTop(int max, Language lang);
	
	/**
	 * Defines the method to be implemented to return the given language the system considers to be the query language entered.
	 * 
	 * @param query - Map<Integer, LanguageEntry> - The query map passed in to compare with the other languages stored in the database.
	 * 
	 * @return Language
	 */
	
	public Language getLanguage(Map<Integer, LanguageEntry> query);
	
	/**
	 * Defines the method to be implemented to return the size of the populated the database.
	 * 
	 * @return int
	 */
	
	public int size();

}
