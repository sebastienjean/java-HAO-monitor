package fr.iutvalence.ubpe.core.interfaces;

import java.util.Set;

import fr.iutvalence.ubpe.core.exceptions.NoSuchFieldException;

public interface DataEvent
{
	public int getMetadataFieldsSize();

	public Set<String> getMetadataFieldsNames();

	public MetadataField getMetadataFieldByName(String name) throws NoSuchFieldException;

	public boolean hasParsedData();

	public Object getParsedData();

	public boolean hasRawData();

	public byte[] getRawData();

	public boolean addMetadataField(MetadataField metadataField);
}
