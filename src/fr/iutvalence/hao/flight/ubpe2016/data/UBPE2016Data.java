package fr.iutvalence.hao.flight.ubpe2016.data;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import fr.iutvalence.hao.monitor.core.exceptions.MalformedFrameException;

public class UBPE2016Data implements Serializable
{

	// #HAO_2016,1182,32,1045,170404,150407,A,457.328,4458.061,169.0,1.2,208.0,5,2.3,829,29736,29826,-497,38,-32158,-32161,242,290

	public final static String FRAME_TOKENS_SEPARATOR = ",";

	public final static String[] FRAME_TOKENS_NAMES = 
		{ 
			"objectName", "frameCounter", "resetCounter", "secondsSinceLastReset", "timeRTC", 
			"timeGPS", "fixGPS", "longGPS", "latGPS", "altGPS", 
			"speedGPS", "courseGPS", "numSatsGPS", "hdopGPS", "boardTemperatureAnalogSensor", 
			"externalTemperatureAnalogSensor", "internalTemperatureAnalogSensor", "externalHumidityAnalogSensor", "differentialPressureAnalogSensor", 
			"visibleLuminosityAnalogSensor","irLuminosityAnalogSensor",
			"batteryTemperatureAnalogSensor", "batteryVoltageAnalogSensor" 
		};

	public final static String[] FRAME_TOKENS_REGEX = 
		{ 
			null, 						// objectName
			"\\d{1,5}", 				// frameCounter
			"\\d{1,5}", 				// resetCounter 
			"\\d{1,5}", 				// secondsSinceLastReset 
			"\\d{6}", 					// timeRTC
			"\\d{6}", 					// timeGPS	 
			"V|A", 						// fixGPS 
			"(-)?\\d{1,5}.\\d{3}", 		// longGPS
			"(-)?\\d{1,4}.\\d{3}", 		// latGPS
			"(-)?\\d{1,5}.\\d{1}", 		// altGPS 
			"\\d{1,3}.\\d{1}", 			// speedGPS
			"\\d{1,3}.\\d{1}", 			// courseGPS
			"\\d{1,2}", 				// numSatsGPS
			"\\d{1,3}.\\d{1}", 			// hdopGPS
			"\\d{1,4}", 				// boardTemperatureAnalogSensor
			"(-)?\\d{1,5}", 			// externalTemperatureAnalogSensor
			"(-)?\\d{1,5}", 			// internalTemperatureAnalogSensor
			"(-)?\\d{1,5}", 			// externalHumidityAnalogSensor
			"\\d{1,4}", 				// differentialPressureAnalogSensor 
			"(-)?\\d{1,5}",				// visibleLuminosityAnalogSensor
			"(-)?\\d{1,5}",				// irLuminosityAnalogSensor
			"\\d{1,4}",					// batteryTemperatureAnalogSensor
			"\\d{1,4}"					// batteryVoltageAnalogSensor
		};

	private final byte[] rawFrame;

	private String[] frameTokens;

	/*
	 * objectName, frameCounter, resetCounter, secondsSinceLastReset, timeRTC, 
	 * timeGPS, fixGPS, longGPS, latGPS, altGPS,
	 * speedGPS, courseGPS, numSatsGPS, hdopGPS, internalTemperatureAnalogSensor, 
	 * externalTemperatureAnalogSensor, middleTemperatureAnalogSensor, externalHumidityAnalogSensor, differentialPressureAnalogSensor,  
	 * visibleLuminosityAnalogSensor,irLuminosityAnalogSensor,
	 * batteryTemperatureAnalogSensor, batteryVoltageAnalogSensor 
	 */
	public UBPE2016Data(byte[] ubpeFrame) throws MalformedFrameException
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
