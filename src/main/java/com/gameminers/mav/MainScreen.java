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

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

public class MainScreen extends Screen {
	@Override
	public void render() {
		float w = Display.getWidth()/2f;
		GL11.glPushMatrix();
			GL11.glTranslatef(Display.getWidth()/2f, 10+(w/2f), 0);
			Mav.personality.renderFace(w);
		GL11.glPopMatrix();
		int y = (int) w;
		String[] split = Mav.text.split("\n");
		for (String s : split) {
			TrueTypeFont font = lightFont[1];
			if (s.startsWith("\u00A7l")) {
				font = baseFont[1];
				s = s.substring(2);
			} else if (s.startsWith("\u00A7L")) {
				font = lightFont[2];
				s = s.substring(2);
			} else if (s.startsWith("\u00A7s")) {
				font = lightFont[0];
				s = s.substring(2);
			}
			font.drawString((Display.getWidth()/2)-(font.getWidth(s)/2), y, s, Color.white);
			y+=font.getHeight();
		}
	}

}
