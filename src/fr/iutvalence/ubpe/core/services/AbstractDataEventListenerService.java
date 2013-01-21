package fr.iutvalence.ubpe.core.services;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import fr.iutvalence.ubpe.core.interfaces.DataEvent;
import fr.iutvalence.ubpe.core.interfaces.DataEventListener;
import fr.iutvalence.ubpe.core.interfaces.Stoppable;

/**
 * Helper implementation of a data event processing thread.
 * Incoming events are processed asynchronously, using a queue.
 * 
 * @author sebastienjean
 *
 */
public abstract class AbstractDataEventListenerService implements Runnable, Stoppable, DataEventListener
{

	/**
	 * Default capacity of the event queue.
	 */
	public final static int DEFAULT_QUEUE_CAPACITY = 1000;
	
	/**
	 * Queue containing events to be processed. 
	 */
	protected BlockingQueue<DataEvent> eventsQueue;

	/**
	 * Boolean used to control termination.
	 */
	private volatile boolean mustRun;

	// TODO add constructor with capacity
	
	/**
	 * Creating a <tt>AbstractDataEventListenerService</tt> instance, with default queue capacity.
	 */
	public AbstractDataEventListenerService()
	{		
		this.eventsQueue = new ArrayBlockingQueue<DataEvent>(DEFAULT_QUEUE_CAPACITY);
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
	 * (event is put in event queue, then processed asynchronously)
	 *  
	 * @see fr.iutvalence.ubpe.core.interfaces.DataEventListener#process(fr.iutvalence.ubpe.core.interfaces.DataEvent)
	 */
	@Override
	public void process(DataEvent event)
	{
		try
		{
			this.eventsQueue.put(event);
		}
		catch (InterruptedException e)
		{
			// Event lost in translation !
		}
	}

	/**
	 * Defines event processing behaviour.
	 *  
	 * @param event event to be processed.
	 */
	protected abstract void onTakingEvent(DataEvent event);
	
	/**
	 * Asynchronous event processing
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		while (this.mustRun)
		{
			try
			{
				this.onTakingEvent(this.eventsQueue.take());
			}
			catch (InterruptedException e)
			{
				continue;
			}	
		}
	}
}
