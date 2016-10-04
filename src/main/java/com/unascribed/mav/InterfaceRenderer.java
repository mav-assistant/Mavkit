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

package com.unascribed.mav;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unascribed.mav.Canvas.Color;
import com.unascribed.mav.Canvas.Font;

public class InterfaceRenderer {
	private static final Logger log = LoggerFactory.getLogger("InterfaceRenderer");
	
	private Mav mav;
	
	public Font robotoLight;
	public Font robotoRegular;
	public Font robotoMedium;
	
	public Font openSansRegular;
	
	public InterfaceRenderer(Mav mav) {
		this.mav = mav;
	}
	
	private void initialize() {
		robotoLight = mav.getDisplay().getCanvas().createFont("Roboto Light", "Roboto-Light.ttf");
		robotoRegular = mav.getDisplay().getCanvas().createFont("Roboto Regular", "Roboto-Regular.ttf");
		robotoMedium = mav.getDisplay().getCanvas().createFont("Roboto Medium", "Roboto-Medium.ttf");
	}
	
	Color teal, black;
	
	public void run() {
		initialize();
		Canvas canvas = mav.getDisplay().getCanvas();
		teal = canvas.colorFromPackedARGB(0x0500FFAA);
		black = canvas.colorFromPackedRGB(0x000000);
		while (!mav.getDisplay().shouldClose()) {
			canvas.beginFrame();
			render(canvas);
			canvas.endFrame();
			mav.getDisplay().swap();
		}
	}

	private void render(Canvas canvas) {
		canvas.save();
		canvas.beginPath();
		canvas.fillStyle(black);
		canvas.textSize(24);
		canvas.fontFace(robotoRegular);
		canvas.drawText(50, 50, "Hello, World!");
		canvas.fill();
		canvas.restore();
	}

}
