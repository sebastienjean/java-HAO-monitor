package fr.iutvalence.hao.monitor.misc;

/**
 * Utility class providing methods dedicated to byte/byte array conversions to
 * hex strings.<br/>
 * 
 * @author SŽbastien JEAN (sebastien.jean@iut-valence.fr), IUT Valence.
 * @version 1.0, mar. 2010.
 */
public class HexUtils
{
	/**
	 * Constant array for hexadecimal digits.
	 */
	private final static char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * Converting a byte into an ASCII string where byte is represented by its
	 * hexadecimal value with 0x as prefix.
	 * 
	 * @param b
	 *            the byte to convert.
	 * 
	 * @return an ASCII string where <tt>b</tt> is represented by its
	 *         hexadecimal value with 0x as prefix.
	 */
	public static String byteToHexString(byte b)
	{
		return "0x" + HEX_DIGITS[((b >> 4) & 0x0F)] + HEX_DIGITS[(b & 0x0F)];
	}

	/**
	 * Converting an array of bytes into an ASCII string where each byte is
	 * represented by its hexadecimal value with 0x as prefix and space as a
	 * suffix.
	 * 
	 * @param array
	 *            the array of bytes to convert.
	 * 
	 * @return an ASCII string where each byte of <tt>array</tt> is represented
	 *         by its hexadecimal value with 0x as prefix and space as a suffix.
	 */
	public static String byteArrayToHexString(byte[] array)
	{
		String result = "";
		for (int i = 0; i < array.length; i++)
		{
			result += byteToHexString(array[i]) + " ";
		}
		return result;
	}

}
