package fr.iutvalence.ubpe.ubpe2011;

import java.util.HashMap;
import java.util.Map;

import fr.iutvalence.ubpe.core.events.AbstractDataEvent;
import fr.iutvalence.ubpe.core.exceptions.MalformedFrameException;
import fr.iutvalence.ubpe.core.exceptions.ParsingException;
import fr.iutvalence.ubpe.core.helpers.AbstractFileSystemStorage;
import fr.iutvalence.ubpe.core.interfaces.MetadataField;

public class UBPE2011DataEvent extends AbstractDataEvent
{

	public UBPE2011DataEvent(byte[] ubpeFrame)
	{
		this(ubpeFrame, new HashMap<String, MetadataField>());
	}

	public UBPE2011DataEvent(byte[] ubpeFrame, Map<String, MetadataField> metadataFields)
	{
		super(ubpeFrame, metadataFields);
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
