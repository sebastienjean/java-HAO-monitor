package fr.iutvalence.ubpe.core.services;

import fr.iutvalence.ubpe.core.interfaces.Stoppable;

/**
 * Helper implementation of a service.
 * 
 * 
 * @author sebastienjean
 *
 */
public abstract class AbstractService implements Runnable, Stoppable
{
	/**
	 * Boolean used to control termination.
	 */
	protected volatile boolean mustRun;
	
	/**
	 * Creating a <tt>AbstractService</tt> instance.
	 */
	public AbstractService()
	{	
		this.mustRun = true;
	}

	/**
	 * @see fr.iutvalence.ubpe.core.interfaces.Stoppable#mustStop()
	 */
	public void mustStop()
	{
		this.mustRun = false;
	}

	/**
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		while (this.mustRun)
			this.serve();	
	}

	/**
	 * Service behaviour.<br/>
	 * (<tt>run</tt> can be overridden if service call must take parameter)
	 */
	public abstract void serve();
}
