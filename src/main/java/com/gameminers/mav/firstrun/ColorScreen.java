package com.gameminers.mav.firstrun;

import java.util.HashMap;
import java.util.Map;

import marytts.exceptions.SynthesisException;

import org.apache.commons.lang.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.gameminers.mav.Mav;
import com.gameminers.mav.Strings;
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
		RenderState.text = "\u00A7LOkay, "+Mav.userName+"\nWhat is your favorite\ncolor?\n\n\u00A7sType a color below to preview, press Enter\n\u00A7sto continue.\n\u00A7sThis will be the color of my interface.\n\u00A7s(e.g. teal, green)";
	}
	
	@Override
	public void doRender() {
		String matched = matchColor();
		if (matched == null) {
			RenderState.targetHue = 150f;
			String str = "teal (unknown color)";
			baseFont[0].drawString((Display.getWidth()-baseFont[0].getWidth(str))-8, Display.getHeight()-18, str);
		} else {
			String str = prettifyColorName(matched);
			baseFont[0].drawString((Display.getWidth()-baseFont[0].getWidth(str))-8, Display.getHeight()-18, str);
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
					Mav.ttsInterface.say("Do you want to use Google services? This can help me better understand you, but it sends everything you say to Google.");
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
