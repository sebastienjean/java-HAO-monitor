package fr.iutvalence.ubpe.commons.services;

import fr.iutvalence.ubpe.core.helpers.RawDataEventFileSystemStorage;
import fr.iutvalence.ubpe.core.interfaces.DataEvent;
import fr.iutvalence.ubpe.core.services.AbstractDataEventListenerService;

public class RawFileSystemStorageDataEventListenerService extends AbstractDataEventListenerService
{
	private final RawDataEventFileSystemStorage storage;

	public RawFileSystemStorageDataEventListenerService(RawDataEventFileSystemStorage storage)
	{
		super();
		this.storage = storage;
	}

	@Override
	protected void onTakingEvent(DataEvent event)
	{
		System.out.println("<RawDataEventFileSystemStorage-service>: starting event processing");
		this.storage.process(event);
		System.out.println("<RawDataEventFileSystemStorage-service>: ending event processing");
	}
}
