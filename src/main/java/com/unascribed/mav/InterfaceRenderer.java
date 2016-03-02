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

package com.unascribed.mav;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

public class InterfaceRenderer {

	private long windowHandle;
	private GLCapabilities caps;
	
	private void initialize() {
		glfwInit();
		glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_API);
		windowHandle = glfwCreateWindow(320, 480, "Mav", 0, 0);
		glfwMakeContextCurrent(windowHandle);
		GL.create();
		caps = GL.createCapabilities();
		
	}
	
	public void run() {
		initialize();
	}

}
