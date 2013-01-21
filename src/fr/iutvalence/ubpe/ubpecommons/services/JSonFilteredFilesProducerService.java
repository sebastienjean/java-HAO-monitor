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
import java.util.HashSet;
import java.util.Map;

import org.json.JSONException;

import fr.iutvalence.json.AffTransform;
import fr.iutvalence.json.ArrayFilter;
import fr.iutvalence.ubpe.core.interfaces.Stoppable;

public class JSonFilteredFilesProducerService implements Runnable, Stoppable
{
	private File inputFilePathWithSuffix;
	
	private String charset;
	
	private File outputDir;
	
	private Map<String, int[]> filtersToGenerate;
	
	private String[] tokens;
	
	private boolean mustRun;

	public JSonFilteredFilesProducerService(File inputFilePathWithSuffix, String charset, File outputDir, Map<String, int[]> filtersToGenerate, String[] tokens)
	{		
		this.inputFilePathWithSuffix = inputFilePathWithSuffix;
		this.charset = charset;
		this.outputDir = outputDir;
		this.filtersToGenerate = filtersToGenerate;
		this.tokens = tokens;
		this.mustRun = true;
	}

	/**
	 * @see fr.iutvalence.ubpe.core.interfaces.Stoppable#mustStop()
	 */
	@Override
	public void mustStop()
	{
		this.mustRun = false;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		while (this.mustRun)
		{
			this.generateFilteredFiles();
			try
			{
				Thread.sleep(30000);
			}
			catch (InterruptedException e)
			{
				// ignore it
			}
		}

	}

	/**
	 * Internal method used to filter all tokens from a text file
	 * @param outputFile output file path (with suffix)
	 * @throws UnsupportedEncodingException if charset is not available
	 * @throws FileNotFoundException if inputFile is not found
	 */
	private void createTokenlessFile(File outputFile) throws UnsupportedEncodingException, FileNotFoundException
	{
		BufferedReader in =  new BufferedReader(new InputStreamReader(new FileInputStream(this.inputFilePathWithSuffix), this.charset));
		//String destPath = inputFile.getAbsolutePath().substring(0, inputFile.getAbsolutePath().lastIndexOf('.')) +destSuffix;
		PrintStream out = new PrintStream(new FileOutputStream(outputFile));
		
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
			
			for (String token:tokens)
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
	
	/**
	 * Internal method used to generate filtered files
	 */
	private void generateFilteredFiles()
	{			
		// generate AnalogTempsVersusTime.json
		try
		{
			String originalPathWithoutSuffix = this.inputFilePathWithSuffix.getAbsolutePath().substring(0, this.inputFilePathWithSuffix.getAbsolutePath().lastIndexOf('.'));
			for (Map.Entry<String, int[]> entry: this.filtersToGenerate.entrySet())
			{
				this.createTokenlessFile(new File(this.outputDir, originalPathWithoutSuffix +".clean"));
				ArrayFilter.jsonFilter(new File(this.outputDir,originalPathWithoutSuffix+".clean"), new File(this.outputDir, entry.getKey()), entry.getValue(), new HashMap<Integer, AffTransform>());
			}
			
			//
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
