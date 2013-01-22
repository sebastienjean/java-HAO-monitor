package fr.iutvalence.ubpe.ubpecommons.services;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;

import fr.iutvalence.ubpe.core.interfaces.DataEvent;
import fr.iutvalence.ubpe.core.services.AbstractDataEventListenerService;

public class UBPETCPRelayClientDataEventListenerService extends AbstractDataEventListenerService
{
	private InetSocketAddress serverAdress;

	public UBPETCPRelayClientDataEventListenerService(InetSocketAddress serverAdress)
	{
		super(0);
		this.serverAdress = serverAdress;
	}

	@Override
	protected void onTakingEvent(DataEvent event)
	{
		//System.out.println("<UBPE2011-TCPrelay-client-service>: starting event processing");
		byte[] ubpeEventRawData = event.getRawData();

		String toSend = "<unknown>";

		try
		{
			toSend = (String) event.getMetadataFieldByName("metadata.reader.name").getValue();
		}
		catch (Exception e)
		{
		}
		toSend += "#UBPE#";
		try
		{
			toSend += new String(ubpeEventRawData, "US-ASCII");
		}
		catch (UnsupportedEncodingException e)
		{
			// Ignoring it
		}

		// try
		// {
		// toSend =
		// Base64.encodeBase64URLSafeString(toSend.getBytes("US-ASCII"));
		// }
		// catch (UnsupportedEncodingException e)
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// continue;
		// }

		Socket socket = null;
		try
		{
			socket = new Socket();
			socket.connect(this.serverAdress);
			PrintStream out = new PrintStream(socket.getOutputStream(), true, "US-ASCII");
			System.out.println("<UBPE-TCPrelay-client-service>: sending " + toSend);
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
				System.out.println("<UBPE-TCPrelay-client-service>: lost event in translation :-(");
			}
		}
		// Socket is closed by the server after reading line
		//System.out.println("<UBPE2011-TCPrelay-client-service>: ending event processing");
	}

}
