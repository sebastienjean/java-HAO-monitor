import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import fr.iutvalence.ubpe.core.events.DefaultMetadataField;
import fr.iutvalence.ubpe.core.helpers.RawDataEventFileSystemStorage;
import fr.iutvalence.ubpe.core.helpers.Serial600InputStream;
import fr.iutvalence.ubpe.core.interfaces.MetadataField;
import fr.iutvalence.ubpe.core.services.DispatcherDataEventListenerForwarderService;
import fr.iutvalence.ubpe.core.services.SystemOutDebugDataEventListenerService;
import fr.iutvalence.ubpe.ubpecommons.services.UBPEInputStreamDataEventReaderService;
import fr.iutvalence.ubpe.ubpecommons.services.UBPETCPRelayClientDataEventListenerService;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

public class UBPESerialRadioRelayClientwithLocalStorage
{

	/**
	 * @param args
	 * @throws
	 */
	public static void main(String[] args)
	{
		// args[0] station name
		// args[1] serial port identifier
		// args[2] relay server IP
		// args[3] relay server port
		// args[4] object name

		// files are stored in current directory, under "station name" subfolder
		System.out.println("Trying to configure serial port "+args[1]+" ...");
		Serial600InputStream in = null;

		try
		{
			in = new Serial600InputStream(args[1]);
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
		
		System.out.println("Event debug output is on");
		SystemOutDebugDataEventListenerService debugListener = new SystemOutDebugDataEventListenerService();
		
		System.out.println("Trying to configure local storage service in "+args[0]+"-storage ...");
		RawDataEventFileSystemStorage storageListener = null;
		try
		{
			storageListener = new RawDataEventFileSystemStorage(args[0]+"-storage", new File(args[0]));
		}
		catch (FileNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(1);
		}
		System.out.println("... done");
		
		System.out.println("Trying to configure TCP relay service to "+args[2]+":"+args[3]+" ...");
		UBPETCPRelayClientDataEventListenerService relayClient = null;
		try
		{
			relayClient = new UBPETCPRelayClientDataEventListenerService(new InetSocketAddress(args[2], Integer.parseInt(args[3])));
		}
		catch (NumberFormatException e)
		{
			System.err.println("Specified server address (" + args[2] + "/"+args[3]+") is invalid, please check it before running this application again");
			System.exit(1);
		}
		System.out.println("... done");
		
		System.out.println("Creating event dispatcher service ...");
		DispatcherDataEventListenerForwarderService dispatcher = new DispatcherDataEventListenerForwarderService(new HashMap<String, MetadataField>());
		System.out.println("... done");
		
		System.out.println("Registering debug service as dispatcher listener ...");
		dispatcher.registerDataEventListener(debugListener);
		System.out.println("... done");
		
		System.out.println("Registering storage service as dispatcher listener ...");
		dispatcher.registerDataEventListener(storageListener);
		System.out.println("... done");
		
		System.out.println("Registering TCP relay service as dispatcher listener ...");
		dispatcher.registerDataEventListener(relayClient);
		System.out.println("... done");
		
		System.out.println("Creating event reader service for station named "+args[0]+" and object named "+args[4]+" ...");
		Map<String, MetadataField> readerMetadataFields = new HashMap<String, MetadataField>();
		readerMetadataFields.put("metadata.reader.name", new DefaultMetadataField("metadata.reader.name", String.class, args[0]));
		readerMetadataFields.put("metadata.object.name", new DefaultMetadataField("metadata.object.name", String.class, args[4]));
		UBPEInputStreamDataEventReaderService serialFrameReader = new UBPEInputStreamDataEventReaderService(in, readerMetadataFields);
		System.out.println("... done");
		
		System.out.println("Registering event dispatcher service as event reader listener ...");
		serialFrameReader.registerDataEventListener(dispatcher);
		System.out.println("... done");
		
		System.out.println("Starting TCP relay service ...");
		Thread relayThread = new Thread(relayClient);
		relayThread.start();
		System.out.println("... done");
		
		System.out.println("Starting debug service ...");
		Thread debugThread = new Thread(debugListener);
		debugThread.start();
		System.out.println("... done");
		
		System.out.println("Starting event dispatcher service ...");
		Thread dispatcherThread = new Thread(dispatcher);
		dispatcherThread.start();
		System.out.println("... done");
		
		System.out.println("Starting event reader service ...");
		Thread serialReaderThread = new Thread(serialFrameReader);
		serialReaderThread.start();
		System.out.println("... done");
		
		System.out.println("Initialization completed.");
	}
}
