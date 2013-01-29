package fr.iutvalence.ubpe.commons.services;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;

import fr.iutvalence.ubpe.core.interfaces.DataEvent;
import fr.iutvalence.ubpe.core.services.AbstractDataEventListenerService;

public class WebFrontEndExporterDataEventListenerService extends AbstractDataEventListenerService
{
	private InetSocketAddress serverAdress;

	public WebFrontEndExporterDataEventListenerService(InetSocketAddress serverAdress)
	{
		this.serverAdress = serverAdress;
	}

	@Override
	protected void onTakingEvent(DataEvent event)
	{
		System.out.println("<WebFrontEndExporter-service>: starting event processing");
		byte[] ubpeEventRawData = event.getRawData();

		String readerName = "<unknown>";
		String eventType = "<unknown>";
		

		try
		{
			 readerName = (String) event.getMetadataFieldByName("metadata.reader.name").getValue();
		}
		catch (Exception e)
		{
			// Ignoring it, let readerName be <unknown>
		}
		
		try
		{
			 eventType = (String) event.getMetadataFieldByName("metadata.event.type").getValue();
		}
		catch (Exception e)
		{
			// Ignoring it, let eventType be <unknown>
		}
		
		String toSend = eventType+"@"+readerName+"#";
		try
		{
			toSend += new String(ubpeEventRawData, "US-ASCII");
		}
		catch (UnsupportedEncodingException e)
		{
			// Ignoring it
		}
		
		Socket socket = null;
		try
		{
			socket = new Socket();
			socket.connect(this.serverAdress);
			PrintStream out = new PrintStream(socket.getOutputStream(), true, "US-ASCII");
			System.out.println("<WebFrontEndExporter-service>: sending " + toSend);
			out.println(toSend);
			out.flush();
		}
		catch (UnsupportedEncodingException e)
		{
			// ignoring it
		}
		catch (IOException e)
		{
			try
			{
				this.eventsQueue.put(event);
			}
			catch (InterruptedException e1)
			{
				System.out.println("<WebFrontEndExporter-service>: lost event in translation :-(");
			}
		}
		// Socket is closed by the server after reading line
		System.out.println("<WebFrontEndExporter-service>: ending event processing");
	}

}
