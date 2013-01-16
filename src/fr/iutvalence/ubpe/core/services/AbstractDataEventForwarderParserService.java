package fr.iutvalence.ubpe.core.services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.iutvalence.ubpe.core.events.DefaultMetadataField;
import fr.iutvalence.ubpe.core.interfaces.DataEvent;
import fr.iutvalence.ubpe.core.interfaces.DataEventForwarder;
import fr.iutvalence.ubpe.core.interfaces.DataEventListener;
import fr.iutvalence.ubpe.core.interfaces.MetadataField;
import fr.iutvalence.ubpe.core.interfaces.Stoppable;

public abstract class AbstractDataEventForwarderParserService implements Runnable, Stoppable, DataEventForwarder
{
	protected List<DataEventListener> listeners;

	protected Map<String, MetadataField> readerMetadataFields;

	protected volatile boolean mustRun;

	public AbstractDataEventForwarderParserService(Map<String, MetadataField> readerMetadataFields)
	{
		this.listeners = new LinkedList<DataEventListener>();
		this.readerMetadataFields = readerMetadataFields;
		if (readerMetadataFields == null)
			this.readerMetadataFields = new HashMap<String, MetadataField>();
		this.mustRun = true;
	}

	public void mustStop()
	{
		this.mustRun = false;
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
	
	public DataEvent parse()
	{
		return null;
		
	}
	
	public void run()
	{
		while (this.mustRun)
		{
			DataEvent event = this.parse();

			if (event == null)
			{				
				continue;
			}
			
			event.addMetadataField(new DefaultMetadataField("metadata.reader.timestamp", long.class, System.currentTimeMillis()));
			for (MetadataField metadataField : this.readerMetadataFields.values())
				event.addMetadataField(metadataField);

			for (DataEventListener listener : this.listeners)
				listener.process(event);
		}		
	}
}
