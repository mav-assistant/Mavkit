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

import marytts.exceptions.SynthesisException;

import org.apache.commons.lang.StringUtils;
import org.lwjgl.input.Keyboard;

import com.gameminers.mav.Mav;
import com.gameminers.mav.Strings;
import com.gameminers.mav.screen.InputScreen;

public class VerifyPronunciationScreen extends InputScreen {
	@Override
	public void doRender() {
	}
	@Override
	public void onKeyDown(int k, char c, long nanos) {
		if (k == Keyboard.KEY_RETURN) {
			String str = tf.getText();
			if (Strings.similarity(str.toLowerCase(), "yes") > 0.6) {
				try {
					Mav.ttsInterface.say("Okay, "+Mav.phoneticUserName+". Next, what is your favorite color?");
				} catch (SynthesisException e) {
					// TODO
					e.printStackTrace();
				}
				Mav.currentScreen = new ColorScreen();
			} else if (Strings.similarity(str.toLowerCase(), "no") > 0.6) {
				try {
					Mav.ttsInterface.say("Okay, type your name again phonetically.");
				} catch (SynthesisException e) {
					// TODO
					e.printStackTrace();
				}
				Mav.currentScreen = new EnterNameScreen(true);
			} else if (Strings.similarity(str.toLowerCase(), "repeat") > 0.6) {
				try {
					Mav.ttsInterface.say(Mav.phoneticUserName);
				} catch (SynthesisException e) {
					e.printStackTrace();
				}
			} else if (StringUtils.isBlank(str)) {
				try {
					Mav.ttsInterface.say("Please enter Yes, No, or Repeat.");
				} catch (SynthesisException e) {
					// TODO
					e.printStackTrace();
				}
			} else {
				try {
					Mav.ttsInterface.say("Sorry, I don't understand.");
				} catch (SynthesisException e) {
					// TODO
					e.printStackTrace();
				}
			}
			tf.setText("");
		}
	}
	@Override
	public void onKeyUp(int k, char c, long nanos) {
		
	}
	@Override
	public void onMouseMove(int x, int y, long nanos) {
		
	}
	@Override
	public void onMouseDown(int x, int y, int button, long nanos) {
		
	}
	@Override
	public void onMouseUp(int x, int y, int button, long nanos) {
		
	}
	@Override
	public void onMouseWheel(int x, int y, int dWheel, long nanos) {
		
	}


}
