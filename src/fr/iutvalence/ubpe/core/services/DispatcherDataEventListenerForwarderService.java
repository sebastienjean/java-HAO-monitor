package fr.iutvalence.ubpe.core.services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.iutvalence.ubpe.core.interfaces.DataEvent;
import fr.iutvalence.ubpe.core.interfaces.DataEventForwarder;
import fr.iutvalence.ubpe.core.interfaces.DataEventListener;
import fr.iutvalence.ubpe.core.interfaces.MetadataField;

public class DispatcherDataEventListenerForwarderService extends AbstractDataEventListenerService implements DataEventForwarder
{
	protected List<DataEventListener> listeners;

	protected Map<String, MetadataField> readerMetadataFields;

	public DispatcherDataEventListenerForwarderService(long period)
	{
		super(period);
		this.listeners = new LinkedList<DataEventListener>();
		this.readerMetadataFields = new HashMap<String, MetadataField>();
	}

	public DispatcherDataEventListenerForwarderService(long period, Map<String, MetadataField> readerMetadataFields)
	{
		super(period);
		this.listeners = new LinkedList<DataEventListener>();
		this.readerMetadataFields = readerMetadataFields;
		if (readerMetadataFields == null)
			this.readerMetadataFields = new HashMap<String, MetadataField>();
	}

	@Override
	public void registerDataEventListener(DataEventListener listener)
	{
		this.listeners.add(listener);
	}

	@Override
	public void unregisterDataEventListener(DataEventListener listener)
	{
		this.listeners.remove(listener);		
	}

	protected int getDataEventListenersSize()
	{
		return this.listeners.size();
	}

	@Override
	protected void onTakingEvent(DataEvent event)
	{
		System.out.println("<event-dispatcher-service>: starting event processing");
		for (DataEventListener listener: this.listeners)
			listener.process(event);
	    System.out.println("<event-dispatcher-service>: ending event processing");
	}
}
