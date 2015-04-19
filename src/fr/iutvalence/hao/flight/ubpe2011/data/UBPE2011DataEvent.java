package fr.iutvalence.hao.flight.ubpe2011.data;

import java.util.HashMap;
import java.util.Map;

import fr.iutvalence.hao.monitor.core.events.AbstractDataEvent;
import fr.iutvalence.hao.monitor.core.events.DefaultMetadataField;
import fr.iutvalence.hao.monitor.core.exceptions.MalformedFrameException;
import fr.iutvalence.hao.monitor.core.exceptions.ParsingException;
import fr.iutvalence.hao.monitor.core.helpers.AbstractFileSystemStorage;
import fr.iutvalence.hao.monitor.core.interfaces.MetadataField;
import fr.iutvalence.ubpe.ubpe2012.UBPE2012Data;

public class UBPE2011DataEvent extends AbstractDataEvent
{

	public UBPE2011DataEvent(byte[] ubpeFrame)
	{
		this(ubpeFrame, new HashMap<String, MetadataField>());
		UBPE2011Data data = (UBPE2011Data) (this.getParsedData());
		this.metadataFields.put("metadata.object.name", new DefaultMetadataField("metadata.object.name", String.class, data.getFrameTokens()[0]));
	}

	public UBPE2011DataEvent(byte[] ubpeFrame, Map<String, MetadataField> metadataFields)
	{
		super(ubpeFrame, metadataFields);
		UBPE2011Data data = (UBPE2011Data) (this.getParsedData());
		this.metadataFields.put("metadata.object.name", new DefaultMetadataField("metadata.object.name", String.class, data.getFrameTokens()[0]));
	}

	@Override
	public Object parseRawForm() throws ParsingException
	{
		UBPE2011Data parsedEvent = null;
		try
		{
			parsedEvent = new UBPE2011Data(this.getRawData());
			// Extract objectName
			return parsedEvent;
		}
		catch (MalformedFrameException e)
		{
			throw new ParsingException();
		}
	}

	public String toString()
	{
		String result = "UBPE2011DataEvent\n";
		try
		{
			result += "- received by " + (String) (this.getMetadataFieldByName("metadata.reader.name").getValue()) + "\n";
		}
		catch (Exception e)
		{
		}
		try
		{
			result += "- received from " + (String) (this.getMetadataFieldByName("metadata.object.name").getValue()) + "\n";
		}
		catch (Exception e)
		{
		}
		try
		{
			result += "- received on "
					+ AbstractFileSystemStorage.RAWFILE_DATEFORMATTER.format((Long) (this.getMetadataFieldByName("metadata.reader.timestamp").getValue()))
					+ "\n";
		}
		catch (Exception e)
		{
		}
		result += this.getParsedData();
		return result;
	}
}
