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
package com.gameminers.mav.personality.poly;

import org.lwjgl.opengl.GL11;

import com.gameminers.mav.personality.Personality;
import com.gameminers.mav.render.RenderState;
import com.gameminers.mav.render.Rendering;

public class PolygonPersonality implements Personality {
	public int sideCount;
	public PolygonPersonality(int sideCount) {
		this.sideCount = sideCount;
	}
	public long frameCount;
	public float angle;
	public float pulse;
	
	public float targetAngle;
	public float targetPulse;
	@Override
	public void renderBackground() {
		float[] rgb = Rendering.getColor(0.3f);
		GL11.glClearColor(rgb[0], rgb[1], rgb[2], 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
	@Override
	public void renderForeground(float diameter) {
		setup();
		float radius = diameter/2f;
		float[] bg = Rendering.getColor(0.3f);
		float[] fg = Rendering.getColor(0.8f);
		Rendering.drawPolygon(0, 0, radius*(0.85f+(float)((pulse)*0.15f)), fg[0], fg[1], fg[2], 0.5f, sideCount, 0);
		Rendering.drawPolygon(0, 0, radius*0.8f, fg[0], fg[1], fg[2], 1, sideCount, 0.5f);
		Rendering.drawPolygon(0, 0, radius*0.7f, bg[0], bg[1], bg[2], 1, sideCount, 1f);
	}
	protected void setup() {
		frameCount++;
		if (RenderState.idle) {
			angle = (angle+0.05f)%360;
			pulse = (float)(Math.sin(frameCount/30f)+1)/2f;
		} else {
			angle = Rendering.tend(angle, targetAngle, 8f);
			pulse = targetPulse;
		}
		GL11.glRotatef(angle, 0f, 0f, 1f);
	}
	public int getSideCount() {
		return sideCount;
	}
}
