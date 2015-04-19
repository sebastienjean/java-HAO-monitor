package fr.iutvalence.ubpe.main;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import fr.iutvalence.hao.monitor.commons.services.JsonFileTokensRemoverPeriodicService;
import fr.iutvalence.hao.monitor.commons.services.JsonFilteredFileProducerService;
import fr.iutvalence.hao.monitor.commons.services.WebFrontEndWelcomeService;
import fr.iutvalence.hao.monitor.core.helpers.UBPEDataEventParserForwarder;
import fr.iutvalence.hao.monitor.core.interfaces.DataEventParserForwarder;
import fr.iutvalence.ubpe.ubpe2012.services.UBPE2012JsonProducerDataEventListenerService;

public class UBPE2011WebFrontEndLauncher
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
	 *            </ul>
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

		System.out.println("Creating and registering ubpe2011 event parser ...");
		UBPEDataEventParserForwarder ubpe2011Parser = new UBPEDataEventParserForwarder(fr.iutvalence.ubpe.ubpe2011.UBPE2011DataEvent.class, "UBPE2011");
		Map<String, DataEventParserForwarder> parsers = new HashMap<String, DataEventParserForwarder>();
		parsers.put("UBPE2011", ubpe2011Parser);
		System.out.println("... done");

		System.out.println("Starting JSON token remover periodic service ...");
		new Thread(new JsonFileTokensRemoverPeriodicService(30000, new File(args[2] + ".json"), "UTF-8", new File(args[2] + ".clean"),
				new String[] { "<!-- @@EVENT@@ -->" })).start();
		System.out.println("... done");

		System.out.println("Starting JSON filtered producer periodic service ...");
		new Thread(new JsonFilteredFileProducerService(30000, new File(args[2] + ".clean"), "UTF-8", new File("global_analogTempVersusTime.json"), new int[] {
				1, 3, 15 })).start();
		System.out.println("... done");

		System.out.println("Creating ubpe2011 JSON producer service ...");
		UBPE2012JsonProducerDataEventListenerService runnableWebJsonListener = new UBPE2012JsonProducerDataEventListenerService(new File(args[2]), "UTF-8");
		System.out.println("... done");

		System.out.println("Registering ubpe2011 JSON producer service as a parser listener ...");
		ubpe2011Parser.registerDataEventListener(runnableWebJsonListener);
		System.out.println("... done");

		System.out.println("Starting ubpe2011 JSON producer service ...");
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
			System.exit(1);
		}

		System.out.println("... done");

		System.out.println("Initialization completed.");
	}
}