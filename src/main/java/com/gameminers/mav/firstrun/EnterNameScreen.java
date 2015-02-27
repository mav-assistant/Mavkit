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
import com.gameminers.mav.render.RenderState;
import com.gameminers.mav.screen.InputScreen;

public class EnterNameScreen extends InputScreen {
	private final boolean phonetic;
	public EnterNameScreen(boolean phonetic) {
		this.phonetic = phonetic;
		if (phonetic) {
			RenderState.setText("\u00A7LOkay, "+Mav.userName+"\nType your name again\nphonetically.\n\n\u00A7s(e.g. Ay-sen)\n\u00A7sPress Enter when finished.");
		}
	}
	@Override
	public void doRender() {
		
	}
	@Override
	public void onKeyDown(int k, char c, long nanos) {
		if (k == Keyboard.KEY_RETURN) {
			String str = tf.getText();
			if (StringUtils.isBlank(str)) {
				try {
					Mav.ttsInterface.say("Please enter your name.");
				} catch (SynthesisException e) {
					// TODO
					e.printStackTrace();
				}
				return;
			}
			Mav.phoneticUserName = str;
			if (!phonetic) {
				Mav.userName = str;
			}
			Mav.currentScreen = new VerifyPronunciationScreen();
			RenderState.setText("\u00A7LOkay, "+Mav.userName+"\nDoes this sound correct?\n\n\u00A7sEnter 'Yes' or 'No' to continue\n\u00A7sEnter 'Repeat' to hear me say it again");
			try {
				Mav.ttsInterface.say("Okay, "+Mav.phoneticUserName+". Does that sound correct?");
			} catch (SynthesisException e) {
				try {
					Mav.ttsInterface.say("I'm sorry, I can't pronounce that. Please type your name again phonetically.");
				} catch (SynthesisException e1) {
					e1.printStackTrace();
					// TODO
				}
				Mav.currentScreen = new EnterNameScreen(true);
			}
		}
	}
	@Override
	public void onKeyUp(int k, char c, long nanos) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMouseMove(int x, int y, long nanos) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMouseDown(int x, int y, int button, long nanos) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMouseUp(int x, int y, int button, long nanos) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMouseWheel(int x, int y, int dWheel, long nanos) {
		// TODO Auto-generated method stub
		
	}

}
