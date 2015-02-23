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
	
	public static float targetSat = 1.0f;
	public static float lagSat = 0.0f;
	
	public static float targetDim = 0.0f;
	public static float lagDim = 0.3f;
	
	public static float[] lagRGB = {0, 0, 0};
	
	public static boolean idle = true;
	public static String text = "What can I do for you?";
	
	public static float[] getColor(float lum) {
		return new float[] { lagRGB[0]*lum, lagRGB[1]*lum, lagRGB[2]*lum };
	}
	
	public static void update() {
		float[] targetRGB = new Color(Color.HSBtoRGB(targetHue/360f, lagSat, 1.0f-lagDim)).getComponents(null);
		lagRGB[0] = Rendering.tend(lagRGB[0], targetRGB[0], 16f);
		lagRGB[1] = Rendering.tend(lagRGB[1], targetRGB[1], 16f);
		lagRGB[2] = Rendering.tend(lagRGB[2], targetRGB[2], 16f);
		lagSat = Rendering.tend(lagSat, targetSat, 24f);
		lagDim = Rendering.tend(lagDim, targetDim, 32f);
	}

}
