package fr.iutvalence.hao.monitor.core.helpers;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import fr.iutvalence.hao.monitor.core.events.DefaultMetadataField;
import fr.iutvalence.hao.monitor.core.exceptions.ParsingException;
import fr.iutvalence.hao.monitor.core.interfaces.DataEvent;
import fr.iutvalence.hao.monitor.core.interfaces.MetadataField;

public class UBPEDataEventParserForwarder extends AbstractDataEventParserForwarder
{
	private final Class<?> dataEventClass;

	private final String enventType;

	public UBPEDataEventParserForwarder(Class<?> dataEventClass, String enventType)
	{
		super();
		this.dataEventClass = dataEventClass;
		this.enventType = enventType;
	}

	@Override
	public DataEvent parse(byte[] eventData) throws ParsingException
	{
		String eventString = null;

		try
		{
			eventString = new String(eventData, "US-ASCII");
		}
		catch (UnsupportedEncodingException e1)
		{
			throw new ParsingException();
		}
		System.err.println(eventString);
		String[] tokens = eventString.split("#");
		if (tokens.length != 2)
			throw new ParsingException();

		Map<String, MetadataField> eventMetadataFields = new HashMap<String, MetadataField>();

		eventMetadataFields.put("metadata.reader.timestamp", new DefaultMetadataField("metadata.reader.timestamp", long.class, System.currentTimeMillis()));
		eventMetadataFields.put("metadata.reader.name", new DefaultMetadataField("metadata.reader.name", String.class, tokens[0]));
		eventMetadataFields.put("metadata.event.type", new DefaultMetadataField("metadata.event.type", String.class, this.enventType));

		try
		{
			Constructor<?> constructor = this.dataEventClass.getConstructor(byte[].class, Map.class);
			return (DataEvent) constructor.newInstance(tokens[1].getBytes("US-ASCII"), eventMetadataFields);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new ParsingException();
		}
	}
}
