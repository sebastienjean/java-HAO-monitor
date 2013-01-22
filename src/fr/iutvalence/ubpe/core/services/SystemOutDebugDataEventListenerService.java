package fr.iutvalence.ubpe.core.services;

import fr.iutvalence.ubpe.core.interfaces.DataEvent;

public class SystemOutDebugDataEventListenerService extends AbstractDataEventListenerService
{
	public SystemOutDebugDataEventListenerService() 
	{
		super(0);
	}

	@Override
	protected void onTakingEvent(DataEvent event)
	{	
		System.out.println("<debug-service>: starting event processing");
		System.out.println("<---------DEBUG--------->");
		System.out.println(event);
		System.out.println("<---------DEBUG--------->");
		System.out.println("<debug-service>: ending event processing");
	}
}
