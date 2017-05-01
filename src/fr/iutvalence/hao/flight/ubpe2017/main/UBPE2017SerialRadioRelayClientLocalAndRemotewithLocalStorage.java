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
import fr.iutvalence.hao.monitor.core.helpers.RawDataEventFileSystemStorage;
import fr.iutvalence.hao.monitor.core.helpers.SerialInputStream;
import fr.iutvalence.hao.monitor.core.helpers.UBPEDataEventParserForwarder;
import fr.iutvalence.hao.monitor.core.interfaces.DataEventParserForwarder;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

public class UBPE2017SerialRadioRelayClientLocalAndRemotewithLocalStorage
{

	/**
	 * @param args
	 * @throws
	 */
	public static void main(String[] args)
	{
		// args[0] station name
		// args[1] serial port identifier
		// args[2] baudrate
		// args[3] relay server IP
		// args[4] relay server port

		if (args.length != 5)
		{
			System.err.println("Missing arguments, exiting...");
			// System.err.println("(expected IP and port for local binding)");
			System.exit(1);
		}

		// files are stored in current directory, under "station name" subfolder
		System.out.println("Trying to configure serial port " + args[1] + " ...");
		SerialInputStream in = null;

		try
		{
			in = new SerialInputStream(args[1], Integer.parseInt(args[2]));
		}
		catch (PortInUseException e)
		{
			System.err.println("Serial port is already in use, please close it before running this application again");
			System.exit(1);
		}
		catch (NoSuchPortException e)
		{
			System.err.println("Specified port (" + args[1] + ") does not exist, please check serial port name before running this application again");
			System.exit(1);
		}
		catch (UnsupportedCommOperationException e)
		{
			System.err.println("Specified port (" + args[1] + ") can not be configured properly, please check it before running this application again");
			System.exit(1);
		}
		catch (IOException e)
		{
			System.err.println("Unable to read from specified port (" + args[1] + "), please check it before running this application again");
			System.exit(1);
		}
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

		System.out.println("Creating remote Web frontend exporter service ...");
		WebFrontEndExporterDataEventListenerService remoteExporterService = new WebFrontEndExporterDataEventListenerService(new InetSocketAddress(args[3],
				Integer.parseInt(args[4])));
		System.out.println("... done");
		
		System.out.println("Creating local Web frontend exporter service ...");
		WebFrontEndExporterDataEventListenerService localExporterService = new WebFrontEndExporterDataEventListenerService(new InetSocketAddress("127.0.0.1", Integer.parseInt(args[4])));
		System.out.println("... done");

		System.out.println("Registering console debug service as a parser listener ...");
		ubpe2017Parser.registerDataEventListener(debugService);
		System.out.println("... done");

		System.out.println("Registering raw filesystem storage service as a parser listener ...");
		ubpe2017Parser.registerDataEventListener(storageService);
		System.out.println("... done");

		System.out.println("Registering remote Web frontend exporter service as a parser listener ...");
		ubpe2017Parser.registerDataEventListener(remoteExporterService);
		System.out.println("... done");
		
		System.out.println("Registering local Web frontend exporter service as a parser listener ...");
		ubpe2017Parser.registerDataEventListener(localExporterService);
		System.out.println("... done");

		System.out.println("Starting console debug service ...");
		new Thread(debugService).start();
		System.out.println("... done");

		System.out.println("Starting raw filesystem storage service ...");
		new Thread(storageService).start();
		System.out.println("... done");

		System.out.println("Starting remote Web frontend exporter service ...");
		new Thread(remoteExporterService).start();
		System.out.println("... done");
		
		System.out.println("Starting local Web frontend exporter service ...");
		new Thread(localExporterService).start();
		System.out.println("... done");

		System.out.println("Starting serial event reader service ...");
		UBPEInputStreamDataEventReaderService readerService = new UBPEInputStreamDataEventReaderService(in, parsers, "UBPE2017", args[0]);
		new Thread(readerService).start();
		System.out.println("... done");

		System.out.println("Initialization completed.");
	}
}
