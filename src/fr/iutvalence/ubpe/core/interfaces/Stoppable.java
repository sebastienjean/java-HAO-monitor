package fr.iutvalence.ubpe.core.interfaces;

/**
 * Defines the general contract of a stoppable process.
 * 
 * @author sebastienjean
 * 
 */
public interface Stoppable
{
	/**
	 * Notifies for termination.
	 */
	public void mustStop();
}
