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

import java.io.File;

import com.gameminers.mav.Mav;
import com.gameminers.mav.render.RenderState;

public class TeachSphinxThread extends Thread {
	public TeachSphinxThread() {
		super("Sphinx teaching thread");
		setDaemon(true);
	}
	@Override
	public void run() {
		try {
			File training = new File(Mav.configDir, "training-data");
			training.mkdirs();
			while (Mav.silentFrames < 10) {
				sleep(100);
			}
			Mav.listening = true;
			RenderState.setText("\u00A7LRead this aloud:\nI am teaching Mav the sound of my voice.");
			Mav.audioManager.playClip("listen1");
			while (true) {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
