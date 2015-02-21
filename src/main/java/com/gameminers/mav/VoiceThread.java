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

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

public class VoiceThread extends Thread {
	public VoiceThread() {
		super("Voice thread");
		setDaemon(true);
	}
	@Override
	public void run() {
		try {
			Mav.text = "\u00A7LJust a moment...";
			Mav.idle = false;
			Mav.targetHue = 60;
			if (Mav.personality instanceof TrianglePersonality) {
				((TrianglePersonality)Mav.personality).targetAngle = 135f;
			}
			Configuration config = new Configuration();
			config.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
			config.setDictionaryPath("resource:/resources/dict/hey.dict");
			config.setGrammarPath("resource:/resources/dict/");
			config.setGrammarName("hey");
			config.setUseGrammar(true);
			config.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.dmp");
			LiveSpeechRecognizer live = new LiveSpeechRecognizer(config);
			Mav.text = "\u00A7LWaiting...\n\u00A7s(Say 'Hey, Mav')";
			Mav.targetHue = 120;
			Mav.idle = true;
			live.startRecognition(false);
			while (!interrupted()) {
				SpeechResult result = live.getResult();
				if (result != null) {
					if (result.getHypothesis().equals("hey mav")) {
						Mav.text = "\u00A7LYes?";
						Mav.idle = false;
						Mav.targetHue = 240;
						if (Mav.personality instanceof TrianglePersonality) {
							((TrianglePersonality)Mav.personality).targetAngle = 270f;
						}
					}
				}
			}
		} catch (Throwable t) {
			Mav.showErrorDialog(null, "An error occured during voice recognition.", t);
		}
	}
}
