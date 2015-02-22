/*
 * This file is part of Mav.
 *
 * Mav is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Mav is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with Mav. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gameminers.mav.render;

import java.awt.Color;

public class RenderState {

	public static float targetHue = 150;
	public static float lagHue = 150;
	
	public static float targetSat = 1.0f;
	public static float lagSat = 0.0f;
	
	public static float targetDim = 0.0f;
	public static float lagDim = 0.3f;
	
	public static boolean idle = true;
	public static String text = "What can I do for you?";
	
	public static float[] getColor(float lum) {
		return new Color(Color.HSBtoRGB(lagHue/360f, lagSat, lum-lagDim)).getComponents(null);
	}
	
	public static void update() {
		lagHue = Rendering.tend(lagHue, targetHue, 16f);
		lagSat = Rendering.tend(lagSat, targetSat, 24f);
		lagDim = Rendering.tend(lagDim, targetDim, 32f);
	}

}
