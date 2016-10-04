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

package com.unascribed.mav.render;

import org.checkerframework.checker.guieffect.qual.UIEffect;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unascribed.mav.Mav;
import com.unascribed.mav.Panic;
import com.unascribed.mav.render.Canvas.Font;

public class InterfaceRenderer {
	private static final Logger log = LoggerFactory.getLogger("InterfaceRenderer");
	private static final double NANOSECOND_RECIPROCAL = 1/1000000000D;
	
	@MonotonicNonNull
	private Mav mav;
	
	@MonotonicNonNull
	public Font robotoLight;
	@MonotonicNonNull
	public Font robotoRegular;
	@MonotonicNonNull
	public Font robotoMedium;
	
	@Nullable
	public Renderable currentScreen;
	public double timeElapsed;
	
	
	@UIEffect
	@EnsuresNonNull({"this.mav", "this.robotoLight", "this.robotoRegular", "this.robotoMedium"})
	public void start(Mav mav) {
		this.mav = mav;
		Font robotoLight = mav.getDisplay().getCanvas().createFont("Roboto Light", "Roboto-Light.ttf");
		Font robotoRegular = mav.getDisplay().getCanvas().createFont("Roboto Regular", "Roboto-Regular.ttf");
		Font robotoMedium = mav.getDisplay().getCanvas().createFont("Roboto Medium", "Roboto-Medium.ttf");
		if (robotoLight == null) throw new Panic("panic.fontLoadFailed", "Roboto Light");
		if (robotoRegular == null) throw new Panic("panic.fontLoadFailed", "Roboto Regular");
		if (robotoMedium == null) throw new Panic("panic.fontLoadFailed", "Roboto Medium");
		this.robotoLight = robotoLight;
		this.robotoRegular = robotoRegular;
		this.robotoMedium = robotoMedium;
	}
	
	@UIEffect
	@RequiresNonNull({"this.mav", "this.robotoLight", "this.robotoRegular", "this.robotoMedium"})
	public void run() {
		Canvas canvas = mav.getDisplay().getCanvas();
		long lastTime = System.nanoTime();
		while (!mav.getDisplay().shouldClose()) {
			long curTime = System.nanoTime();
			double delta = nanosToSeconds(curTime-lastTime);
			lastTime = curTime;
			
			timeElapsed += delta;
			
			System.out.println(timeElapsed);
			
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
