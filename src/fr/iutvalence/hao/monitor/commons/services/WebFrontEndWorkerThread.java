package fr.iutvalence.hao.monitor.commons.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Map;

import fr.iutvalence.hao.monitor.core.exceptions.ParsingException;
import fr.iutvalence.hao.monitor.core.interfaces.DataEventParserForwarder;

public class WebFrontEndWorkerThread extends Thread
{
	/**
	 * Socket used to communicate with client.
	 */
	private final Socket socket;

	/**
	 * Data events parsers (value) to be used, for each event type supported
	 * (key).
	 */
	private final Map<String, DataEventParserForwarder> parsers;

	/**
	 * Creates a new <tt>WebFrontEndWelcomeService</tt> instance, locally bound
	 * to a given address, notifying to a given collection of listeners and
	 * using a given collection of parsers (depending of event types)
	 * 
	 * @param socket
	 *            client socket
	 * @param parsers
	 *            data event parsers to be used, for each supported event type.
	 */
	public WebFrontEndWorkerThread(Socket socket, Map<String, DataEventParserForwarder> parsers)
	{
		// TODO verify that metadata are cloned
		// super(new HashMap<String, MetadataField>());

		this.socket = socket;

		this.parsers = parsers;

		// for (DataEventListener listener : listeners)
		// this.registerDataEventListener(listener);
	}

	public void run()
	{
		System.out.println("<WebFrontEndWorker-" + this.hashCode() + ">: starting event parsing");

		String line = null;
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "US-ASCII"));
			line = in.readLine();
		}
		catch (Exception e)
		{
			// Nothing special to do, just let line be null
		}

		if (line == null)
		{
			System.err.println("<WebFrontEndWorker-" + this.hashCode() + ">: broken connection...bye!");
			return;
		}
		System.out.println("<WebFrontEndWorker-" + this.hashCode() + ">: received " + line);

		String[] tokens = line.split("@");
		if (tokens.length != 2)
		{
			System.err.println("<WebFrontEndWorker-" + this.hashCode() + ">: nothing readable...bye!");
			return;
		}

		DataEventParserForwarder parser = this.parsers.get(tokens[0]);
		if (parser == null)
		{
			System.err.println("<WebFrontEndWorker-" + this.hashCode() + ">: no suitable parser found...bye!");
			return;
		}

		try
		{
			parser.parseAndForward(tokens[1].getBytes("US-ASCII"));
		}
		catch (Exception e)
		{
			System.err.println("<WebFrontEndWorker-" + this.hashCode() + ">: unable to parse event...bye!");
			return;
		}

		try
		{
			this.socket.close();
		}
		catch (IOException e1)
		{
			// Ignoring socket closing failure
		}
	}
}
