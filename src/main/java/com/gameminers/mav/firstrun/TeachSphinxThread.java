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

import com.gameminers.mav.Mav;
import com.gameminers.mav.render.RenderState;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;

public class TeachSphinxThread extends Thread {
	public TeachSphinxThread() {
		super("Sphinx teaching thread");
		setDaemon(true);
	}
	@Override
	public void run() {
		try {
			Configuration config = new Configuration();
			config.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.dmp");
			config.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
			config.setSampleRate(16000);
			config.setDictionaryPath("resource:/resources/sphinx/train/arcticAll.dict");
			while (Mav.silentFrames < 10) {
				sleep(100);
			}
			StreamSpeechRecognizer recog = new StreamSpeechRecognizer(config);
			recog.startRecognition(Mav.audioManager.getSource().getAudioInputStream());
			Mav.listening = true;
			RenderState.setText("\u00A7LRead this aloud:\nI am teaching Mav the sound of my voice.");
			Mav.audioManager.playClip("notif5");
			while (true) {
				SpeechResult result = recog.getResult();
				if (result != null) {
					System.out.println(result.getHypothesis());
					RenderState.setText("\u00A7LRead this aloud:\nI am teaching Mav the sound of my voice.\n\n\u00A7s"+result.getHypothesis());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
