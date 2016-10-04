/*
 * This file is part of Mav.
 *
 * Mav is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Mav is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mav. If not, see <http://www.gnu.org/licenses/>.
 */

package com.unascribed.mav.internal.canvas.gl;

import static org.lwjgl.opengl.GL11.*;

import com.unascribed.mav.Mav;
import com.unascribed.mav.internal.canvas.NanoVGCanvas;

public abstract class NanoVGGLCanvas extends NanoVGCanvas {

	public NanoVGGLCanvas(Mav mav, long ctx) {
		super(mav, ctx);
		glClearColor(1, 1, 1, 1);
	}
	
	@Override
	public void beginFrame() {
		glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		super.beginFrame();
	}

}
