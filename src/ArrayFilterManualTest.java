/******************************************************************************
 * Copyright (c) 2012, Anthony GELIBERT                                       *
 * All rights reserved.                                                       *
 *                                                                            *
 * Redistribution and use in source and binary forms, with or without         *
 * modification, are permitted provided that the following conditions are met:*
 *                                                                            *
 *     * Redistributions of source code must retain the above copyright       *
 * notice, this list of conditions and the following disclaimer.              *
 *                                                                            *
 *     * Redistributions in binary form must reproduce the above copyright    *
 * notice, this list of conditions and the following disclaimer in the        *
 * documentation and/or other materials provided with the distribution.       *
 *                                                                            *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS        *
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED  *
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A            *
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER   *
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,   *
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,       *
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR        *
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY        *
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT               *
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE      *
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.       *
 ******************************************************************************/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import fr.iutvalence.hao.monitor.misc.AffTransform;
import fr.iutvalence.hao.monitor.misc.ArrayFilter;

/**
 * Minimal manual test.
 * 
 * @author Anthony Gelibert
 * @version 1.0.0
 */
public class ArrayFilterManualTest
{
	/**
	 * Main.
	 * 
	 * @param args
	 *            none
	 * 
	 * @throws FileNotFoundException
	 *             Can't find the files.
	 * @throws IOException
	 *             I/O exception with the files.
	 * @throws JSONException
	 *             JSON exception.
	 */
	public static void main(final String... args) throws FileNotFoundException, IOException, JSONException
	{
		final File res = new File("res.txt");
		if (res.delete())
		{
			System.out.println("Remove old files...");
		}
		if (!res.createNewFile())
		{
			System.out.println("Serious problem... That shouldn't happen");
		}
		final Map<Integer, AffTransform> map = new HashMap<Integer, AffTransform>(1);
		map.put(13, new AffTransform(1.0f, 5.3923f));
		ArrayFilter.jsonFilter(new File("test.js"), res, new int[] { 1, 2, 0, 13 }, map, 0, "IUT-FM");

	}
}
