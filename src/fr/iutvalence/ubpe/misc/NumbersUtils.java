package fr.iutvalence.ubpe.misc;

public class NumbersUtils
{
	public static String removeLeadingZeros(String number)
	{
		if (number.length() == 0) return number;
		while (number.startsWith("0"))
		{
			if (number.length() == 1) return number;
			if (number.charAt(1) == '.') return number;
			number = number.substring(1);
		}
		return number;
	}

}
