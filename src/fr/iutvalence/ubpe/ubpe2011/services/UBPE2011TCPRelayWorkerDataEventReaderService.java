package fr.iutvalence.ubpe.ubpe2011.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.iutvalence.ubpe.core.events.DefaultMetadataField;
import fr.iutvalence.ubpe.core.interfaces.DataEvent;
import fr.iutvalence.ubpe.core.interfaces.DataEventListener;
import fr.iutvalence.ubpe.core.interfaces.MetadataField;
import fr.iutvalence.ubpe.core.services.AbstractDataEventParserForwarderService;
import fr.iutvalence.ubpe.ubpe2011.UBPE2011DataEvent;

public class UBPE2011TCPRelayWorkerDataEventReaderService extends AbstractDataEventParserForwarderService
{
	private Socket socket;

	public UBPE2011TCPRelayWorkerDataEventReaderService(Socket socket, List<DataEventListener> listeners)
	{
		// TODO verify that metadata are cloned
		super(new HashMap<String, MetadataField>());

		this.socket = socket;
		
		for (DataEventListener listener : listeners)
			this.registerDataEventListener(listener);
	}

	@Override
	public DataEvent parse()
	{
		System.out.println("<UBPE2011-TCPrelay-worker-"+this.hashCode()+">: starting event parsing");
		BufferedReader in = null;
		try
		{
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "US-ASCII"));
			String line = in.readLine();
			if (line == null)
			{
				throw new IOException();
			}
			System.out.println("<UBPE2011-TCPrelay-worker-"+this.hashCode()+">: received "+line);			
			// String data = new String(Base64.decodeBase64(line), "US-ASCII");
			String[] tokens = line.split("#");
			if (tokens.length != 3)
				throw new IOException();
			Map<String, MetadataField> eventMetadataFields = new HashMap<String, MetadataField>();
			if (!(tokens[1].equals("UBPE")))
				throw new IOException();
			eventMetadataFields.putAll(this.readerMetadataFields);
			eventMetadataFields.put("metadata.reader.timestamp", new DefaultMetadataField("metadata.reader.timestamp", long.class, System.currentTimeMillis()));
			eventMetadataFields.put("metadata.reader.name", new DefaultMetadataField("metadata.reader.name", String.class, tokens[0]));
			this.mustStop();
			try
			{
				this.socket.close();
			}
			catch (IOException e1)
			{
			}
			System.out.println("<UBPE2011-TCPrelay-worker-"+this.hashCode()+">: ending event parsing (success)");
			return new UBPE2011DataEvent(tokens[2].getBytes("US-ASCII"), eventMetadataFields);

		}
		catch (Exception e)
		{			
			try
			{
				this.socket.close();
			}
			catch (IOException e1)
			{
			}
			this.mustStop();
			System.out.println("<UBPE2011-TCPrelay-worker-"+this.hashCode()+">: ending event parsing (failure)");
			return null;
		}
	}

	@Override
	public void serve() {
		// TODO Auto-generated method stub
		
	}
}
