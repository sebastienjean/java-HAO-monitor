import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class CaptureFileTimestampRemover
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		File inputFile = new File(args[0]);
		File OutputFile = new File(args[1]);
		BufferedReader in = null;
		PrintStream out = null;
		try
		{
			in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "US-ASCII"));
			out = new PrintStream(new FileOutputStream(OutputFile), true, "US-ASCII");

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
				if (line == null)
					break;

				line = line.replaceAll("\"(.{19})\",", "");
				if (line.startsWith("$"))
					out.println(line);
			}
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			in.close();
			out.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
