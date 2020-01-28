package ie.gmit.sw;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 
 * @author Aaron
 * @version 1.0
 * @since 1.8
 * 
 * This class acts as a facade to the servlet functionality hiding the inner workings of the rest of the application and returning the predicted language from the database. 
 */

public class ServiceFacade implements LanguageServiceFacade {
	
	private Request req;
	
	private BlockingQueue<Request> inQueue;
	
	private DatabaseProxy db;
	
	private Future<Language> lang;
	
	private Language predictedLanguage;
	
	private ExecutorService es;
	
	/**
	 * Service facade constructor takes blocking queue of Request objects and database proxy as input and initializes executor service
	 * 
	 * @param queue - BlockingQueue<Request> - Blocking queue containing request objects
	 * @param db - DatabaseProxy - object that controls access to database object
	 */

	public ServiceFacade(BlockingQueue<Request> queue, DatabaseProxy db) {
		super();
		this.inQueue = queue;
		this.db = db;
		this.es = Executors.newSingleThreadExecutor();
	}
	
	/**
	 * Overrides interface method. Executes a thread to parse the query text and return the predicted language.
	 * 
	 * @return Language
	 */
	
	@Override
	public Language processQueryRequest() {
		
		try {
			this.req = inQueue.take();
			
			this.lang = es.submit(new QueryParser(req, db));
			
			this.predictedLanguage = lang.get();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			es.shutdown();
		}
		
		return predictedLanguage;
	}

}
