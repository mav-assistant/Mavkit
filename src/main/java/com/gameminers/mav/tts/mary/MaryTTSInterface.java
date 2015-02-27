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

import java.util.HashMap;
import java.util.Map;

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
	private Map<Character, String> letterNames = new HashMap<>();
	public MaryTTSInterface() throws MaryConfigurationException {
		mary = new LocalMaryInterface();
		letterNames.put('A', "Ay");
		letterNames.put('B', "Bee");
		letterNames.put('C', "Sea");
		letterNames.put('D', "Dee");
		letterNames.put('E', "Ee");
		letterNames.put('F', "Eff");
		letterNames.put('G', "Gee");
		letterNames.put('H', "Aitch");
		letterNames.put('I', "Eye");
		letterNames.put('J', "Jay");
		letterNames.put('K', "Kay");
		letterNames.put('L', "El");
		letterNames.put('M', "Em");
		letterNames.put('N', "En");
		letterNames.put('O', "Oh");
		letterNames.put('P', "Pee");
		letterNames.put('Q', "Cue");
		letterNames.put('R', "Ar");
		letterNames.put('S', "Ess");
		letterNames.put('T', "Tee");
		letterNames.put('U', "You");
		letterNames.put('V', "Vee");
		letterNames.put('W', "Duble U");
		letterNames.put('X', "Ecks");
		letterNames.put('Y', "Why");
		letterNames.put('Z', "Zee");
	}
	
	@Override
	public void say(String msg) throws SynthesisException {
		mary.setInputType("TEXT");
		// MARY TTS is a bit glitchy at the best of times, so we have these special handicaps for common words that it doesn't say correctly.
		AudioInputStream audio = mary.generateAudio(abbrev(msg)
				.replace("don't", "dont")
				.replace("Don't", "Dont")
				.replace("Does", "duhz")
				.replace("does", "duhz")
				.replace("Color", "Ka-ler")
				.replace("color", "ka-ler")
				.replace("Can't", "Cant")
				.replace("can't", "cant")
				.replace("Know", "No") // 'know' is pronounced correctly, but causes a strange pause
				.replace("know", "no")
				.replace("Chartreuse", "Sh'art-trooce")
				.replace("chartreuse", "sh'art-trooce"));
		Mav.audioManager.play(audio);
	}

	private String abbrev(String msg) {
		String[] split = msg.split(" ");
		StringBuilder b = new StringBuilder();
		for (String s : split) {
			boolean allCaps = true;
			for (char c : s.toCharArray()) {
				if (Character.isLetter(c) && !Character.isUpperCase(c)) {
					allCaps = false;
					break;
				}
			}
			if (allCaps) {
				for (char c : s.toCharArray()) {
					if (Character.isLetter(c)) {
						b.append(letterNames.get(c));
						b.append(" ");
					} else {
						b.append(c);
					}
				}
			} else {
				b.append(s);
			}
			b.append(" ");
		}
		return b.toString();
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
