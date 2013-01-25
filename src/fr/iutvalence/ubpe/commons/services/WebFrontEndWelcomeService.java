package fr.iutvalence.ubpe.commons.services;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import fr.iutvalence.ubpe.core.interfaces.DataEventParserForwarder;
import fr.iutvalence.ubpe.core.services.AbstractService;

/**
 * 
 * Welcome part of a front-end server (Web part of the application).<br/>
 * <br/>
 * This TCP server reads data frames received from the HAO and relayed by stations.<br/>
 * 
 * Application protocol is pretty simple:
 * <ul>
 * <li>
 * 	Communication model is simplex (client to server only)
 * </li>
 * <li>
 * 	The client sends a unique line of ASCII-encoded text, formatted as follows
 * 	<br>/
 * 	<tt>eventType@clientName#eventData</tt> 
 *  <br/>
 * </li>
 * </ul> 
 * 
 */
public class WebFrontEndWelcomeService extends AbstractService
{
	/**
	 * Welcome socket, for local binding.
	 */
	private final ServerSocket serverSocket;
	
	/**
	 * Data events parsers (value) to be used, for each event type supported (key).
	 */
	private final Map<String, DataEventParserForwarder> parsers;
	

	/**
	 * Creates a new <tt>WebFrontEndWelcomeService</tt> instance, locally bound to a given address, notifying to
	 * a given collection of listeners and using a given collection of parsers (depending of event types)
	 * @param address the local address where to bind 
	 * @param parsers data event parsers to be used, for each supported event type.
	 * @throws IOException if server can not bind
	 */
	public WebFrontEndWelcomeService(InetSocketAddress address, Map<String, DataEventParserForwarder> parsers) throws IOException 
	{
		this.serverSocket = new ServerSocket();
		this.serverSocket.bind(address);
		this.parsers = parsers;
	}

	public void run()
	{
		while (this.mustRun)
		{
			try
			{
				Socket sock = this.serverSocket.accept();
				System.out.println("<WebFrontEndWelcome-service> Incoming connection from "+sock.getRemoteSocketAddress());
				new Thread(new WebFrontEndWorkerThread(sock, this.parsers)).start();
			}
			catch (IOException e)
			{
				System.err.println("<WebFrontEndWelcome-service> IOException while accepting connections");
				continue;
			}
		}
	}

	@Override
	public void serve() 
	{
		// Nothing to do here, it is a fake service. The service paradigm is only used 
		// here to be able to stop properly
	}
}
