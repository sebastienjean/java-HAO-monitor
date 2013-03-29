package fr.iutvalence.ubpe.core.interfaces;

/**
 * Defines the general contracts for a data event listener
 * 
 * @author sebastienjean
 * 
 */
public interface DataEventListener
{
	/**
	 * Notifies for event processing.
	 * 
	 * @param event
	 *            the data event to process
	 */
	public void process(DataEvent event);
}
