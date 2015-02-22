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
package com.gameminers.mav.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.AudioFormat.Encoding;

public class AudioManager {
	private AudioFormat targetFormat;
	public void init() {
		AudioFormat format = new AudioFormat(Encoding.PCM_SIGNED, 48000, 16, 2, 2, 48000, false);
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		boolean supported = AudioSystem.isLineSupported(info);
		if (!supported) {
			targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(),
					format.getSampleSizeInBits(), format.getChannels(), format.getChannels()
							* (format.getSampleSizeInBits() / 8), format.getSampleRate(),
					format.isBigEndian());
		}
	}
}
