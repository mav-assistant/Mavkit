/*
 * This file is part of Mav.
 *
 * Mav is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Mav is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mav. If not, see <http://www.gnu.org/licenses/>.
 */

package com.unascribed.mav;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class AudioProcessor {
	private static final Logger log = LoggerFactory.getLogger("AudioProcessor");
	
	private AudioFormat audioFormat;
	private SourceDataLine dataLine;
	
	private Mav mav;
	
	public AudioProcessor(Mav mav) {
		this.mav = mav;
	}
	
	private void initialize() {
		try {
			AudioFormat af = new AudioFormat(Encoding.PCM_SIGNED, 44100, 16, 1, 2, 44100, true);
			SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
			sdl.open(af);
			audioFormat = af;
			dataLine = sdl;
		} catch (Exception e) {
			throw new Panic("panic.failedAudioSystem", e);
		}
	}
	
	public void run() {
		initialize();
	}
}
