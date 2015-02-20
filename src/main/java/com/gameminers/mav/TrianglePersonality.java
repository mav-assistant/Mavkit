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
package com.gameminers.mav;

import org.lwjgl.opengl.GL11;

public class TrianglePersonality extends Personality {
	private long frameCount;
	@Override
	public void renderFace() {
		frameCount++;
		GL11.glRotatef((frameCount/5f)%360, 0f, 0f, 1f);
		Rendering.drawTriangle(0, 0, 55, 0, 0.4f, 0, 1, 0);
		Rendering.drawTriangle(0, 0, 45, 0, 0.8f, 0, 1, 0.5f);
		Rendering.drawTriangle(0, 0, 40, 0, 0.3f, 0, 1, 1f);
	}

}
