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

package fr.iutvalence.hao.monitor.misc;

/**
 * Affine Transformation (y = Ax+B).
 * 
 * @author Anthony Gelibert
 * @version 1.0.0
 */
public final class AffTransform
{
	/** Gradient. */
	private final float m_gradient;
	/** Y-intercept. */
	private final float m_yintercept;

	/**
	 * Create a new transform with A and B.
	 * 
	 * @param coefA
	 *            Gradient.
	 * @param coefB
	 *            Y-intercept.
	 */
	public AffTransform(final float coefA, final float coefB)
	{
		m_gradient = coefA;
		m_yintercept = coefB;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder("AffTransform");
		sb.append("{m_gradient=").append(m_gradient);
		sb.append(", m_yintercept=").append(m_yintercept);
		sb.append('}');
		return sb.toString();
	}

	/**
	 * Apply the affine transformation on "x".
	 * 
	 * @param x
	 *            X.
	 * @return Y = Ax+B
	 */
	public double applyOn(final int x)
	{
		double y = m_gradient * x + m_yintercept;
		// To keep only two digits (X.yy).
		y = Math.floor(y * 100);
		y /= 100;
		return y;
	}

	/**
	 * Get the gradient.
	 * 
	 * @return The gradient.
	 */
	public float getA()
	{
		return m_gradient;
	}

	/**
	 * Get the Y-Intercept.
	 * 
	 * @return The y-intercept.
	 */
	public float getB()
	{
		return m_yintercept;
	}
}
