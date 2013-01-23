package fr.iutvalence.ubpe.ubpe2012.services;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import fr.iutvalence.ubpe.core.interfaces.DataEventListener;
import fr.iutvalence.ubpe.core.services.AbstractService;

/**
 * 
 * Welcome part of the front-end server (Web part of the application).<br/>
 * <br/>
 * This TCP server reads data frames received from the HAO and relayed by stations.
 * 
 *   // TODO detail application protocol 
 * 
 */
public class UBPE2012WebFrontEndWelcomeService extends AbstractService
{
	/**
	 * Welcome socket, for local binding.
	 */
	private ServerSocket serverSocket;
	
	/**
	 * Data events listeners to be notified.
	 */
	private List<DataEventListener> listeners;


	/**
	 * Creates a new <tt>UBPE2012WebFrontEndWelcomeService</tt> instance, locally bound to a given address notifying to
	 * a given collection of listeners.
	 * @param address the local address where to bind
	 * @param listeners data event listeners to notify
	 * @throws IOException if server can not bind
	 */
	public UBPE2012WebFrontEndWelcomeService(InetSocketAddress address, List<DataEventListener> listeners) throws IOException
	{	
		this.serverSocket = new ServerSocket();
		this.serverSocket.bind(address);
		this.listeners = listeners;
	}

	public void run()
	{
		while (this.mustRun)
		{
			try
			{
				Socket sock = this.serverSocket.accept();
				System.out.println("<ubpe2012-TCPrelay-welcome-service> Incoming connection from "+sock.getRemoteSocketAddress());
				new Thread(new UBPE2012WebFrontEndDataEventParserForwarderService(sock, this.listeners)).start();
			}
			catch (IOException e)
			{
				System.err.println("<ubpe2012-TCPrelay-welcome-service> IOException while accepting connections");
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
