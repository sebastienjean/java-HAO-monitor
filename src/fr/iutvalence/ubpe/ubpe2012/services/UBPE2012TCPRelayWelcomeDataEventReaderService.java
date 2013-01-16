package fr.iutvalence.ubpe.ubpe2012.services;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import fr.iutvalence.ubpe.core.interfaces.MetadataField;
import fr.iutvalence.ubpe.core.services.AbstractDataEventForwarderParserService;

/**
 * The HTTP Server
 * 
 */
public class UBPE2012TCPRelayWelcomeDataEventReaderService extends AbstractDataEventForwarderParserService
{
	private ServerSocket serverSocket;

	public UBPE2012TCPRelayWelcomeDataEventReaderService(InetSocketAddress address) throws IOException
	{
		super(new HashMap<String, MetadataField>());
		this.mustRun = true;
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
				System.out.println("<ubpe2012-TCPrelay-welcome-service> Incoming connection from "+sock.getRemoteSocketAddress());
				new Thread(new UBPE2012TCPRelayWorkerDataEventReaderService(sock, this.listeners)).start();
			}
			catch (IOException e)
			{
				System.err.println("<ubpe2012-TCPrelay-welcome-service> IOException while accepting connections");
				continue;
			}
		}
	}
}
