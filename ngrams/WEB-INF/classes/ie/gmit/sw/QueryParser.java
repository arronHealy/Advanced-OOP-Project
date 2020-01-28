package ie.gmit.sw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;


/**
 * 
 * @author Aaron
 * @version 1.0
 * @since 1.8
 * 
 * Query parser class <i>implements</i> Callable interface which then returns predicted Language instance.
 * <br>
 * This class takes the request object and database instance and computes the query text and compares it with the other languages in the database.
 * <br>
 * Once the the text is parsed into kmers and compared it returns the language it thinks is the matching language.
 */

public class QueryParser implements Callable<Language> {

	private TreeMap<Integer, LanguageEntry> queryMap;

	private TreeMap<Integer, LanguageEntry> sortedQueryMap;

	private Request req;

	private DatabaseProxy db;

	private CharSequence kmer;

	private int kmerHashCode;
	
	/**
	 * Constructor object takes request and database object and assigns them to variables.
	 * <br>
	 * Local Map variables are initialized for use to build query map for comparison of subject database.
	 *  
	 * @param req - Request - object contains job number and query text to parse.
	 * 
	 * @param db - DatabaseProxy - database object used to control access to actual database object
	 */

	public QueryParser(Request req, DatabaseProxy db) {
		// TODO Auto-generated constructor stub
		this.req = req;
		this.db = db;
		this.queryMap = new TreeMap<>();
		this.sortedQueryMap = new TreeMap<>();
	}
	
	/**
	 * This method takes query map built from query text passed in from Request object.
	 * <br>
	 * It then sorts and rank the map entries based on the query text and passes the ranked map to the database for comparison and returns the predicted language.
	 * 
	 * @param queryMap - TreeMap<Integer, LanguageEntry> - queryMap to be sorted and used for comparison
	 * 
	 * @return Language
	 */

	private Language rankAndPredictLanguage(TreeMap<Integer, LanguageEntry> queryMap) {
		
		int rank = 1;

		List<Map.Entry<Integer, LanguageEntry>> sortedList = new ArrayList<>(queryMap.entrySet());

		Collections.sort(sortedList, new Comparator<Map.Entry<Integer, LanguageEntry>>() {

			@Override
			public int compare(Map.Entry<Integer, LanguageEntry> o1, Map.Entry<Integer, LanguageEntry> o2) {
				// TODO Auto-generated method stub
				return -Integer.compare(o1.getValue().getFrequency(), o2.getValue().getFrequency());
			}
		});

		for (Map.Entry<Integer, LanguageEntry> le : sortedList) {
			le.getValue().setRank(rank);
			sortedQueryMap.put(le.getValue().getKmer(), le.getValue());
			rank++;
		}

		return db.getLanguage(sortedQueryMap);
	}
	
	/**
	 * Call method returns the predicted language. It first parses the query text into kmers and stores them in a query map.
	 * <br>
	 * It then calls the rank and predict language method which in turn returns the predicted language. 
	 * 
	 * @return Language
	 */

	@Override
	public Language call() throws Exception {
		// TODO Auto-generated method stub

		for (int i = 0; i < req.getQueryText().length() - db.getKmerSize(); i++) {
			kmer = req.getQueryText().substring(i, i + db.getKmerSize());

			kmerHashCode = kmer.hashCode();

			if (queryMap.containsKey(kmerHashCode)) {
				queryMap.get(kmerHashCode).setFrequency(queryMap.get(kmerHashCode).getFrequency() + 1);
			} else {
				queryMap.put(kmerHashCode, new LanguageEntry(kmerHashCode, 1));
			}
		}

		return rankAndPredictLanguage(queryMap);
	}

}
