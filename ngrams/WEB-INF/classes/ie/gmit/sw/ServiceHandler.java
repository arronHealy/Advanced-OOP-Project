package ie.gmit.sw;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.Map;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;



/*
 * To compile this servlet, open a command prompt in the web application directory and execute the following commands:
 *
 * Linux/Mac																	Windows
 * ---------																	---------
 * cd WEB-INF/classes/															cd WEB-INF\classes\
 * javac -cp .:$TOMCAT_HOME/lib/servlet-api.jar ie/gmit/sw/*.java				javac -cp .:%TOMCAT_HOME%/lib/servlet-api.jar ie/gmit/sw/*.java
 * cd ../../																	cd ..\..\
 * jar -cf ngrams.war *															jar -cf ngrams.war *
 *
 * Drag and drop the file ngrams.war into the webapps directory of Tomcat to deploy the application. It will then be
 * accessible from http://localhost:8080.
 *
 * NOTE: the text file containing the 253 different languages needs to be placed in /data/wili-2018-Edited.txt. This means
 * that you must have a "data" directory in the root of your file system that contains a file called "wili-2018-Edited.txt".
 * Do NOT submit the wili-2018 text file with your assignment!
 *
*/

/**
 * 
 * @author Aaron
 * @version 1.0
 * @since 1.8
 * 
 * This class acts as a servlet to the rest of the application, the servlet takes the query text converts it to response and prints a predicted language text once a prediction has been made.
 */


public class ServiceHandler extends HttpServlet {
	private String languageDataSet = null; //This variable is shared by all HTTP requests for the servlet
	
	private static long jobNumber = 0; //The number of the task in the async queue
	
	private ExecutorService es;
	
	private Future<DatabaseProxy> db;
	
	private DatabaseProxy database;

	private File f;
	
	private BlockingQueue<Request> inQueue = new ArrayBlockingQueue<>(10);
	
	private Map<String, Language> outQueue = new ConcurrentSkipListMap<>();
	
	/**
	 * init method acts as a constructor to the servlet and once constructed proceeds to populate the database and return the database instance.
	 * @throws ServletException
	 */

	public void init() throws ServletException {
		ServletContext ctx = getServletContext(); //Get a handle on the application context
		languageDataSet = ctx.getInitParameter("LANGUAGE_DATA_SET"); //Reads the value from the <context-param> in web.xml

		//You can start to build the subject database at this point. The init() method is only ever called once during the life cycle of a servlet
		f = new File(languageDataSet);
		
		try{
			es = Executors.newSingleThreadExecutor();
		
			db = es.submit(new FileParser(languageDataSet, 4));
		
			database = db.get();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e){
			e.printStackTrace();
		} finally {
			es.shutdown();
		}
		
	}
	
	/**
	 * This method takes the query text from the jsp page and executes the functionality to return the predicted language.
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html"); //Output the MIME type
		PrintWriter out = resp.getWriter(); //Write out text. We can write out binary too and change the MIME type...

		//Initialise some request varuables with the submitted form info. These are local to this method and thread safe...
		String option = req.getParameter("cmbOptions"); //Change options to whatever you think adds value to your assignment...
		String s = req.getParameter("query");
		String taskNumber = req.getParameter("frmTaskNumber");


		out.print("<html><head><title>Advanced Object Oriented Software Development Assignment</title>");
		out.print("</head>");
		out.print("<body>");

		if (taskNumber == null){
			taskNumber = new String("T" + jobNumber);
			jobNumber++;
			//Add job to in-queue
			try{
				inQueue.put(new Request(taskNumber, s));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Language lang = new ServiceFacade(inQueue, database).processQueryRequest();
			
			outQueue.put(taskNumber, lang);
			
		}else{
			//Check out-queue for finished job
			if(outQueue.containsKey(taskNumber)){
				out.print("Predicted Language: " + outQueue.get(taskNumber));
				outQueue.remove(taskNumber);
			}
		}



		out.print("<H1>Processing request for Job#: " + taskNumber + "</H1>");
		out.print("<div id=\"r\"></div>");

		out.print("<font color=\"#993333\"><b>");
		out.print("Language Database size: " + database.size());
		out.print("\nLanguage Dataset is located at " + languageDataSet + " and is <b><u>" + f.length() + "</u></b> bytes in size");
		out.print("<br>Option(s): " + option);
		out.print("<br>Query Text : " + s);
		out.print("</font><p/>");

		out.print("<br>This servlet should only be responsible for handling client request and returning responses. Everything else should be handled by different objects. ");
		out.print("Note that any variables declared inside this doGet() method are thread safe. Anything defined at a class level is shared between HTTP requests.");
		out.print("</b></font>");


		out.print("<P> Next Steps:");
		out.print("<OL>");
		out.print("<LI>Generate a big random number to use a a job number, or just increment a static long variable declared at a class level, e.g. jobNumber.");
		out.print("<LI>Create some type of an object from the request variables and jobNumber.");
		out.print("<LI>Add the message request object to a LinkedList or BlockingQueue (the IN-queue)");
		out.print("<LI>Return the jobNumber to the client web browser with a wait interval using <meta http-equiv=\"refresh\" content=\"10\">. The content=\"10\" will wait for 10s.");
		out.print("<LI>Have some process check the LinkedList or BlockingQueue for message requests.");
		out.print("<LI>Poll a message request from the front of the queue and pass the task to the language detection service.");
		out.print("<LI>Add the jobNumber as a key in a Map (the OUT-queue) and an initial value of null.");
		out.print("<LI>Return the result of the language detection system to the client next time a request for the jobNumber is received and the task has been complete (value is not null).");
		out.print("</OL>");

		out.print("<form method=\"POST\" name=\"frmRequestDetails\">");
		out.print("<input name=\"cmbOptions\" type=\"hidden\" value=\"" + option + "\">");
		out.print("<input name=\"query\" type=\"hidden\" value=\"" + s + "\">");
		out.print("<input name=\"frmTaskNumber\" type=\"hidden\" value=\"" + taskNumber + "\">");
		out.print("</form>");
		out.print("</body>");
		out.print("</html>");

		out.print("<script>");
		out.print("var wait=setTimeout(\"document.frmRequestDetails.submit();\", 10000);");
		out.print("</script>");


	}
	
	/**
	 * This method posts the form every 10 seconds in which it checks for a predicted language
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
 	}
}