package fr.iutvalence.hao.flight.ubpe2015.data;

import java.util.HashMap;
import java.util.Map;

import fr.iutvalence.hao.monitor.core.events.AbstractDataEvent;
import fr.iutvalence.hao.monitor.core.events.DefaultMetadataField;
import fr.iutvalence.hao.monitor.core.exceptions.MalformedFrameException;
import fr.iutvalence.hao.monitor.core.exceptions.ParsingException;
import fr.iutvalence.hao.monitor.core.helpers.AbstractFileSystemStorage;
import fr.iutvalence.hao.monitor.core.interfaces.MetadataField;

public class UBPE2015DataEvent extends AbstractDataEvent
{

	public UBPE2015DataEvent(byte[] ubpeFrame)
	{
		this(ubpeFrame, new HashMap<String, MetadataField>());
		UBPE2015Data data = (UBPE2015Data) (this.getParsedData());
		this.metadataFields.put("metadata.object.name", new DefaultMetadataField("metadata.object.name", String.class, data.getFrameTokens()[0]));
	}

	public UBPE2015DataEvent(byte[] ubpeFrame, Map<String, MetadataField> metadataFields)
	{
		super(ubpeFrame, metadataFields);
		UBPE2015Data data = (UBPE2015Data) (this.getParsedData());
		this.metadataFields.put("metadata.object.name", new DefaultMetadataField("metadata.object.name", String.class, data.getFrameTokens()[0]));
	}

	@Override
	public Object parseRawForm() throws ParsingException
	{
		UBPE2015Data parsedEvent = null;
		try
		{
			parsedEvent = new UBPE2015Data(this.getRawData());
			// Extract objectName
			return parsedEvent;
		}
		catch (MalformedFrameException e)
		{
			e.printStackTrace();
			throw new ParsingException();
		}
	}

	public String toString()
	{
		String result = "UBPE2015DataEvent\n";
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
