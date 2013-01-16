package fr.iutvalence.ubpe.core.interfaces;

import fr.iutvalence.ubpe.core.exceptions.StorageException;

public interface DataEventStorage extends DataEventListener
{
	public String getDataEventStorageName();

	public void storeDataEvent(DataEvent event) throws StorageException;
}
