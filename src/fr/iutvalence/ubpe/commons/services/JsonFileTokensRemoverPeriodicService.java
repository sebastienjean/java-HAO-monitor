package fr.iutvalence.ubpe.commons.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import fr.iutvalence.ubpe.core.services.AbstractTextFileTransformationPeriodicService;

/**
 * Service that take a JSON input file containing events and produces output files obtained by selecting a set of fields.
 * This service can for example be used to generate charts-friendly files retaining only given series of data)
 * <br/>
 * 
 * @author sebastienjean
 *
 */
public class JsonFileTokensRemoverPeriodicService extends AbstractTextFileTransformationPeriodicService
{
	/**
	 * Array of tokens to
	 */
	private String[] tokens;
	

	public JsonFileTokensRemoverPeriodicService(long period, File inputFile, String charset, File outputFile, String[] tokens)
	{	
		super(period, inputFile, charset, outputFile);
		this.tokens = tokens;
	}

	public void serve()
	{
		BufferedReader in = null;
		try 
		{
			in = new BufferedReader(new InputStreamReader(new FileInputStream(this.getInputFile()), this.getCharset()));
		} 
		// TODO register an error handler
		catch (Exception e) 
		{
			return;
		}

		//String destPath = inputFile.getAbsolutePath().substring(0, inputFile.getAbsolutePath().lastIndexOf('.')) +destSuffix;
		PrintStream out = null;
		try 
		{
			out = new PrintStream(new FileOutputStream(this.getOutputFile()));
		} 
		catch (Exception e) 
		{
			return;
		}
		
		while (true)
		{
			String line = null;
			try
			{
				line = in.readLine();
			}
			catch (IOException e)
			{			
				// no use to do something special here, just let line remain null
			}
			if (line == null) break;
			
			boolean isTokenLine = false;
			
			for (String token:this.tokens)
				if ((line.trim().startsWith(token))) isTokenLine = true;

			if (!isTokenLine) 
				out.println(line);
		}
		
		try
		{
			in.close();
			out.close();
		}
		catch (IOException e)
		{
			// ignore failure on close operations
		}
	}
}
