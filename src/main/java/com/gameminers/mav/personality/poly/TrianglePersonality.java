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

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.gameminers.mav.render.RenderState;
import com.gameminers.mav.render.Rendering;

public class TrianglePersonality extends PolygonPersonality {
	public TrianglePersonality() {
		super(3);
	}
	@Override
	public void renderForeground(float diameter) {
		GL11.glTranslatef(0, (0.3f-bgLum)*((Display.getHeight()/2f)-10), 0);
		diameter *= Math.max(1.0f, (0.3f-bgLum)*6f);
		setup();
		float radius = diameter/2f;
		float[] bg = RenderState.getColor(bgLum);
		float[] fg = RenderState.getColor(0.8f);
		Rendering.drawTriangle(0, 0, radius*(0.85f+(pulse*0.15f)), fg[0], fg[1], fg[2], 0.5f, 0);
		Rendering.drawTriangle(0, 0, radius*0.8f, fg[0], fg[1], fg[2], 1, 0.5f);
		Rendering.drawTriangle(0, 0, radius*0.7f, bg[0], bg[1], bg[2], 1, 1f);
	}

	@Override
	public void renderIconForeground(int size) {
		GL11.glTranslatef(0, -8, 0);
		float radius = size/2f;
		float[] bg = RenderState.getColor(bgLum);
		float[] fg = RenderState.getColor(0.8f);
		Rendering.drawTriangle(0, 0, radius, fg[0], fg[1], fg[2], 0.5f, 0);
		Rendering.drawTriangle(0, 0, radius*0.7f, fg[0], fg[1], fg[2], 1, 0.5f);
		Rendering.drawTriangle(0, 0, radius*0.4f, bg[0], bg[1], bg[2], 1, 1f);
	}
	
}
