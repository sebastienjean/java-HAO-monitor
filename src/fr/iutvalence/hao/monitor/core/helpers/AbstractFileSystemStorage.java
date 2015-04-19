package fr.iutvalence.hao.monitor.core.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.iutvalence.hao.monitor.core.exceptions.StorageException;
import fr.iutvalence.hao.monitor.core.interfaces.DataEvent;
import fr.iutvalence.hao.monitor.core.interfaces.DataEventStorage;

public abstract class AbstractFileSystemStorage implements DataEventStorage
{
	// private final static Logger LOGGER =
	// Logger.getLogger(AbstractFileSystemStorage.class.getName());

	protected final String eventStorageName;

	protected File eventStorageRootDir;

	public final static SimpleDateFormat RAWFILE_DATEFORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	public AbstractFileSystemStorage(String eventStorageName, File eventStorageRootDir) throws FileNotFoundException
	{
		if (!eventStorageRootDir.isDirectory())
		{
			if (!eventStorageRootDir.mkdirs())
				throw new FileNotFoundException();
		}

		this.eventStorageRootDir = eventStorageRootDir;
		this.eventStorageName = eventStorageName;

		// TODO debug
		System.out.println("Storage created, storing in " + eventStorageRootDir.getAbsolutePath());
		// LOGGER.log(Level.INFO, eventStorageName + " created, storing in " +
		// eventStorageRootDir.getAbsolutePath());
	}

	@Override
	public final void process(DataEvent event)
	{
		if (event == null)
		{
			// LOGGER.log(Level.FINEST, "storage " + this.eventStorageName +
			// " received an empty event");
			// TODO debug
			System.out.println("Storage received an empty event");
			try
			{
				new File(this.eventStorageRootDir, RAWFILE_DATEFORMATTER.format(new Date()) + ".malformed").createNewFile();
			}
			catch (IOException e)
			{
				// Ignoring this
			}
		}

		// LOGGER.log(Level.FINEST, "storage " + this.eventStorageName +
		// " received an event");
		// TODO debug
		System.out.println("Storage received an event");
		try
		{
			this.storeDataEvent(event);
		}
		catch (StorageException e)
		{
			e.printStackTrace();
			try
			{
				this.storeAsRaw(event, new File(this.eventStorageRootDir, RAWFILE_DATEFORMATTER.format(new Date()) + ".malformed"));
				// LOGGER.log(Level.WARNING,
				// "event could not be stored as expected, stored as raw");
				// TODO debug
				System.out.println("Storage could not store event as expected, storing it as raw");
			}
			catch (IOException e1)
			{
				// LOGGER.log(Level.WARNING,
				// "event could not be stored neither as expected, neither as raw");
				// TODO debug
				System.out.println("Storage could not store event neither as expected, neither as raw");
			}
		}
	}

	protected final void storeAsRaw(DataEvent event, File outFile) throws IOException
	{
		if (!outFile.createNewFile())
			throw new IOException();
		byte[] rawForm = event.getRawData();

		if (rawForm != null)
		{
			FileOutputStream outStream = new FileOutputStream(outFile);
			outStream.write(rawForm);
			outStream.flush();
			outStream.close();
		}
	}

	@Override
	public final String getDataEventStorageName()
	{
		return this.eventStorageName;
	}

	@Override
	public abstract void storeDataEvent(DataEvent event) throws StorageException;
}
