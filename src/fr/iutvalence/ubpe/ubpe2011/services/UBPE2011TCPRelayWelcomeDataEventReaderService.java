package fr.iutvalence.ubpe.ubpe2011.services;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import fr.iutvalence.ubpe.core.interfaces.DataEvent;
import fr.iutvalence.ubpe.core.interfaces.MetadataField;
import fr.iutvalence.ubpe.core.services.AbstractDataEventParserForwarderService;

/**
 * The HTTP Server
 * 
 */
public class UBPE2011TCPRelayWelcomeDataEventReaderService extends AbstractDataEventParserForwarderService
{
	private ServerSocket serverSocket;

	public UBPE2011TCPRelayWelcomeDataEventReaderService(InetSocketAddress address) throws IOException
	{
		super(new HashMap<String, MetadataField>());
		this.serverSocket = new ServerSocket();
		this.serverSocket.bind(address);
	}

	public void run()
	{
		while (this.mustRun)
		{
			try
			{
				Socket sock = this.serverSocket.accept();
				System.out.println("<ubpe2011-TCPrelay-welcome-service> Incoming connection from "+sock.getRemoteSocketAddress());
				new Thread(new UBPE2011TCPRelayWorkerDataEventReaderService(sock, this.listeners)).start();
			}
			catch (IOException e)
			{
				System.err.println("<ubpe2011-TCPrelay-welcome-service> IOException while accepting connections");
				continue;
			}
		}
	}

	@Override
	public DataEvent parse() 
	{ 
		return null;
	}

	@Override
	public void serve() 
	{	
		// Nothing to do here, it is a fake service
	}
}
