package fr.iutvalence.hao.monitor.core.services;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import fr.iutvalence.hao.monitor.core.interfaces.DataEvent;
import fr.iutvalence.hao.monitor.core.interfaces.DataEventListener;

/**
 * Helper implementation of a data event processing service. Incoming events are
 * processed asynchronously, using a queue.
 * 
 * @author sebastienjean
 * 
 */
public abstract class AbstractDataEventListenerService extends AbstractService implements DataEventListener
{
	/**
	 * Default capacity of the event queue.
	 */
	public final static int DEFAULT_QUEUE_CAPACITY = 1000;

	/**
	 * Queue containing events to be processed.
	 */
	protected BlockingQueue<DataEvent> eventsQueue;

	// TODO add constructor with capacity

	/**
	 * Creating a <tt>AbstractDataEventListenerService</tt> instance, with
	 * default queue capacity.
	 */
	public AbstractDataEventListenerService()
	{
		this.eventsQueue = new ArrayBlockingQueue<DataEvent>(DEFAULT_QUEUE_CAPACITY);
	}

	/**
	 * (event is put in event queue, then processed asynchronously)
	 * 
	 * @see fr.iutvalence.hao.monitor.core.interfaces.DataEventListener#process(fr.iutvalence.hao.monitor.core.interfaces.DataEvent)
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
	 * @param event
	 *            event to be processed.
	 */
	protected abstract void onTakingEvent(DataEvent event);

	@Override
	public void serve()
	{
		try
		{
			this.onTakingEvent(this.eventsQueue.take());
		}
		catch (InterruptedException e)
		{
			// Ignore it
		}
	}

}
