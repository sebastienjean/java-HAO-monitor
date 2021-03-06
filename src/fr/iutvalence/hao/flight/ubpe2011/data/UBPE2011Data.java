package fr.iutvalence.hao.flight.ubpe2011.data;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import fr.iutvalence.hao.monitor.core.exceptions.MalformedFrameException;

public class UBPE2011Data implements Serializable
{
	// #USAINBALL,001330,4454.91506N,00454.88154E,,CC,V,00,,,0,0,350,601,38,0,563,539,564,554,577,350,344,267,967,111242,001

	public final static int FRAME_LENGTH = 27;

	public final static String FRAME_TOKENS_SEPARATOR = ",";

	public final static String[] FRAME_TOKENS_NAMES = { "objectName", "dateGPS", "latGPS", "longGPS", "altGPS", "checkGPS", "fixGPS", "numSatsGPS", "speedGPS",
			"bearingGPS", "resets", "frameNumber", "tempIn", "tempOut", "pressure", "vario", "hygro", "lux1", "lux2", "lux3", "lux4", "accX", "accY", "accZ",
			"voltage", "dateLoc", "requests" };

	public final static String[] FRAME_TOKENS_REGEX = { null, "(\\d{6})|(^$)", "(\\d{4}.\\d{5}(N|S))|(^$)", "(\\d{5}.\\d{5}(E|O|W))|(^$)",
			"((-)?\\d{1,5}.\\d)|(^$)", "(\\d|[A-F]){2}", "(V|A)|(^$)", "(\\d{2})|(^$)", "(\\d{1,3}.\\d)|(^$)", "(\\d{1,3}.\\d)|(^$)", "\\d{1,5}", "\\d{1,5}",
			"(\\d{1,4})|(^$)", "(\\d{1,4})|(^$)", "(\\d{1,4})|(^$)", "(\\d{1,4})|(^$)", "(\\d{1,4})|(^$)", "(\\d{1,4})|(^$)", "(\\d{1,4})|(^$)",
			"(\\d{1,4})|(^$)", "(\\d{1,4})|(^$)", "(\\d{1,4})|(^$)", "(\\d{1,4})|(^$)", "(\\d{1,4})|(^$)", "(\\d{1,4})|(^$)", "(\\d{6})|(^$)",
			"(\\d{1,3})|(^$)" };

	private final byte[] rawFrame;

	private String[] frameTokens;

	/*
	 * ObjectName dateGPS; latitudeGPS; longitudeGPS; altitudeGPS; checksumGPS;
	 * fixGPS; numSatGPS; speedGPS; bearingGPS; resetCounter; frameNumber;
	 * temperatureIn; temperatureOut; pressure; vario; hygro; lux1; lux2; lux3;
	 * lux4; accX; accY; accZ; voltage; dateLoc; requests;
	 */

	public UBPE2011Data(byte[] ubpeFrame) throws MalformedFrameException
	{
		String ubpeString;
		try
		{
			ubpeString = new String(ubpeFrame, "US-ASCII");
		}
		catch (UnsupportedEncodingException e)
		{
			throw new MalformedFrameException();
		}
		this.frameTokens = ubpeString.split(",");

		if (this.frameTokens.length != 27)
			throw new MalformedFrameException();
		if (!this.isValidData())
		{
			throw new MalformedFrameException();

		}

		this.rawFrame = ubpeFrame;
	}

	public boolean isValidData()
	{
		for (int i = 0; i < FRAME_LENGTH; i++)
		{
			String regex = FRAME_TOKENS_REGEX[i];
			if (regex == null)
				continue;
			if (!(Pattern.matches(regex, frameTokens[i])))
			{
				System.out.println("Frame corrupted on field " + FRAME_TOKENS_NAMES[i]);
				return false;
			}
		}
		return true;
	}

	public String[] getFrameTokens()
	{
		return this.frameTokens;
	}

	public byte[] getRawFrame()
	{
		return this.rawFrame.clone();
	}

	public String toString()
	{
		try
		{
			return new String(this.getRawFrame(), "US-ASCII");
		}
		catch (UnsupportedEncodingException e)
		{
			return null;
		}
	}
}
