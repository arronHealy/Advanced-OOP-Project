package ie.gmit.sw;

/**
 * 
 * @author Aaron
 * @version 1.0
 * @since 1.8
 * 
 * Request object stores the information for the job number and the query text.
 */

public class Request {
	
	private String jobNum;
	
	private String queryText;
	
	/**
	 * Constructor object assigns the job number and query text
	 * 
	 * @param jobNum
	 * @param queryText
	 */

	public Request(String jobNum, String queryText) {
		super();
		this.jobNum = jobNum;
		this.queryText = queryText;
	}
	
	/**
	 * Returns the assigned job number
	 * 
	 * @return String
	 */

	public String getJobNum() {
		return jobNum;
	}
	
	/**
	 * Sets the value for the job number
	 * 
	 * @param jobNum - String - sets the job number variablr
	 */

	public void setJobNum(String jobNum) {
		this.jobNum = jobNum;
	}
	
	/**
	 * Gets the value for the given query text
	 * 
	 * @return String
	 */

	public String getQueryText() {
		return queryText;
	}
	
	/**
	 * Sets the value for the given query text
	 * 
	 * @param queryText - String - sets the query text for the given request
	 */

	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

}
