package fr.iutvalence.ubpe.core.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

public class AsciiDumpFileReplayInputStream extends PipedInputStream implements Runnable
{
	private final BufferedReader in;

	private final PrintStream out;

	private final long pause;

	public AsciiDumpFileReplayInputStream(File path, String charset, long pauseBetweenLines) throws IOException
	{
		this.in = new BufferedReader(new InputStreamReader(new FileInputStream(path), charset));
		this.out = new PrintStream(new PipedOutputStream(this), true, charset);
		this.pause = pauseBetweenLines;
	}

	public void run()
	{
		while (true)
		{
			try
			{
				String line = this.in.readLine();
				// TODO remove debug
				if (line == null)
					throw new Exception();
				System.out.println(line);
				this.out.println(line);
			}
			catch (Exception e)
			{
				System.out.println("End");
				return;
			}

			try
			{
				Thread.sleep(this.pause);
			}
			catch (InterruptedException e)
			{
				// Ignoring it
			}
		}
	}
}