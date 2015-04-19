package fr.iutvalence.ubpe.ubpe2013;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import fr.iutvalence.hao.monitor.core.exceptions.MalformedFrameException;

public class UBPE2013Data implements Serializable
{
	// TODO add sample frame here
	// #STRATERRESTRE,1,0,1,0,126,151733,000149,V,454.892,4454.925,0.0,0.0,0.0,0,0.0,308,330,336,321,312,318,46,222,284,1007,515
	public final static String FRAME_TOKENS_SEPARATOR = ",";

	public final static String[] FRAME_TOKENS_NAMES = { "objectName", "frameCounter", "resetCounter", "currentFlightPhaseNumber", "currentFlightPhaseDurationInSeconds",
		    "secondsSinceLastReset", "timeRTC", "timeGPS", "fixGPS", "longGPS", "latGPS", "altGPS", "speedGPS", "courseGPS", "numSatsGPS", "hdopGPS",
		    "internalTemperatureAnalogSensor", "middleTemperatureAnalogSensor","externalTemperatureAnalogSensor", "externalHumidityAnalogSensor", 
		    "differentialPressureAnalogSensor", "upLuminosityAnalogSensor", "side1LuminosityAnalogSensor","side2LuminosityAnalogSensor",
		    "soundLevelAnalogSensor", "batteryTemperatureAnalogSensor", "analogVoltageSensor" };

	public final static String[] FRAME_TOKENS_REGEX = { null, "\\d{1,5}", "\\d{1,5}", "\\d{1}", "\\d{1,5}", "\\d{1,5}", "\\d{6}", "\\d{6}", "V|A",
			"(-)?\\d{1,5}.\\d{3}", "(-)?\\d{1,4}.\\d{3}", "(-)?\\d{1,5}.\\d{1}", "\\d{1,3}.\\d{1}", "\\d{1,3}.\\d{1}", "\\d{1,2}", "\\d{1,3}.\\d{1}",
			"\\d{1,4}", "\\d{1,4}", "\\d{1,4}", "\\d{1,4}", "\\d{1,4}", "\\d{1,4}", "\\d{1,4}", "\\d{1,4}", "\\d{1,4}", "\\d{1,4}", "\\d{1,4}" };

	private final byte[] rawFrame;

	private String[] frameTokens;

	/*
	 * objectName, frameCounter, resetCounter, flightPhaseCounter,
	 * flightPhaseDuration, secondsSinceLastReset, timeRTC, timeGPS, fixGPS,
	 * longGPS, latGPS, altGPS, speedGPS, courseGPS, numSatsGPS, hdopGPS,
	 * analog1, analog2, analog3, analog4, voltage
	 */
	public UBPE2013Data(byte[] ubpeFrame) throws MalformedFrameException
	{
		String ubpeString;
		try
		{
			ubpeString = new String(ubpeFrame, "US-ASCII").trim();
			// TODO remove debug info
			System.out.println("--- " + ubpeString);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new MalformedFrameException();
		}
		this.frameTokens = ubpeString.split(",");

		if (this.frameTokens.length != FRAME_TOKENS_NAMES.length)
		{
			// TODO remove debug
			System.err.println("--- expected " + FRAME_TOKENS_NAMES.length + ", found " + this.frameTokens.length + " tokens");
			throw new MalformedFrameException();
		}
		if (!this.isValidData())
		{
			throw new MalformedFrameException();
		}

		this.rawFrame = ubpeFrame;
	}

	public boolean isValidData()
	{
		for (int i = 0; i < FRAME_TOKENS_NAMES.length; i++)
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
