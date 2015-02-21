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

public class MainScreen extends Screen {
	@Override
	public void render() {
		GL11.glPushMatrix();
			GL11.glTranslatef(Display.getWidth()/2f, 90, 0);
			Mav.personality.renderFace();
		GL11.glPopMatrix();
		int y = 180;
		String[] split = Mav.text.split("\n");
		for (String s : split) {
			lightFont.drawString((Display.getWidth()/2)-(lightFont.getWidth(s)/2), y, s, Color.white);
			y+=30;
		}
	}

}
