package fr.iutvalence.hao.monitor.core.interfaces;

import fr.iutvalence.hao.monitor.core.exceptions.StorageException;

public interface DataEventStorage extends DataEventListener
{
	public String getDataEventStorageName();

	public void storeDataEvent(DataEvent event) throws StorageException;
}
