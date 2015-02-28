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

import java.util.HashMap;
import java.util.Map;

import marytts.exceptions.SynthesisException;

import org.apache.commons.lang.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.gameminers.mav.Mav;
import com.gameminers.mav.Strings;
import com.gameminers.mav.render.Fonts;
import com.gameminers.mav.render.RenderState;
import com.gameminers.mav.screen.InputScreen;

public class ColorScreen extends InputScreen {
	private static final Map<String, Float> colors = new HashMap<>();
	static {
		colors.put("red", 0f);
		colors.put("orange", 30f);
		colors.put("yellow", 60f);
		colors.put("chartreuse", 90f);
		colors.put("lime", 90f);
		colors.put("green", 120f);
		colors.put("teal", 150f);
		colors.put("cyan", 180f);
		colors.put("aqua", 180f);
		colors.put("turquoise", 180f);
		colors.put("capri", 210f);
		colors.put("sky", 210f);
		colors.put("blue", 240f);
		colors.put("purple", 270f);
		colors.put("violet", 270f);
		colors.put("magenta", 300f);
		colors.put("hot", 300f);
		colors.put("pink", 300f);
	}
	public ColorScreen() {
		RenderState.setText("\u00A7LOkay, "+Mav.userName+"\nWhat is your favorite\ncolor?\n\n\u00A7sType a color below to preview, press Enter\n\u00A7sto continue.\n\u00A7sThis will be the color of my interface.\n\u00A7s(e.g. teal, green)");
	}
	
	@Override
	public void doRender() {
		String matched = matchColor();
		if (matched == null) {
			RenderState.targetHue = 150f;
			String str = "teal (unknown color)";
			Fonts.base[0].drawString((Display.getWidth()-Fonts.base[0].getWidth(str))-8, Display.getHeight()-18, str);
		} else {
			String str = prettifyColorName(matched);
			Fonts.base[0].drawString((Display.getWidth()-Fonts.base[0].getWidth(str))-8, Display.getHeight()-18, str);
		}
	}
	
	private String prettifyColorName(String matched) {
		return matched.equals("hot") ? "hot pink" : matched.equals("sky") ? "sky blue" : matched;
	}

	private String matchColor() {
		if (StringUtils.isBlank(tf.getText())) return "teal";
		for (String s : colors.keySet()) {
			if (Strings.similarity(tf.getText(), s) > 0.6) {
				RenderState.targetHue = colors.get(s);
				return s;
			}
		}
		return null;
	}

	@Override
	public void onKeyDown(int k, char c, long nanos) {
		if (k == Keyboard.KEY_RETURN) {
			String str = tf.getText();
			if (StringUtils.isBlank(str)) {
				try {
					Mav.ttsInterface.say("Please enter a color.");
				} catch (SynthesisException e) {
					// TODO
					e.printStackTrace();
				}
				return;
			}
			String matched = matchColor();
			if (matched == null) {
				try {
					Mav.ttsInterface.say("Sorry, I don't know that color.");
				} catch (SynthesisException e) {
					// TODO
					e.printStackTrace();
				}
			} else {
				try {
					String colorSaying = "Okay, from now on I'll use "+prettifyColorName(matched);
					if (matched.equals("teal")) {
						colorSaying = "Okay, I'll continue to use teal.";
					}
					Mav.ttsInterface.say(colorSaying+". Next, do you want to use Google services? This can help me better understand you, but it sends everything you say to Google.");
				} catch (SynthesisException e) {
					// TODO
					e.printStackTrace();
				}
				Mav.currentScreen = new GoogleScreen();
			}
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
