package fr.iutvalence.ubpe.core.services;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import fr.iutvalence.ubpe.core.interfaces.DataEvent;
import fr.iutvalence.ubpe.core.interfaces.DataEventListener;
import fr.iutvalence.ubpe.core.interfaces.Stoppable;

public abstract class AbstractDataEventListenerService implements Runnable, Stoppable, DataEventListener
{

	public final static int DEFAULT_QUEUE_CAPACITY = 1000;
	
	protected BlockingQueue<DataEvent> eventsQueue;

	private volatile boolean mustRun;

	// TODO add constructor with capacity
	public AbstractDataEventListenerService()
	{		
	
		this.eventsQueue = new ArrayBlockingQueue<DataEvent>(DEFAULT_QUEUE_CAPACITY);

		this.mustRun = true;
	}

	public void mustStop()
	{
		this.mustRun = false;
	}

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

	protected abstract void onTakingEvent(DataEvent event);
	
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
