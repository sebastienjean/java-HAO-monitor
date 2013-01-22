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
	 * Service period, in milliseconds.
	 */
	protected final long period;
	
	/**
	 * Boolean used to control termination.
	 */
	protected volatile boolean mustRun;
	
	/**
	 * Creating a <tt>AbstractService</tt> instance, qith a given period.
	 * @param period service period.
	 */
	public AbstractService(long period)
	{	
		this.period = period;
		this.mustRun = true;
	}

	/**
	 * @see fr.iutvalence.ubpe.core.interfaces.Stoppable#mustStop()
	 */
	public void mustStop()
	{
		this.mustRun = false;
	}

	
	public long getPeriod() 
	{
		return this.period;
	}

	/**
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		while (this.mustRun)
		{
			this.serve();
			try
			{
				Thread.sleep(this.period);
			}
			catch (InterruptedException e)
			{
				continue;
			}
		}	
	}

	/**
	 * Service behaviour.<br/>
	 * (<tt>run</tt> can be overridden if service call must take parameter)
	 */
	public abstract void serve();
}
