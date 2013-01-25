package fr.iutvalence.ubpe.core.helpers;

import java.util.LinkedList;
import java.util.List;

import fr.iutvalence.ubpe.core.exceptions.ParsingException;
import fr.iutvalence.ubpe.core.interfaces.DataEvent;
import fr.iutvalence.ubpe.core.interfaces.DataEventListener;
import fr.iutvalence.ubpe.core.interfaces.DataEventParserForwarder;

public abstract class AbstractDataEventParserForwarder implements DataEventParserForwarder
{
	protected List<DataEventListener> listeners;

	public AbstractDataEventParserForwarder()
	{
		this.listeners = new LinkedList<DataEventListener>();
	}
	
	/**
	 * @see fr.iutvalence.ubpe.core.interfaces.DataEventForwarder#registerDataEventListener(fr.iutvalence.ubpe.core.interfaces.DataEventListener)
	 */
	@Override
	public void registerDataEventListener(DataEventListener listener)
	{
		this.listeners.add(listener);
	}

	/**
	 * @see fr.iutvalence.ubpe.core.interfaces.DataEventForwarder#unregisterDataEventListener(fr.iutvalence.ubpe.core.interfaces.DataEventListener)
	 */
	@Override
	public void unregisterDataEventListener(DataEventListener listener)
	{
		this.listeners.remove(listener);		
	}

	@Override
	public void forward(DataEvent event) 
	{
		for (DataEventListener listener:this.listeners)
			listener.process(event);
	}

	@Override
	public void parseAndForward(byte[] eventData) throws ParsingException 
	{
		this.forward(this.parse(eventData));	
	}
}
