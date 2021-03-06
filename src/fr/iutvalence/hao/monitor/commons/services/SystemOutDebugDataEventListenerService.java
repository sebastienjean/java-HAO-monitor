package fr.iutvalence.hao.monitor.commons.services;

import fr.iutvalence.hao.monitor.core.interfaces.DataEvent;
import fr.iutvalence.hao.monitor.core.services.AbstractDataEventListenerService;

public class SystemOutDebugDataEventListenerService extends AbstractDataEventListenerService
{
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
