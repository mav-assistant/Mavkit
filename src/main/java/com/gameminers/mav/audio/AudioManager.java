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
