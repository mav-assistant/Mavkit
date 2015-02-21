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
package com.gameminers.mav.component;

import org.lwjgl.opengl.GL11;

public abstract class Component {
	protected float x;
	protected float y;
	protected float z;
	protected float width;
	protected float height;
	
	public final void render() {
		GL11.glPushMatrix();
			GL11.glTranslatef(x, y, z);
			doRender();
		GL11.glPopMatrix();
	}
	public abstract void keyDown(int k, char c, long nanos);
	public abstract void keyUp(int k, char c, long nanos);
	
	public abstract void mouseMove(int x, int y, long nanos);
	public abstract void mouseDown(int x, int y, int button, long nanos);
	public abstract void mouseUp(int x, int y, int button, long nanos);
	public abstract void mouseWheel(int x, int y, int dWheel, long nanos);
	
	public abstract void doRender();
	
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public float getWidth() {
		return width;
	}
	public float getHeight() {
		return height;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	public void setY(float y) {
		this.y = y;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	
}
