package fr.iutvalence.ubpe.ubpe2014;

import java.util.HashMap;
import java.util.Map;

import fr.iutvalence.ubpe.core.events.AbstractDataEvent;
import fr.iutvalence.ubpe.core.events.DefaultMetadataField;
import fr.iutvalence.ubpe.core.exceptions.MalformedFrameException;
import fr.iutvalence.ubpe.core.exceptions.ParsingException;
import fr.iutvalence.ubpe.core.helpers.AbstractFileSystemStorage;
import fr.iutvalence.ubpe.core.interfaces.MetadataField;

public class UBPE2014DataEvent extends AbstractDataEvent
{

	public UBPE2014DataEvent(byte[] ubpeFrame)
	{
		this(ubpeFrame, new HashMap<String, MetadataField>());
		UBPE2014Data data = (UBPE2014Data) (this.getParsedData());
		this.metadataFields.put("metadata.object.name", new DefaultMetadataField("metadata.object.name", String.class, data.getFrameTokens()[0]));
	}

	public UBPE2014DataEvent(byte[] ubpeFrame, Map<String, MetadataField> metadataFields)
	{
		super(ubpeFrame, metadataFields);
		UBPE2014Data data = (UBPE2014Data) (this.getParsedData());
		this.metadataFields.put("metadata.object.name", new DefaultMetadataField("metadata.object.name", String.class, data.getFrameTokens()[0]));
	}

	@Override
	public Object parseRawForm() throws ParsingException
	{
		UBPE2014Data parsedEvent = null;
		try
		{
			parsedEvent = new UBPE2014Data(this.getRawData());
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
		String result = "UBPE2014DataEvent\n";
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
