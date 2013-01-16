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
import java.util.Set;

import org.json.JSONException;

import fr.iutvalence.json.AffTransform;
import fr.iutvalence.json.ArrayFilter;
import fr.iutvalence.ubpe.core.interfaces.Stoppable;

public class JSonFilteredFilesProducerService implements Runnable, Stoppable
{
	private File jsonDir;
	
	private File globalFile;

	private boolean mustRun;

	private Set<String> stationNames;
	
	
	public final static String ANALOG_TEMP_TIME_GRAPH_FILENAME = "analogTempVersusTime.json";

	public JSonFilteredFilesProducerService(File globalFilePath)
	{		
		this.globalFile = globalFilePath;
		this.jsonDir = this.globalFile.getParentFile();
		this.mustRun = true;
		this.stationNames = new HashSet<String>();
	}

	@Override
	public void mustStop()
	{
		this.mustRun = false;

	}

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

	private void createTokenlessFile(File src, String destSuffix, String charset, String token) throws UnsupportedEncodingException, FileNotFoundException
	{
		BufferedReader in =  new BufferedReader(new InputStreamReader(new FileInputStream(src), charset));
		String destPath = src.getAbsolutePath().substring(0, src.getAbsolutePath().lastIndexOf('.')) +destSuffix;
		PrintStream out = new PrintStream(new FileOutputStream(destPath));
		while (true)
		{
			String line = null;
			try
			{
				line = in.readLine();
			}
			catch (IOException e)
			{			
			}
			if (line == null) break;
			
			if (!(line.trim().startsWith(token)))
				out.println(line);
		}
		
		try
		{
			in.close();
			out.close();
		}
		catch (IOException e)
		{
			// ignore it
		}
	}
	
	private void generateFilteredFiles()
	{			
		// generate AnalogTempsVersusTime.json
		try
		{
			String originalPathWithoutSuffix = this.globalFile.getAbsolutePath().substring(0, this.globalFile.getAbsolutePath().lastIndexOf('.'));
			
			// TODO charset and token as constructor param
			this.createTokenlessFile(globalFile, ".clean", "UTF-8", "//@@EVENT@@//");
			ArrayFilter.jsonFilter(new File(originalPathWithoutSuffix+".clean"), new File(this.jsonDir, "global_"+ANALOG_TEMP_TIME_GRAPH_FILENAME), new int[] {3, 14, 15}, new HashMap<Integer, AffTransform>());
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
