package fr.iutvalence.ubpe.ubpe2013.services;

// TODO abstract this to reuse across different experiment instances
import java.io.File;

import fr.iutvalence.hao.monitor.core.interfaces.DataEvent;
import fr.iutvalence.hao.monitor.core.services.AbstractFileBuilderDataEventListenerService;
import fr.iutvalence.ubpe.ubpe2013.UBPE2013Data;
import fr.iutvalence.ubpe.ubpe2013.UBPE2013DataEvent;

public class UBPE2013JsonProducerDataEventListenerService extends AbstractFileBuilderDataEventListenerService
{
	public final static String TOKEN = "EVENT";

	public UBPE2013JsonProducerDataEventListenerService(File file, String charset)
	{

		super(file, "json", charset, "<!-- @@", "@@ -->");
	}

	public String insertDataEventText(DataEvent event, String token, boolean firstTime)
	{
		if (!token.equals(TOKEN))
			return null;

		UBPE2013DataEvent ubpeDataEvent = (UBPE2013DataEvent) event;
		UBPE2013Data ubpeData = (UBPE2013Data) ubpeDataEvent.getParsedData();

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

		/*
		 * readerTimestamp, readerName, objectName, frameCounter, resetCounter, currentFlightPhaseNumber, currentFlightPhaseDurationInSeconds,
		    secondsSinceLastReset, timeRTC, timeGPS, fixGPS, longGPS, latGPS, altGPS, speedGPS, courseGPS, numSatsGPS, hdopGPS,
			differentialPressureAnalogSensor, absolutePressureAnalogSensor, externalTemperatureAnalogSensor, internalTemperatureAnalogSensor, analogVoltaageSensor
		 */
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
