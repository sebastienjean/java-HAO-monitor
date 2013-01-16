import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

import fr.iutvalence.ubpe.ubpe2011.services.UBPE2011JsonProducerDataEventListenerService;
import fr.iutvalence.ubpe.ubpe2011.services.UBPE2011TCPRelayWelcomeDataEventReaderService;
import fr.iutvalence.ubpe.ubpecommons.services.JSonFilteredFilesProducerService;

public class UBPE2011RelayServerMain
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{	
		// args[0] server's IP (for incoming frames)
		// args[1] server's port (for incoming frames)

		// TODO checking args, console usage
		
		System.out.println("Creating ubpe2011 JSON producer service ...");
		UBPE2011JsonProducerDataEventListenerService runnableWebJsonListener = new UBPE2011JsonProducerDataEventListenerService(new File(args[2]), "UTF-8");
		System.out.println("... done");
		
		System.out.println("Creating ubpe2011 TCP Relay welcome service ...");
		InetSocketAddress address = new InetSocketAddress(args[0], Integer.parseInt(args[1]));		
		UBPE2011TCPRelayWelcomeDataEventReaderService server = null;
		try
		{
			server = new UBPE2011TCPRelayWelcomeDataEventReaderService(address);
		}
		catch (IOException e)
		{
			System.err.println("Specified server address (" + args[0] + "/"+args[1]+") is invalid, please check it before running this application again");
			System.exit(1);
		}
		System.out.println("... done");
		
		System.out.println("Registering ubpe2011 JSON producer service as welcome listener ...");
		server.registerDataEventListener(runnableWebJsonListener);
		System.out.println("... done");
		
		System.out.println("Starting ubpe JSON filtered producer service ...");
		Thread jsonFilteredThread = new Thread(new JSonFilteredFilesProducerService(new File(args[2]+".json")));
		jsonFilteredThread.start();
		System.out.println("... done");		
		
		System.out.println("Starting ubpe2011 JSON producer service ...");
		Thread jsonThread = new Thread(runnableWebJsonListener);
		jsonThread.start();
		System.out.println("... done");
		
		System.out.println("Starting ubpe2011 TCP Relay welcome service ...");
		Thread serverThread = new Thread(server);
		serverThread.start();
		System.out.println("... done");
		
		System.out.println("Initialization completed.");
	}
}
