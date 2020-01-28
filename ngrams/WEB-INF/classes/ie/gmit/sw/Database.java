package ie.gmit.sw;

import java.util.*;

/**
 * 
 * @author Aaron
 * @version 1.0
 * @since 1.8
 * 
 * Database class <i>implements</i> Language Database interface.
 * <br>
 * This class acts as a database and stores the language names and Kmer tiles parsed from language text file.
 */


public class Database implements LanguageDatabase {
	private Map<Language, Map<Integer, LanguageEntry>> db = new TreeMap<>();
	
	
	/**
	 * add method overrides interface method implementation.
	 * <br>
	 * Gets language database from defined map. Either adds new entry to database or increases frequency if kmer already present.
	 * 
	 * @param seq - CharSequence - parsed kmer tile to store as key in database.
	 * 
	 * @param lang - Language - language passed to check whether it already exists in database. 
	 */
	
	@Override
	public void add(CharSequence s, Language lang) {
		int kmer = s.hashCode();
		Map<Integer, LanguageEntry> langDb = getLanguageEntries(lang);
		
		int frequency = 1;
		if (langDb.containsKey(kmer)) {
			frequency += langDb.get(kmer).getFrequency();
		}
		langDb.put(kmer, new LanguageEntry(kmer, frequency));	
	}
	
	/**
	 * private method returns database map that represents an entry for a language along with it's frequency.
	 * <br>
	 * Checks for existence of language in database and either creates a new map or returns existing map.
	 * 
	 * @param lang - Language - the language to check for in database.
	 * 
	 * @return Map<Integer, LanguageEntry>
	 */
	
	private Map<Integer, LanguageEntry> getLanguageEntries(Language lang){
		Map<Integer, LanguageEntry> langDb = null; 
		if (db.containsKey(lang)) {
			langDb = db.get(lang);
		}else {
			langDb = new TreeMap<Integer, LanguageEntry>();
			db.put(lang, langDb);
		}
		return langDb;
	}
	
	/**
	 * Overrides interface implementation method.
	 * <br>
	 * Resizes database language entries in descending order based on highest frequencies of Kmers present.
	 * 
	 * @param max - int - max size of entries to return.
	 */
	
	@Override
	public void resize(int max) {
		Set<Language> keys = db.keySet();
		for (Language lang : keys) {
			Map<Integer, LanguageEntry> top = getTop(max, lang);
			db.put(lang, top);
		}
	}
	
	/**
	 * Overrides interface implementation. Gets language database entry values sorted based on frequency.
	 * <br>
	 * Then assigns a rank to the entry as to where it is placed in the database.
	 * 
	 * @param max - int - The max number of kmers to be ranked
	 * 
	 * @param lang - Language - The language to rank based on its kmer entries
	 */
	
	@Override
	public Map<Integer, LanguageEntry> getTop(int max, Language lang) {
		Map<Integer, LanguageEntry> temp = new TreeMap<>();
		Set<LanguageEntry> les = new TreeSet<>(db.get(lang).values());

		int rank = 1;
		for (LanguageEntry le : les) {
			le.setRank(rank);
			temp.put(le.getKmer(), le);			
			if (rank == max) break;
			rank++;
		}
		
		return temp;
	}
	
	/**
	 * Overrides interface implementation. Returns most likely language based on the query map passed to method.
	 * <br>
	 * Loops through all languages in database and computes out of place distance between them.
	 * 
	 * @param query - Map<Integer, LanguageEntry> - the query text map to be compared with languages in database.
	 * 
	 * @return Language
	 */
	
	@Override
	public Language getLanguage(Map<Integer, LanguageEntry> query) {
		TreeSet<OutOfPlaceMetric> oopm = new TreeSet<>();
		
		Set<Language> langs = db.keySet();
		for (Language lang : langs) {
			oopm.add(new OutOfPlaceMetric(lang, getOutOfPlaceDistance(query, db.get(lang))));
		}
		return oopm.first().getLanguage();
	}
	
	/**
	 * Overrides interface implementation. Returns size of database. Mainly used for testing.
	 * 
	 * @return int
	 */
	
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return db.size();
	}

	/**
	 * Computes out of place distance between two sorted and ranked maps. Returns int value to indicate how closely related they are or not.
	 * 
	 * @return int
	 */
	
	private int getOutOfPlaceDistance(Map<Integer, LanguageEntry> query, Map<Integer, LanguageEntry> subject) {
		int distance = 0;
		
		Set<LanguageEntry> les = new TreeSet<>(query.values());		
		for (LanguageEntry q : les) {
			LanguageEntry s = subject.get(q.getKmer());
			if (s == null) {
				distance += subject.size() + 1;
			}else {
				distance += s.getRank() - q.getRank();
			}
		}
		return distance;
	}
	
}