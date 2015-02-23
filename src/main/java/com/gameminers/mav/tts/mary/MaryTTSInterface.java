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
package com.gameminers.mav.tts.mary;

import javax.sound.sampled.AudioInputStream;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.util.dom.DomUtils;

import com.gameminers.mav.Mav;
import com.gameminers.mav.tts.TTSInterface;

public class MaryTTSInterface implements TTSInterface {
	private MaryInterface mary;
	public MaryTTSInterface() throws MaryConfigurationException {
		mary = new LocalMaryInterface();
	}
	
	@Override
	public void say(String msg) throws SynthesisException {
		mary.setInputType("TEXT");
		AudioInputStream audio = mary.generateAudio(msg);
		Mav.audioManager.play(audio);
	}

	@Override
	public void sayWithEmotion(String msg, String plaintextFallback)
			throws SynthesisException {
		try {
			mary.setInputType("EMOTIONML");
			AudioInputStream audio = mary.generateAudio(DomUtils.parseDocument(msg));
			Mav.audioManager.play(audio);
		} catch (Exception e) {
			say(plaintextFallback);
		}
	}

}
