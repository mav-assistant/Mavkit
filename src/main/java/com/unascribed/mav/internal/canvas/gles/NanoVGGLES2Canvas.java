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

package com.unascribed.mav.internal.canvas.gles;

import static org.lwjgl.nanovg.NanoVGGLES2.*;

import com.unascribed.mav.Mav;

/**
 * Implementation of Canvas on top of NanoVG, using OpenGL ES 2.
 */
public class NanoVGGLES2Canvas extends NanoVGGLESCanvas {
	public NanoVGGLES2Canvas(Mav mav) {
		super(mav, nvgCreateGLES2(NVG_ANTIALIAS | NVG_STENCIL_STROKES));
	}
}
