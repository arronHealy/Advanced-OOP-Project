package ie.gmit.sw;

import java.util.Map;

/**
 * 
 * @author Aaron
 * @version 1.0
 * @since 1.8
 * 
 * 
 * Database proxy class <i>implements</i> Language database interface.
 * <br>
 * This class acts as a proxy to the actual database to limit the access the client may have to it.
 * <br>
 * This class instatiates the database and allows user to storer data indirectly.
 *
 */

public class DatabaseProxy implements LanguageDatabase {
	
	private Database db = new Database();
	
	private int kmerSize = -1;

	/**
	 * Overrides interface implementation. Adds to database by calling database instance method.
	 * 
	 * @param seq - CharSequence - Kmer to add to database
	 * 
	 * @param lang - Language - The language the kmer belongs too.
	 */
	
	@Override
	public void add(CharSequence seq, Language lang) {
		// TODO Auto-generated method stub
		db.add(seq, lang);
		
		if(kmerSize == -1) {
			setKmerSize(seq.length());
		}
	}

	/**
	 * Overrides interface implementation. Indirectly calls the databases resize method.
	 * 
	 * @param max - int - max size the database should be resized too.
	 */
	
	@Override
	public void resize(int max) {
		// TODO Auto-generated method stub
		db.resize(max);
	}

	/**
	 * Overrides interface implementation. Indirectly calls the databases getTop method.
	 * 
	 * @param max - int - max size the database should be resized too.
	 * @param lang - Language - The language int the database that should be used.
	 * 
	 * @return Map<Integer, LanguageEntry>
	 */
	
	@Override
	public Map<Integer, LanguageEntry> getTop(int max, Language lang) {
		// TODO Auto-generated method stub
		return db.getTop(max, lang);
	}
	
	/**
	 * Overrides interface implementation. Indirectly calls the databases method to get matching language.
	 * 
	 * @param query - Map<Integer, LanguageEntry> - max size the database should be resized too.
	 * 
	 * @return Language
	 */

	@Override
	public Language getLanguage(Map<Integer, LanguageEntry> query) {
		// TODO Auto-generated method stub
		return db.getLanguage(query);
	}
	
	/**
	 * Overrides interface implementation. Indirectly calls the databases size method.
	 * 
	 * @return int
	 */

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return db.size();
	}
	
	/**
	 * Set Kmer size in proxy to be used as reference when parsing query.
	 * 
	 * @param k - int - the size the kmer should be set too.
	 */

	public void setKmerSize(int k) {
		// TODO Auto-generated method stub
		this.kmerSize = k;
	}
	
	/**
	 * Get Kmer size. returns size of kmer tiles to be used.
	 * 
	 * @return int
	 */
	
	public int getKmerSize() {
		return kmerSize;
	}

}
