package fr.iutvalence.ubpe.core.events;

import java.io.Serializable;

import fr.iutvalence.ubpe.core.interfaces.MetadataField;

public class DefaultMetadataField implements MetadataField, Serializable
{
	private final String name;

	private final Class<?> type;

	private final Object value;

	public DefaultMetadataField(String name, Class<?> type, Object value)
	{
		this.name = name;
		this.type = type;
		this.value = value;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	public Class<?> getType()
	{
		return this.type;
	}

	@Override
	public Object getValue()
	{
		return this.value;
	}

	@Override
	public String toString()
	{
		return "MetadataField<" + this.type.getName() + "/" + this.name + "> = " + this.value;
	}

	@Override
	public boolean equals(Object obj)
	{
		if ((this == null) && (obj == null))
			return false;
		if (!(obj instanceof MetadataField))
			return false;
		return this.name.equals(((MetadataField) obj).getName());
	}

	@Override
	public int hashCode()
	{
		return this.name.hashCode();
	}

}
