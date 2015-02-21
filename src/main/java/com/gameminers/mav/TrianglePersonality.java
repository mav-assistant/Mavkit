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
	private float angle;
	
	public float targetAngle;
	public float targetPulse;
	@Override
	public void renderFace(float diameter) {
		float radius = diameter/2f;
		frameCount++;
		float pulse;
		if (Mav.idle) {
			angle = (angle+0.05f)%360;
			pulse = (float)(Math.sin(frameCount/30f)+1)/2f;
		} else {
			angle = Mav.tend(angle, targetAngle, 8f);
			pulse = targetPulse;
		}
		GL11.glRotatef(angle, 0f, 0f, 1f);
		float[] bg = Mav.getColor(0.3f);
		float[] fg = Mav.getColor(0.8f);
		Rendering.drawTriangle(0, 0, radius*(0.85f+(float)((pulse)*0.15f)), fg[0], fg[1], fg[2], 0.5f, 0);
		Rendering.drawTriangle(0, 0, radius*0.8f, fg[0], fg[1], fg[2], 1, 0.5f);
		Rendering.drawTriangle(0, 0, radius*0.7f, bg[0], bg[1], bg[2], 1, 1f);
	}

}
