package fr.iutvalence.ubpe.ubpecommons.services;

import java.io.File;

import fr.iutvalence.ubpe.core.services.AbstractPeriodicService;

/**
 * Service that take a JSON input file containing events and produces output files obtained by selecting a set of fields.
 * This service can for example be used to generate charts-friendly files retaining only given series of data)
 * <br/>
 * 
 * @author sebastienjean
 *
 */
public abstract class AbstractTextFileTransformationPeriodicService extends AbstractPeriodicService
{
	/**
	 * Input file path (complete, with suffix).
	 */
	private final File inputFile;
	
	/**
	 * Charset to be used.
	 */
	private final String charset;
	
	/**
	 * Directory where to generate output files.
	 */
	private final File outputFile;
	
	public AbstractTextFileTransformationPeriodicService(long period, File inputFile, String charset, File outputFile)
	{		
		super(period);
		this.inputFile = inputFile;
		this.charset = charset;
		this.outputFile = outputFile;
	}

	public File getInputFile() 
	{
		return this.inputFile;
	}

	public String getCharset() 
	{
		return this.charset;
	}

	public File getOutputFile() 
	{
		return this.outputFile;
	}
}
