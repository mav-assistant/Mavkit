/*
 * This file is part of Mavkit.
 *
 * Mavkit is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Mavkit is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mavkit. If not, see <http://www.gnu.org/licenses/>.
 */

package com.unascribed.mavkit.internal.canvas.gles;

import static org.lwjgl.nanovg.NanoVGGLES3.*;

import com.unascribed.mavkit.Mavkit;

/**
 * Implementation of Canvas on top of NanoVG, using OpenGL 3.
 */
public class NanoVGGLES3Canvas extends NanoVGGLESCanvas {
	public NanoVGGLES3Canvas(Mavkit mav) {
		super(mav, nvgCreateGLES3(NVG_ANTIALIAS | NVG_STENCIL_STROKES));
	}
}
