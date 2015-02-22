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
import marytts.util.data.audio.AudioPlayer;

import com.gameminers.mav.tts.TTSInterface;

public class MaryTTSInterface implements TTSInterface {
	private MaryInterface mary;
	private AudioPlayer player;
	public MaryTTSInterface() throws MaryConfigurationException {
		mary = new LocalMaryInterface();
	}
	
	@Override
	public void say(String msg) throws SynthesisException {
		AudioInputStream audio = mary.generateAudio(msg);
		player = new AudioPlayer(audio);
		player.start();
	}
	
	@Override
	public void sayAndWait(String msg) throws SynthesisException, InterruptedException {
		say(msg);
		player.join();
	}

}
