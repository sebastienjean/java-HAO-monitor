package fr.iutvalence.ubpe.core.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import fr.iutvalence.ubpe.core.interfaces.DataEvent;

public abstract class AbstractFileBuilderDataEventListenerService extends AbstractDataEventListenerService
{	
	private File destFile;

	private String extension;

	private String charset;

	private String startOfToken;

	private String endOfToken;
	
	private boolean firstTime;
	
	public AbstractFileBuilderDataEventListenerService(File file, String fileExtension, String charset, String startOfToken, String endOfToken)
	{
		this.destFile = file;
		this.charset = charset;
		this.startOfToken = startOfToken;
		this.endOfToken = endOfToken;
		this.extension = fileExtension;
		this.firstTime = true;
	}

	public abstract String insertDataEventText(DataEvent event, String token, boolean firstTime);
	
	protected void onTakingEvent(DataEvent event)
	{
		System.out.println("<fileBuilder-service>: starting event processing");
		BufferedReader br = null;
		PrintStream ps = null;
		// TODO throwing an exception
		try
		{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(destFile + "." + this.extension), this.charset));
			ps = new PrintStream(new FileOutputStream(destFile + ".new"), true, this.charset);
			while (true)
			{
				String line = br.readLine();
				if (line == null)
					break;
				ps.println(line);
				String lineTrimed = line.trim();
				if (lineTrimed.startsWith(this.startOfToken) && lineTrimed.endsWith(this.endOfToken))
				{
					int sot = lineTrimed.indexOf(this.startOfToken);
					//System.out.println(sot);
					//System.out.println(sot + this.startOfToken.length());
					int eot = lineTrimed.indexOf(this.endOfToken);
					//System.out.println(eot);
					String token = lineTrimed.substring(sot + this.startOfToken.length(), eot).trim();
					String textToInsert = insertDataEventText(event, token, this.firstTime);
					
					if (textToInsert != null)
					{
						if (this.firstTime && (textToInsert.length()>0)) this.firstTime = false;
						ps.println(textToInsert);
					}
				}
			}
			new File(destFile + "." + this.extension).renameTo(new File(destFile + ".old"));
			new File(destFile + ".new").renameTo(new File(destFile + "." + this.extension));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			br.close();
			ps.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("<fileBuilder-service>: ending event processing");
	}
}
