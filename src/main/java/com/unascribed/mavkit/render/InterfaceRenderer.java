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

package com.unascribed.mavkit.render;

import org.checkerframework.checker.guieffect.qual.UIEffect;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unascribed.mavkit.Mavkit;

public class InterfaceRenderer {
	private static final Logger log = LoggerFactory.getLogger("InterfaceRenderer");
	private static final double NANOSECOND_RECIPROCAL = 1/1000000000D;
	
	@MonotonicNonNull
	private Mavkit mav;
	
	@Nullable
	public Renderable currentScreen;
	public double timeElapsed;
	
	
	@UIEffect
	@EnsuresNonNull("this.mav")
	public void start(Mavkit mav) {
		this.mav = mav;
	}
	
	@UIEffect
	@RequiresNonNull("this.mav")
	public void run() {
		Canvas canvas = mav.getDisplay().getCanvas();
		long lastTime = System.nanoTime();
		while (!mav.getDisplay().shouldClose()) {
			long curTime = System.nanoTime();
			double delta = nanosToSeconds(curTime-lastTime);
			lastTime = curTime;
			
			timeElapsed += delta;
			
			canvas.beginFrame();
			if (currentScreen != null) {
				currentScreen.render(canvas, delta);
			}
			canvas.endFrame();
			mav.getDisplay().swap();
		}
	}

	private double nanosToSeconds(long l) {
		return l*NANOSECOND_RECIPROCAL;
	}

}
