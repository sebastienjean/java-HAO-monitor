package fr.iutvalence.hao.monitor.commons.services;

import fr.iutvalence.hao.monitor.core.helpers.RawDataEventFileSystemStorage;
import fr.iutvalence.hao.monitor.core.interfaces.DataEvent;
import fr.iutvalence.hao.monitor.core.services.AbstractDataEventListenerService;

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
