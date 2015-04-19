package fr.iutvalence.hao.flight.ubpe2015.data;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import fr.iutvalence.hao.monitor.core.exceptions.MalformedFrameException;

public class UBPE2015Data implements Serializable
{

	// #CHARLIE26,1333,148,12,160409,140405,V,457.343,4458.046,0.0,0.0,0.0,1,0.0,853,0,0,0,11,292,287,358,0,0,627,3570,857

	public final static String FRAME_TOKENS_SEPARATOR = ",";

	public final static String[] FRAME_TOKENS_NAMES = 
		{ 
			"objectName", "frameCounter", "resetCounter", "secondsSinceLastReset", "timeRTC", 
			"timeGPS", "fixGPS", "longGPS", "latGPS", "altGPS", 
			"speedGPS", "courseGPS", "numSatsGPS", "hdopGPS", "internalTemperatureAnalogSensor", 
			"externalTemperatureAnalogSensor", "middleTemperatureAnalogSensor", "externalHumidityAnalogSensor", "differentialPressureAnalogSensor", "xAccelerationAnalogSensor",
			"yAccelerationAnalogSensor", "zAccelerationAnalogSensor", "visibleLuminosityAnalogSensor","irLuminosityAnalogSensor",
			"batteryTemperatureAnalogSensor", "headingPseudoAnalogSensor", "batteryVoltageAnalogSensor" 
		};

	public final static String[] FRAME_TOKENS_REGEX = 
		{ 
			null, "\\d{1,5}", "\\d{1,5}", "\\d{1,5}", "\\d{6}", 
			"\\d{6}", "V|A", "(-)?\\d{1,5}.\\d{3}", "(-)?\\d{1,4}.\\d{3}", "(-)?\\d{1,5}.\\d{1}", 
			"\\d{1,3}.\\d{1}", "\\d{1,3}.\\d{1}", "\\d{1,2}", "\\d{1,3}.\\d{1}", "\\d{1,4}", 
			"\\d{1,5}", "\\d{1,5}", "\\d{1,5}", "\\d{1,4}", "\\d{1,4}", 
			"\\d{1,4}", "\\d{1,4}", "\\d{1,5}", "\\d{1,5}", 
			"\\d{1,4}", "\\d{1,4}", "\\d{1,4}"
		};

	private final byte[] rawFrame;

	private String[] frameTokens;

	/*
	 * objectName, frameCounter, resetCounter, secondsSinceLastReset, timeRTC, 
	 * timeGPS, fixGPS, longGPS, latGPS, altGPS,
	 * speedGPS, courseGPS, numSatsGPS, hdopGPS, internalTemperatureAnalogSensor, 
	 * externalTemperatureAnalogSensor, middleTemperatureAnalogSensor, externalHumidityAnalogSensor, differentialPressureAnalogSensor, xAccelerationAnalogSensor,
	 * yAccelerationAnalogSensor, zAccelerationAnalogSensor, visibleLuminosityAnalogSensor,irLuminosityAnalogSensor,
	 * batteryTemperatureAnalogSensor, headingPseudoAnalogSensor, batteryVoltageAnalogSensor 
	 */
	public UBPE2015Data(byte[] ubpeFrame) throws MalformedFrameException
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
