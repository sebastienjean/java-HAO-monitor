package fr.iutvalence.hao.flight.ubpe2017.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import fr.iutvalence.hao.monitor.commons.services.RawFileSystemStorageDataEventListenerService;
import fr.iutvalence.hao.monitor.commons.services.SystemOutDebugDataEventListenerService;
import fr.iutvalence.hao.monitor.commons.services.UBPEInputStreamDataEventReaderService;
import fr.iutvalence.hao.monitor.commons.services.WebFrontEndExporterDataEventListenerService;
import fr.iutvalence.hao.monitor.core.helpers.AsciiDumpFileReplayInputStream;
import fr.iutvalence.hao.monitor.core.helpers.RawDataEventFileSystemStorage;
import fr.iutvalence.hao.monitor.core.helpers.UBPEDataEventParserForwarder;
import fr.iutvalence.hao.monitor.core.interfaces.DataEventParserForwarder;

public class UBPE2017ReplayRelayClientwithLocalStorage
{

	/**
	 * @param args
	 * @throws
	 */
	public static void main(String[] args)
	{
		// args[0] station name
		// args[1] replay file path
		// args[2] relay server IP
		// args[3] relay server port

		if (args.length != 4)
		{
			System.err.println("Missing arguments, exiting...");
			// System.err.println("(expected IP and port for local binding)");
			System.exit(1);
		}

		// files are stored in current directory, under "station name" subfolder
		System.out.println("Opening replay file (" + args[1] + ") ...");
		AsciiDumpFileReplayInputStream in = null;

		try
		{
			in = new AsciiDumpFileReplayInputStream(new File(args[1]), "US-ASCII", 2000);
		}
		catch (IOException e)
		{
			System.err.println("Replay file not found");
			System.exit(1);
		}
		new Thread(in).start();
		System.out.println("... done");

		System.out.println("Creating and registering ubpe2017 event parser ...");
		UBPEDataEventParserForwarder ubpe2017Parser = new UBPEDataEventParserForwarder(fr.iutvalence.hao.flight.ubpe2017.data.UBPE2017DataEvent.class, "UBPE2017");
		Map<String, DataEventParserForwarder> parsers = new HashMap<String, DataEventParserForwarder>();
		parsers.put("UBPE2017", ubpe2017Parser);
		System.out.println("... done");

		System.out.println("Creating console debug service ...");
		SystemOutDebugDataEventListenerService debugService = new SystemOutDebugDataEventListenerService();
		System.out.println("... done");

		System.out.println("Creating raw filesystem storage service ...");
		RawDataEventFileSystemStorage storage = null;
		RawFileSystemStorageDataEventListenerService storageService = null;
		try
		{
			storage = new RawDataEventFileSystemStorage(args[0] + "-storage", new File(args[0]));
			storageService = new RawFileSystemStorageDataEventListenerService(storage);
		}
		catch (FileNotFoundException e1)
		{
			System.err.println("Unable to create subdir in working directory, check permissions");
			System.exit(1);
		}
		System.out.println("... done");

		System.out.println("Creating Web frontend exporter service ...");
		WebFrontEndExporterDataEventListenerService exporterService = new WebFrontEndExporterDataEventListenerService(new InetSocketAddress(args[2],
				Integer.parseInt(args[3])));
		System.out.println("... done");

		System.out.println("Registering console debug service as a parser listener ...");
		ubpe2017Parser.registerDataEventListener(debugService);
		System.out.println("... done");

		System.out.println("Registering raw filesystem storage service as a parser listener ...");
		ubpe2017Parser.registerDataEventListener(storageService);
		System.out.println("... done");

		System.out.println("Registering Web frontend exporter service as a parser listener ...");
		ubpe2017Parser.registerDataEventListener(exporterService);
		System.out.println("... done");

		System.out.println("Starting console debug service ...");
		new Thread(debugService).start();
		System.out.println("... done");

		System.out.println("Starting raw filesystem storage service ...");
		new Thread(storageService).start();
		System.out.println("... done");

		System.out.println("Starting Web frontend exporter service ...");
		new Thread(exporterService).start();
		System.out.println("... done");

		System.out.println("Starting replay event reader service ...");
		UBPEInputStreamDataEventReaderService readerService = new UBPEInputStreamDataEventReaderService(in, parsers, "UBPE2017", args[0]);
		new Thread(readerService).start();
		System.out.println("... done");

		System.out.println("Initialization completed.");
	}
}
