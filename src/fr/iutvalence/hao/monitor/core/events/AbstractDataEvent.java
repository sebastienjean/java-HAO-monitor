package fr.iutvalence.hao.monitor.core.events;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.iutvalence.hao.monitor.core.exceptions.NoSuchFieldException;
import fr.iutvalence.hao.monitor.core.exceptions.ParsingException;
import fr.iutvalence.hao.monitor.core.interfaces.DataEvent;
import fr.iutvalence.hao.monitor.core.interfaces.MetadataField;

public abstract class AbstractDataEvent implements DataEvent, Serializable
{
	protected Map<String, MetadataField> metadataFields;

	private final byte[] rawDataBytes;

	private boolean hasParsedData;

	private final boolean hasRawData;

	private Object parsedData;

	public AbstractDataEvent(byte[] rawForm, Map<String, MetadataField> metadataFields)
	{
		this.rawDataBytes = rawForm;
		this.hasRawData = !(rawForm == null);

		this.metadataFields = new HashMap<String, MetadataField>();
		this.metadataFields.putAll(metadataFields);

		try
		{
			this.parsedData = this.parseRawForm();
			this.hasParsedData = true;
		}
		catch (ParsingException e)
		{
			this.parsedData = null;
			this.hasParsedData = false;
		}
	}

	@Override
	public int getMetadataFieldsSize()
	{
		return this.metadataFields.size();
	}

	@Override
	public Set<String> getMetadataFieldsNames()
	{
		return Collections.unmodifiableSet(this.metadataFields.keySet());
	}

	@Override
	public MetadataField getMetadataFieldByName(String eventFieldName) throws NoSuchFieldException
	{
		if (!this.metadataFields.containsKey(eventFieldName))
			throw new NoSuchFieldException();
		return this.metadataFields.get(eventFieldName);
	}

	@Override
	public boolean hasRawData()
	{
		return hasRawData;
	}

	@Override
	public byte[] getRawData()
	{
		return this.rawDataBytes.clone();
	}

	@Override
	public boolean hasParsedData()
	{
		return this.hasParsedData;
	}

	@Override
	public Object getParsedData()
	{
		// TODO make this immutable
		return this.parsedData;
	}

	@Override
	public String toString()
	{
		String result = "";
		for (MetadataField dataField : this.metadataFields.values())
			result += "{" + dataField + "} ";

		return result;
	}

	public abstract Object parseRawForm() throws ParsingException;

	public boolean addMetadataField(MetadataField metadataField)
	{
		if (this.metadataFields.containsKey(metadataField.getName()))
			return false;

		this.metadataFields.put(metadataField.getName(), metadataField);
		return true;
	}
}
