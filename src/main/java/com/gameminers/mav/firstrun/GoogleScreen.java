package com.gameminers.mav.firstrun;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang.StringUtils;
import org.lwjgl.input.Keyboard;

import marytts.exceptions.SynthesisException;

import com.gameminers.mav.Mav;
import com.gameminers.mav.Strings;
import com.gameminers.mav.render.RenderState;
import com.gameminers.mav.screen.InputScreen;

public class GoogleScreen extends InputScreen {

	public GoogleScreen() {
		RenderState.text = "\u00A7LDo you want to use\n\u00A7LGoogle services?\nThis can help me better\nunderstand you, but it\nsends everything you say\nto Google.\n\u00A7sEnter 'Yes' or 'No'.";
	}
	
	@Override
	public void onKeyDown(int k, char c, long nanos) {
		if (k == Keyboard.KEY_RETURN) {
			String str = tf.getText();
			if (Strings.similarity(str.toLowerCase(), "yes") > 0.6) {
				if (Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().browse(new URI("https://mav.gameminers.com/using-google.html"));
						try {
							Mav.ttsInterface.say("Okay. I'll need you to get a Google API key. I've opened a page on my website explaining how to do this.");
						} catch (SynthesisException e) {
							// TODO
							e.printStackTrace();
						}
						return;
					} catch (IOException e) {
						e.printStackTrace();
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
				try {
					Mav.ttsInterface.say("Okay. I'll need you to get a Google API key. I can't open your browser, so you'll need to go to my site yourself. Near the bottom is a link titled 'Using Google'. That page explains how to get an API key.");
				} catch (SynthesisException e) {
					// TODO
					e.printStackTrace();
				}
			} else if (Strings.similarity(str.toLowerCase(), "no") > 0.6) {
				try {
					Mav.ttsInterface.say("Okay. Next, I need to learn the sound of your voice. Read the sentences I show out loud.");
				} catch (SynthesisException e) {
					// TODO
					e.printStackTrace();
				}
				Mav.currentScreen = new TeachSphinxScreen();
			} else if (StringUtils.isBlank(str)) {
				try {
					Mav.ttsInterface.say("Please enter Yes or No.");
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

	@Override
	public void doRender() {
		
	}

}
