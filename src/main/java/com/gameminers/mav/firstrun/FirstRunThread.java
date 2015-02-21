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
package com.gameminers.mav.firstrun;

import com.gameminers.mav.Dialogs;
import com.gameminers.mav.Mav;
import com.gameminers.mav.render.RenderState;

public class FirstRunThread extends Thread {
	@Override
	public void run() {
		try {
			RenderState.targetHue = 150;
			RenderState.text = "\u00A7LHi! I'm Mav.\nI don't know who you are,\nso let's fix that.\n\nFirst off, what's your name?\n\u00A7sClick inside the box to start typing.";
			Mav.currentScreen = new FirstRunScreen();
		} catch (Throwable t) {
			Dialogs.showErrorDialog(null, "An error occured during first run setup.", t);
		}
	}
}
