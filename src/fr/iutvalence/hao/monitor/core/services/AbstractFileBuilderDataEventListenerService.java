package fr.iutvalence.hao.monitor.core.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import fr.iutvalence.hao.monitor.core.interfaces.DataEvent;

/**
 * Helper implementation of a data event listener service that produces output
 * in a text file.<br/>
 * <br/>
 * 
 * A template file (supposed to be existing) is used both for input and output.
 * It contains tokens where generated information can be inserted (before or/and
 * after) each time a data event is received.<br/>
 * There can be several tokens in the same template file, but all tokens that
 * this service is able to recognise must begin and end with the same characters
 * sequences.
 * 
 * @author sebastienjean
 * 
 */
public abstract class AbstractFileBuilderDataEventListenerService extends AbstractDataEventListenerService
{
	/**
	 * Destination file path.
	 */
	private File destFile;

	/**
	 * File extension.
	 */
	private String extension;

	/**
	 * Charset to be used.
	 */
	private String charset;

	/**
	 * String identifying the beginning of a token.
	 */
	private String startOfToken;

	/**
	 * String identifying the end of a token.
	 */
	private String endOfToken;

	/**
	 * Boolean indicating if it is the first event processing
	 */
	private boolean firstTime;

	/**
	 * Creating a new <tt>AbstractFileBuilderDataEventListenerService</tt>
	 * instance, from given filename/extension, charset and start/end of tokens.
	 * 
	 * @param file
	 *            input/output file path (including name but excluding
	 *            extension)
	 * @param fileExtension
	 *            input/output file extension
	 * @param charset
	 *            charset ot be used
	 * @param startOfToken
	 *            string identifying the beginning of tokens
	 * @param endOfToken
	 *            string identifying the end of tokens
	 */
	public AbstractFileBuilderDataEventListenerService(File file, String fileExtension, String charset, String startOfToken, String endOfToken)
	{
		this.destFile = file;
		this.charset = charset;
		this.startOfToken = startOfToken;
		this.endOfToken = endOfToken;
		this.extension = fileExtension;
		this.firstTime = true;
	}

	/**
	 * Text insertion behaviour.<br/>
	 * Once an event is received, this method is called each time a valid token
	 * (matching start/end strings) is parsed in input/output file.
	 * 
	 * @param event
	 *            the received event
	 * @param token
	 *            the parsed token
	 * @param firstTime
	 *            first event indicator
	 * @return the text to be inserted
	 */
	public abstract String insertDataEventText(DataEvent event, String token, boolean firstTime);

	/**
	 * @see fr.iutvalence.hao.monitor.core.services.AbstractDataEventListenerService#onTakingEvent(fr.iutvalence.hao.monitor.core.interfaces.DataEvent)
	 */
	protected void onTakingEvent(DataEvent event)
	{
		System.out.println("<fileBuilder-service>: starting event processing");
		BufferedReader br = null;
		PrintStream ps = null;
		// TODO throwing an exception
		try
		{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(this.destFile + "." + this.extension), this.charset));
			ps = new PrintStream(new FileOutputStream(this.destFile + ".new"), true, this.charset);
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
					// System.out.println(sot);
					// System.out.println(sot + this.startOfToken.length());
					int eot = lineTrimed.indexOf(this.endOfToken);
					// System.out.println(eot);
					String token = lineTrimed.substring(sot + this.startOfToken.length(), eot).trim();
					String textToInsert = insertDataEventText(event, token, this.firstTime);

					if (textToInsert != null)
					{
						if (this.firstTime && (textToInsert.length() > 0))
							this.firstTime = false;
						ps.println(textToInsert);
					}
				}
			}
			new File(this.destFile + "." + this.extension).renameTo(new File(this.destFile + ".old"));
			new File(this.destFile + ".new").renameTo(new File(this.destFile + "." + this.extension));
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
