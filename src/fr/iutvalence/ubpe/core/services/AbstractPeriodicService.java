package fr.iutvalence.ubpe.core.services;

/**
 * Helper implementation of a service.
 * 
 * 
 * @author sebastienjean
 * 
 */
public abstract class AbstractPeriodicService extends AbstractService
{

	/**
	 * Service's delay between two invocations.
	 */
	private final long period;

	/**
	 * Creating a <tt>AbstractPeriodicService</tt> instance, from a given
	 * period.
	 * 
	 * @param period
	 *            service period.
	 */
	public AbstractPeriodicService(long period)
	{
		this.period = period;
	}

	/**
	 * Returns service period.
	 * 
	 * @return service period
	 */
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
}
