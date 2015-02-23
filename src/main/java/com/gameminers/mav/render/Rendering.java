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

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.opengl.ImageIOImageData;

public class Rendering {
	public static void drawPolygon(float x, float y, float radius, float r, float g, float b, float a, int count, float z) {
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
	
	public static void drawRectangle(float x, float y, float width, float height, float r, float g, float b, float a, float z) {
		GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glTranslatef(0, 0, z);
			GL11.glColor4f(r, g, b, a);
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2f(x, y);
				GL11.glVertex2f(x+width, y);
				GL11.glVertex2f(x+width, y+height);
				GL11.glVertex2f(x, y+height);
			GL11.glEnd();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}
	
	public static void setUpDisplay() throws LWJGLException, IOException {
		Display.setDisplayMode(new DisplayMode(320, 480));
		Display.setTitle("Mav");
		Display.setResizable(true);
		Display.setIcon(new ByteBuffer[] {
				new ImageIOImageData().imageToByteBuffer(ImageIO.read(ClassLoader.getSystemResource("resources/images/mav-16.png")), false, true, null),
				new ImageIOImageData().imageToByteBuffer(ImageIO.read(ClassLoader.getSystemResource("resources/images/mav-32.png")), false, true, null),
				new ImageIOImageData().imageToByteBuffer(ImageIO.read(ClassLoader.getSystemResource("resources/images/mav-64.png")), false, true, null)
		});
		try {
			Display.create(new PixelFormat(24, 8, 8, 8, 8));
		} catch (LWJGLException e) {
			// try again a couple times with less samples
			try {
				Display.create(new PixelFormat(24, 8, 8, 8, 4));
			} catch (LWJGLException ex) {
				// no catch block here, if even this fails then we won't be able to get a reasonable context
				Display.create(new PixelFormat(24, 8, 8, 8, 0));
			}
		}
	}

	public static void setUpGL() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);

		GL11.glClearDepth(1);
		
		GL11.glShadeModel(GL11.GL_FLAT);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static void beforeFrame() {
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 10, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public static float tend(float a, float b, float c) {
		if (a > b) {
			float diff = (a - b);
			if (diff < 0.1f)
				return b;
			return a - (diff/c);
		} else {
			float diff = (b - a);
			if (diff < 0.1f)
				return b;
			return a + (diff/c);
		}
	}
}
