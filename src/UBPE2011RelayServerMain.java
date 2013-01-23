import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

import fr.iutvalence.ubpe.ubpe2011.services.UBPE2011JsonProducerDataEventListenerService;
import fr.iutvalence.ubpe.ubpe2011.services.UBPE2011TCPRelayWelcomeDataEventReaderService;
import fr.iutvalence.ubpe.ubpecommons.services.JsonFileTokensRemoverPeriodicService;
import fr.iutvalence.ubpe.ubpecommons.services.JsonFilteredFileProducerService;

public class UBPE2011RelayServerMain
{

	/**
	 * Application's main.
	 * 
	 * @param args command-line arguments<br/>
	 * <ul>
	 * <li> <tt>args[0]</tt> server's IP (for incoming frames)</li>
	 * <li> <tt>args[1]</tt> server's port (for incoming frames)</li>
	 * <li> <tt>args[2]</tt> output JSON file path</li>
	 * </ul>
	 */
	public static void main(String[] args)
	{	
		// args[0] server's IP (for incoming frames)
		// args[1] server's port (for incoming frames)
		// args[2] output JSON file path
		
		if (args.length != 3)
		{
			System.err.println("Missing arguments, exiting...");
			System.err.println("(expected IP and port for local binding)");
			System.exit(1);
		}
		
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
		
		System.out.println("Starting ubpe2011 JSON token remover service ...");
		Thread jsonTokenRemoverThread = new Thread(new JsonFileTokensRemoverPeriodicService(30000, new File(args[2]+".json"), "UTF-8", new File(args[2]+".clean"), new String[] {"//@@EVENT@@//"}));
		jsonTokenRemoverThread.start();
		System.out.println("... done");		
		
		System.out.println("Starting ubpe2011 JSON filtered producer service ...");
		Thread jsonFilteredThread = new Thread(new JsonFilteredFileProducerService(30000, new File(args[2]+".clean"), "UTF-8", new File("global_analogTempVersusTime.json"), new int[] {1, 3, 15}));
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
