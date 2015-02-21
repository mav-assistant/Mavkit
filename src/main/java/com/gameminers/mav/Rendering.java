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

public class Rendering {
	public static void drawPolygon(float x, float y, int radius, float r, float g, float b, float a, int count, float z) {
		GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glTranslatef(x, y, z);
			GL11.glColor4f(r, g, b, a);
			GL11.glBegin(GL11.GL_POLYGON);
			for (int i = 0; i < count; ++i) {
				GL11.glVertex2d(Math.sin(i / ((double) count) * 2 * Math.PI) * (radius), Math.cos(i / ((double) count) * 2 * Math.PI) * (radius));
			}
			GL11.glEnd();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}
	public static void drawTriangle(float x, float y, float radius, float r, float g, float b, float a, float z) {
		GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glTranslatef(x, y, z);
			GL11.glColor4f(r, g, b, a);
			GL11.glBegin(GL11.GL_TRIANGLES);
			for (int i = 0; i < 3; ++i) {
				GL11.glVertex2d(Math.sin(i / ((double) 3) * 2 * Math.PI) * (radius), Math.cos(i / 3D * 2 * Math.PI) * (radius));
			}
			GL11.glEnd();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}
}
