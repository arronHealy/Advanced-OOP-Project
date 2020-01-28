package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

/**
 * 
 * @author Aaron
 * @version 1.0
 * @since 1.8
 * 
 * File parser class <i>implements</i> Callable interface. Which in turn returns an instance of a Database Proxy.
 * 
 * This class takes the location of the data file as a string and attempts to parse it into the database based on the language name and the kmers it produces.
 */

public class FileParser implements Callable<DatabaseProxy> {

	private String dataFile;

	private int k;

	private DatabaseProxy db;
	
	/**
	 * File parser constructor takes file path and kmer size as input and instantiates new instance of database proxy.
	 * 
	 * @param dataFile - String - file path as string
	 * @param k - int - the size of Kmers the file contents should be parsed into.
	 */

	public FileParser(String dataFile, int k) {
		super();
		this.dataFile = dataFile;
		this.k = k;
		this.db = new DatabaseProxy();
	}
	
	/**
	 * parse method converts the language text into kmers and stores them via database proxy.
	 * 
	 * @param text - String - the language text to be converted into kmers
	 * @param lang - String - the language of the text to be stored along with the Kmer text.
	 */

	private void parse(String text, String lang) {
		Language language = Language.valueOf(lang);

		for (int i = 0; i < text.length() - k; i++) {
			CharSequence kmer = text.substring(i, i + k);
			db.add(kmer, language);
		}
	}
	
	/**
	 * call method splits data file into language text and language name delimited by the @ symbol
	 * <br>
	 * Returns instance of the built database proxy to be used again to compare with with query text
	 * 
	 * @return DatabaseProxy
	 */

	@Override
	public DatabaseProxy call() throws Exception {
		// TODO Auto-generated method stub
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile)));
			String line = null;

			while ((line = br.readLine()) != null) {
				String[] record = line.trim().split("@");
				if (record.length != 2) {
					continue;
				}
				parse(record[0], record[1]);
			}

			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.resize(300);
		}
		return db;
	}

}
