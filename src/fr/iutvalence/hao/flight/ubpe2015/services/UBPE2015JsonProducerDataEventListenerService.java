package fr.iutvalence.hao.flight.ubpe2015.services;

// TODO abstract this to reuse across different experiment instances
import java.io.File;

import fr.iutvalence.hao.flight.ubpe2015.data.UBPE2015Data;
import fr.iutvalence.hao.flight.ubpe2015.data.UBPE2015DataEvent;
import fr.iutvalence.hao.monitor.core.interfaces.DataEvent;
import fr.iutvalence.hao.monitor.core.services.AbstractFileBuilderDataEventListenerService;

public class UBPE2015JsonProducerDataEventListenerService extends AbstractFileBuilderDataEventListenerService
{
	public final static String TOKEN = "EVENT";

	public UBPE2015JsonProducerDataEventListenerService(File file, String charset)
	{

		super(file, "json", charset, "<!-- @@", "@@ -->");
	}

	public String insertDataEventText(DataEvent event, String token, boolean firstTime)
	{
		if (!token.equals(TOKEN))
			return null;

		UBPE2015DataEvent ubpeDataEvent = (UBPE2015DataEvent) event;
		UBPE2015Data ubpeData = (UBPE2015Data) ubpeDataEvent.getParsedData();

		try
		{
			if (!ubpeData.isValidData())
				return null;

		}
		catch (NullPointerException e)
		{
			return null;
		}

		String readerName = "<unknown>";

		try
		{
			readerName = (String) event.getMetadataFieldByName("metadata.reader.name").getValue();
		}
		catch (Exception e)
		{
		}

		long readerTimestamp = System.currentTimeMillis();
		try
		{
			readerTimestamp = (Long) event.getMetadataFieldByName("metadata.reader.timestamp").getValue();
		}
		catch (Exception e)
		{
		}

		String[] frameTokens = ubpeData.getFrameTokens();

		String result = "[\n";

		// Inserting reader timestamp (from metadata), with quotes
		result += "\"" + readerTimestamp + "\", ";

		// Inserting reader name (from metadata), with quotes
		result += "\"" + readerName + "\", ";

		for (int i = 0; i < frameTokens.length; i++)
		{
			// Inserting frame token, with quotes
			result += "\"" + frameTokens[i] + "\"";
			if (i < frameTokens.length - 1)
				result += ", ";
		}
		result += "\n]";

		if (!firstTime)
			result += ",";
		return result;
	}
}
