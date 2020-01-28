package ie.gmit.sw;

/**
 * 
 * @author Aaron
 * @version 1.0
 * @since 1.8
 * 
 * This interface defines a method to be implemented in the Service facade which will hide the implementation of the work being done behind the scenes.
 */

public interface LanguageServiceFacade {
	
	/**
	 * Defines the method to be implemented in the facade class. Returns the predicted language instance.
	 * 
	 * @return Language
	 */
	
	public Language processQueryRequest();

}
