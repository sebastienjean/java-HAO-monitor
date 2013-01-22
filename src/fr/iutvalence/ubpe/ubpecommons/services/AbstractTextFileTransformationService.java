package fr.iutvalence.ubpe.ubpecommons.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import fr.iutvalence.json.AffTransform;
import fr.iutvalence.json.ArrayFilter;
import fr.iutvalence.ubpe.core.services.AbstractService;

/**
 * Service that take a JSON input file containing events and produces output files obtained by selecting a set of fields.
 * This service can for example be used to generate charts-friendly files retaining only given series of data)
 * <br/>
 * 
 * @author sebastienjean
 *
 */
public abstract class AbstractTextFileTransformationService extends AbstractService
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
	
	public AbstractTextFileTransformationService(File inputFile, String charset, File outputFile)
	{		
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
