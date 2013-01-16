package fr.iutvalence.ubpe.misc;

import java.util.Calendar;

/**
 * Utility class providing methods dedicated to byte/byte array conversions to
 * hex strings.<br/>
 * 
 * @author SŽbastien JEAN (sebastien.jean@iut-valence.fr), IUT Valence.
 * @version 1.0, mar. 2010.
 */
public class TimeOfDay
{
	public static void changeTimeofDay(Calendar date, String hourString) throws NumberFormatException
	{
		if (hourString.length() != 6)
			throw new NumberFormatException();

		// hour is in HHmmss format
		int hours = Integer.parseInt(hourString.substring(0, 2));
		int minutes = Integer.parseInt(hourString.substring(2, 4));
		int seconds = Integer.parseInt(hourString.substring(5));

		date.set(Calendar.HOUR_OF_DAY, hours);
		date.set(Calendar.MINUTE, minutes);
		date.set(Calendar.SECOND, seconds);

	}

}
