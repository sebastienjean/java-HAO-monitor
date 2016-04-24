package fr.iutvalence.hao.flight.ubpe2016.main;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import fr.iutvalence.hao.flight.ubpe2016.services.UBPE2016JsonProducerDataEventListenerService;
import fr.iutvalence.hao.monitor.commons.services.JsonFileTokensRemoverPeriodicService;
import fr.iutvalence.hao.monitor.commons.services.WebFrontEndWelcomeService;
import fr.iutvalence.hao.monitor.core.helpers.UBPEDataEventParserForwarder;
import fr.iutvalence.hao.monitor.core.interfaces.DataEventParserForwarder;

public class UBPE2016WebFrontEndLauncher
{
	/**
	 * Application's main.
	 * 
	 * @param args
	 *            command-line arguments<br/>
	 *            <ul>
	 *            <li> <tt>args[0]</tt> server's IP (for incoming frames)</li>
	 *            <li> <tt>args[1]</tt> server's port (for incoming frames)</li>
	 *            <li> <tt>args[2]</tt> output JSON file path</li>
	 *            <li> <tt>args[3]</tt> refresh delay (in seconds)</li>
	 *            </ul>
	 */
	public static void main(String[] args)
	{
		// args[0] server's IP (for incoming frames)
		// args[1] server's port (for incoming frames)
		// args[2] output JSON file path
		// args[3] refresh delay (in seconds)

		if (args.length != 4)
		{
			System.err.println("Missing arguments, exiting...");
			// System.err.println("(expected IP and port for local binding)");
			System.exit(1);
		}

		System.out.println("Creating and registering ubpe2016 event parser ...");
		UBPEDataEventParserForwarder ubpe2016Parser = new UBPEDataEventParserForwarder(fr.iutvalence.hao.flight.ubpe2016.data.UBPE2016DataEvent.class, "UBPE2016");
		Map<String, DataEventParserForwarder> parsers = new HashMap<String, DataEventParserForwarder>();
		parsers.put("UBPE2016", ubpe2016Parser);
		System.out.println("... done");

		System.out.println("Starting JSON token remover periodic service ...");
		new Thread(new JsonFileTokensRemoverPeriodicService(Integer.parseInt(args[3])*1000, new File(args[2] + ".json"), "UTF-8", new File(args[2] + ".clean"),
				new String[] { "<!-- @@EVENT@@ -->" })).start();
		System.out.println("... done");

		System.out.println("Creating ubpe2016 JSON producer service ...");
		UBPE2016JsonProducerDataEventListenerService runnableWebJsonListener = new UBPE2016JsonProducerDataEventListenerService(new File(args[2]), "UTF-8");
		System.out.println("... done");

		System.out.println("Registering ubpe2016 JSON producer service as a parser listener ...");
		ubpe2016Parser.registerDataEventListener(runnableWebJsonListener);
		System.out.println("... done");

		System.out.println("Starting ubpe2016 JSON producer service ...");
		new Thread(runnableWebJsonListener).start();
		System.out.println("... done");

		System.out.println("Starting Web FrontEnd welcome service ...");
		InetSocketAddress address = new InetSocketAddress(args[0], Integer.parseInt(args[1]));
		WebFrontEndWelcomeService server = null;
		try
		{
			server = new WebFrontEndWelcomeService(address, parsers);
			new Thread(server).start();
		}
		catch (IOException e)
		{
			System.err.println("Specified server address (" + args[0] + "/" + args[1] + ") is invalid, please check it before running this application again");
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("... done");

		System.out.println("Initialization completed.");
	}
}
