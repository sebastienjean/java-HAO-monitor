package fr.iutvalence.ubpe.core.services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.iutvalence.ubpe.core.events.DefaultMetadataField;
import fr.iutvalence.ubpe.core.interfaces.DataEvent;
import fr.iutvalence.ubpe.core.interfaces.DataEventForwarder;
import fr.iutvalence.ubpe.core.interfaces.DataEventListener;
import fr.iutvalence.ubpe.core.interfaces.DataEventParser;
import fr.iutvalence.ubpe.core.interfaces.MetadataField;

public abstract class AbstractDataEventParserForwarderService extends AbstractService implements DataEventParser, DataEventForwarder
{
	protected List<DataEventListener> listeners;

	protected Map<String, MetadataField> readerMetadataFields;

	public AbstractDataEventParserForwarderService(Map<String, MetadataField> readerMetadataFields)
	{
		super(0);
		this.listeners = new LinkedList<DataEventListener>();
		this.readerMetadataFields = readerMetadataFields;
		if (readerMetadataFields == null)
			this.readerMetadataFields = new HashMap<String, MetadataField>();
		this.mustRun = true;
	}

	public abstract DataEvent parse();

	/**
	 * @see fr.iutvalence.ubpe.core.services.AbstractService#run()
	 */
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
}
