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

package fr.iutvalence.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * JSON array filter.
 *
 * @author Anthony Gelibert
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class ArrayFilter
{
    /**
     * File to String.
     *
     * @param file The file to process.
     *
     * @return The resulting String.
     *
     * @throws FileNotFoundException File not found.
     * @throws IOException           I/O exception with the file.
     */
    private static String fileToString(final File file)
            throws FileNotFoundException, IOException

    {
        final byte[] buffer = new byte[(int) file.length()];
        final FileInputStream fis = new FileInputStream(file);
        try
        {
            if (fis.read(buffer) != buffer.length)
            {
                throw new IOException("Can't read all the chars");
            }
        }
        finally
        {
            fis.close();
        }
        return new String(buffer);
    }

    /**
     * String to File.
     *
     * @param src  String.
     * @param dest File.
     *
     * @throws FileNotFoundException Destination file not found.
     * @throws IOException           I/O exception with the file.
     */
    private static void stringToFile(final String src, final File dest)
            throws FileNotFoundException, IOException
    {
        final FileOutputStream fos = new FileOutputStream(dest);
        try
        {
            fos.write(src.getBytes());
        }
        finally
        {
            fos.close();
        }
    }

    /**
     * Remove all the "//@@EVENT@@//" markers.
     *
     * @param src The initial String.
     *
     * @return The resulting Strings.
     */
    private static String cleanMarkers(final String src)
    {
        return src;
        //    return src.replaceAll("//@@EVENT@@//", "");
    }

    /**
     * Prune the empty columns from the array of indexes concerning a specific
     * JSONArray.
     * <p/>
     * TODO Ask to Sebastien if a column can be partially empty because here, I
     * consider NO.
     *
     * @param array   The JSON array.
     * @param initial The initial indexes.
     *
     * @return Array of full indexes.
     *
     * @throws JSONException Exception during the JSON processing.
     */
    private static int[] pruneEmptyColumns(final JSONArray array,
                                           final int[] initial)
            throws JSONException
    {
        final JSONArray inputLine = array.getJSONArray(0);
        final int[] prePruned = new int[initial.length];

        int j = 0;
        for (final int i : initial)
        {
            if (!inputLine.get(i).equals(JSONObject.NULL))
            {
                prePruned[j++] = i;
            }
        }
        final int[] pruned = new int[j];
        System.arraycopy(prePruned, 0, pruned, 0, j);
        return pruned;
    }

    /**
     * Filtering a JSON Array file.
     * <p/>
     * Create a new JSON array with only the columns indicated by the
     * <i>offsets</i> parameter.
     *
     * @param src     File containing the initial JSON Array.
     * @param dest    File produced.
     * @param offsets Index of the colmuns to keep.
     *
     * @throws JSONException         Exception during the JSON processing.
     * @throws FileNotFoundException Can't find src or dest.
     * @throws IOException           I/O exception with src or dest.
     */
    public static void jsonFilter(final File src,
                                  final File dest,
                                  final int[] offsets)
            throws JSONException, FileNotFoundException, IOException
    {
        // Just convert file to string and call the corresponding method.
        jsonFilter(fileToString(src), dest, offsets);
    }

    /**
     * Filtering a JSON Array file.
     * <p/>
     * Create a new JSON array with only the columns indicated by the
     * <i>offsets</i> parameter. If the <i>offset</i> column is a key in the
     * <i>transform</i> parameter, the corresponding AffTransform is applied.
     *
     * @param src       File containing the initial JSON Array.
     * @param dest      File produced.
     * @param offsets   Index of the colmuns to keep.
     * @param transform All the AffTransform to apply.
     *
     * @throws JSONException         Exception during the JSON processing.
     * @throws FileNotFoundException Can't find src or dest.
     * @throws IOException           I/O exception with src or dest.
     */
    public static void jsonFilter(final File src,
                                  final File dest,
                                  final int[] offsets,
                                  final Map<Integer, AffTransform> transform)
            throws JSONException, FileNotFoundException, IOException
    {
        // Just convert file to string and call the corresponding method.
        jsonFilter(fileToString(src), dest, offsets, transform, -1, null);
    }

    /**
     * Filtering a JSON Array file.
     * <p/>
     * Create a new JSON array with only the columns indicated by the
     * <i>offsets</i> parameter. If the <i>offset</i> column is a key in the
     * <i>transform</i> parameter, the corresponding AffTransform is applied.
     *
     * @param src          File containing the initial JSON Array.
     * @param dest         File produced.
     * @param offsets      Index of the colmuns to keep.
     * @param transform    All the AffTransform to apply.
     * @param filterIndex  Index of the filter column.
     * @param filterString String to filter.
     *
     * @throws JSONException         Exception during the JSON processing.
     * @throws FileNotFoundException Can't find src or dest.
     * @throws IOException           I/O exception with src or dest.
     */
    public static void jsonFilter(final File src,
                                  final File dest,
                                  final int[] offsets,
                                  final Map<Integer, AffTransform> transform,
                                  final int filterIndex,
                                  final String filterString)
            throws JSONException, FileNotFoundException, IOException
    {
        // Just convert file to string and call the corresponding method.
        jsonFilter(fileToString(src), dest, offsets, transform, filterIndex, filterString);
    }


    /**
     * Filtering a JSON Array.
     * <p/>
     * Create a new JSON array with only the columns indicated by the
     * <i>offsets</i> parameter.
     *
     * @param src     String containing the initial JSON Array.
     * @param dest    File produced.
     * @param offsets Index of the colmuns to keep.
     *
     * @throws JSONException         Exception during the JSON processing.
     * @throws FileNotFoundException Can't find dest.
     * @throws IOException           I/O exception with dest.
     */
    public static void jsonFilter(final String src,
                                  final File dest,
                                  final int[] offsets)
            throws JSONException, FileNotFoundException, IOException

    {
        // Just create an empty transform Map and call the corresponding method.
        // I think it's not a good point for performance but it simplifies the
        // code. Maybe by enhancing the real method we can correct the
        // performance problem.
        jsonFilter(src, dest, offsets, new HashMap<Integer, AffTransform>(0), -1, null);
    }

    /**
     * Filtering a JSON Array.
     * <p/>
     * Create a new JSON array with only the columns indicated by the
     * <i>offsets</i> parameter.
     * <p/>
     * If the <i>offset</i> column is a key in the <i>transform</i> parameter,
     * the corresponding AffTransform is applied.
     * <p/>
     * If the <i>filterIndex</i> isn't -1, the row is only added if the column
     * <i>filterIndex</i> contains <i>filterString</i>.
     *
     * @param src          String containing the initial JSON Array.
     * @param dest         File produced.
     * @param offsets      Index of the colmuns to keep.
     * @param transform    All the AffTransform to apply.
     * @param filterIndex  Index of the filter column.
     * @param filterString String to filter.
     *
     * @throws JSONException         Exception during the JSON processing.
     * @throws FileNotFoundException Can't find dest.
     * @throws IOException           I/O exception with dest.
     */
    public static void jsonFilter(final String src,
                                  final File dest,
                                  final int[] offsets,
                                  final Map<Integer, AffTransform> transform,
                                  final int filterIndex,
                                  final String filterString)
            throws JSONException, FileNotFoundException, IOException
    {
        // Clean the markers. Comments aren't allowed in JSONArray.
        final JSONArray input = new JSONArray(cleanMarkers(src));
        final JSONArray output = new JSONArray();
        //final int[] realOffsets = pruneEmptyColumns(input, offsets);

        for (int i = 0; i < input.length(); i++)
        {
            final JSONArray inputLine = input.getJSONArray(i);

            if ((filterIndex != -1) && !((String) inputLine.get(filterIndex)).equals(filterString))
            {
                continue;
            }

            final JSONArray outputLine = new JSONArray();
            // The colmuns to keep
            //for (final int j : realOffsets)
            boolean validLine = true;
            for (final int j : offsets)
            {
            	if (inputLine.isNull(j)) 
            	{
            		validLine = false;
            		break;
            	}
               
            	final Object value = inputLine.get(j);
            	
            	try
            	{
            		// filter empty string
            		if (((String) value).length() == 0)
            		{
            			validLine = false;
                		break;
            		}
            	}
            	catch (Exception e) {}
            	
                
                // Search an AffTransform
                final AffTransform trans = transform.get(j);
                outputLine.put(trans != null ? trans.applyOn((Integer) value) : value);
            }
            if (validLine) output.put(outputLine);
        }
        stringToFile(output.toString(), dest);
    }

    /** No constructor. */
    private ArrayFilter()
    {}
}
