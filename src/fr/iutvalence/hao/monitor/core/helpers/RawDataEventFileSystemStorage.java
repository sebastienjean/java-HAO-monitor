package fr.iutvalence.hao.monitor.core.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import fr.iutvalence.hao.monitor.core.exceptions.NoSuchFieldException;
import fr.iutvalence.hao.monitor.core.exceptions.StorageException;
import fr.iutvalence.hao.monitor.core.interfaces.DataEvent;

public class RawDataEventFileSystemStorage extends AbstractFileSystemStorage
{
	// private final static Logger LOGGER =
	// Logger.getLogger(RawDataEventFileSystemStorage.class.getName());

	public RawDataEventFileSystemStorage(String eventStorageName, File eventStorageRootDir) throws FileNotFoundException
	{
		super(eventStorageName, eventStorageRootDir);
	}

	@Override
	public void storeDataEvent(DataEvent event) throws StorageException
	{
		String objectName = null;
		String receiverName = null;
		long receiverTimestamp = System.currentTimeMillis();

		try
		{
			objectName = (String) (event.getMetadataFieldByName("metadata.object.name").getValue());
			// LOGGER.log(Level.INFO, eventStorageName +
			// " found object name metadata in event");
			receiverName = (String) (event.getMetadataFieldByName("metadata.reader.name").getValue());
			// LOGGER.log(Level.INFO, eventStorageName +
			// " found reader name metadata in event");
		}
		catch (NoSuchFieldException e)
		{
			throw new StorageException("Event does not contains either object or reader name");
		}

		File storageSubdir = new File(this.eventStorageRootDir, objectName + "/" + receiverName);
		storageSubdir.mkdirs();

		try
		{
			receiverTimestamp = (Long) (event.getMetadataFieldByName("metadata.reader.timestamp").getValue());
			// LOGGER.log(Level.INFO, eventStorageName +
			// " found timestamp metadata in event");
		}
		catch (NoSuchFieldException e)
		{
		}

		try
		{
			this.storeAsRaw(event, new File(storageSubdir, "" + receiverTimestamp + ".raw"));
			// TODO check for windows if a user-friendly name can be used
			// (parsed format fails)
			// this.storeAsRaw(event, new File(storageSubdir,
			// RAWFILE_DATEFORMATTER.format(receiverTimestamp) + ".raw"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new StorageException("Exception while writing output file");
		}
		// LOGGER.log(Level.INFO, eventStorageName + " logged an event");
	}
}
