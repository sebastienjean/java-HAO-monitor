package fr.iutvalence.ubpe.ubpecommons.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import fr.iutvalence.ubpe.core.events.DefaultMetadataField;
import fr.iutvalence.ubpe.core.interfaces.DataEvent;
import fr.iutvalence.ubpe.core.interfaces.MetadataField;
import fr.iutvalence.ubpe.core.services.AbstractDataEventParserForwarderService;
import fr.iutvalence.ubpe.ubpe2011.UBPE2011DataEvent;

public class UBPEInputStreamDataEventReaderService extends AbstractDataEventParserForwarderService
{
	public final static int UBPE_START_FRAME_FLAG = (byte) 0x23; // # character
	
	private InputStream in;

	private OutputStream out;

	public UBPEInputStreamDataEventReaderService(InputStream inStream, Map<String, MetadataField> readerMetadataFields)
	{
		super(readerMetadataFields);
		this.in = inStream;
		this.out = null;
	}

	public UBPEInputStreamDataEventReaderService(InputStream inStream, OutputStream outStream, Map<String, MetadataField> readerMetadataFields)
	{
		super(readerMetadataFields);
		this.in = inStream;
		this.out = outStream;
	}

	@Override
	public DataEvent parse()
	{		
		System.out.println("<UBPE-event-reader-service>: starting event processing");
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int state = 0;
		while (state != 2)
		{
			int currentByte = -1;
			try
			{
				currentByte = this.in.read();
				if (currentByte == -1) 
				{
					//System.out.print("*");
					continue;
				}				
			}
			catch (IOException e)
			{
				// "IO exception while reading raw data"
				this.mustStop();
				// LOGGER.log(Level.FINEST, "Reader " + this.getReaderName() +
				// " could not parse event");
				return null;
			}
			
			//System.out.print(".");

			try
			{
				if (this.out != null)
				{
					this.out.write(currentByte);
					this.out.flush();
				}
			}
			catch (IOException e)
			{
			}
			
			if (currentByte == UBPE_START_FRAME_FLAG)
			{
				buffer.reset();
				state = 1;
				continue;
			}

			switch (state)
			{
			case 0:
				break;
			case 1:
				if ((currentByte == (byte) 0x0A) || (currentByte == (byte) 0x0D))
					state = 2;
				else
					buffer.write(currentByte);
				break;
			}
		}

		System.out.println("<UBPE-event-reader-service>: ending event processing");
		
		Map<String, MetadataField> eventMetadataFields = new HashMap<String, MetadataField>();
		eventMetadataFields.putAll(this.readerMetadataFields);
		eventMetadataFields.put("metadata.reader.timestamp", new DefaultMetadataField("metadata.reader.timestamp", long.class, System.currentTimeMillis()));

		return new UBPE2011DataEvent(buffer.toByteArray(), eventMetadataFields);
	}

	@Override
	public void serve() 
	{
		// Nothing to do here, it is a fake service	
	}
}