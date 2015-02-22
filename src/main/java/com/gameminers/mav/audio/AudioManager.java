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

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioFormat.Encoding;

public class AudioManager {
	public class AudioThread extends Thread {
		public AudioThread() {
			super("Audio thread");
			setDaemon(true);
		}
		@Override
		public void run() {
			sink.start();
			while (!interrupted()) {
				if (sounds.isEmpty()) {
					try {
						sleep(100L);
					} catch (InterruptedException e) {
						break;
					}
				} else {
					AudioInputStream stream = sounds.pop();
					int read = 0;
					byte[] data = new byte[65532];
					while (read != -1) {
						try {
							read = stream.read(data, 0, data.length);
						} catch (IOException e) {
							e.printStackTrace();
						}
						if (read >= 0) {
							sink.write(data, 0, read);
						}
					}
					sink.drain();
				}
			}
		}
	}
	private AudioFormat targetFormat;
	private SourceDataLine sink;
	private TargetDataLine source;
	private Deque<AudioInputStream> sounds = new ArrayDeque<>();
	private AudioThread thread;
	public void init() {
		targetFormat = new AudioFormat(Encoding.PCM_SIGNED, 48000, 16, 2, 2, 48000, false);
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, targetFormat);
		boolean supported = AudioSystem.isLineSupported(info);
		if (!supported) {
			targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, targetFormat.getSampleRate(),
					targetFormat.getSampleSizeInBits(), targetFormat.getChannels(), targetFormat.getChannels()
							* (targetFormat.getSampleSizeInBits() / 8), targetFormat.getSampleRate(),
					targetFormat.isBigEndian());
		}
		try {
			sink = AudioSystem.getSourceDataLine(targetFormat);
			source = AudioSystem.getTargetDataLine(targetFormat);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		if (sink != null) {
			if (sink.getClass().getName().equals("org.classpath.icedtea.pulseaudio.PulseAudioSourceDataLine")) {
				PulseAudioHelper.setLineName(sink, "Mav");
			}
			try {
				sink.open();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		}
		if (source != null) {
			if (source.getClass().getName().equals("org.classpath.icedtea.pulseaudio.PulseAudioTargetDataLine")) {
				PulseAudioHelper.setLineName(source, "Mav");
			}
			try {
				source.open();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		}
		thread = new AudioThread();
		thread.start();
	}
	public void play(AudioInputStream stream) {
		sounds.addLast(AudioSystem.getAudioInputStream(targetFormat, stream));
	}
	public SourceDataLine getSink() {
		return sink;
	}
	public TargetDataLine getSource() {
		return source;
	}
	public void destroy() {
		thread.interrupt();
		if (sink != null) {
			sink.close();
			sink = null;
		}
		if (source != null) {
			source.close();
			source = null;
		}
	}
}
