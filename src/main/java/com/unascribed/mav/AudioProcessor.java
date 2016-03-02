package com.unascribed.mav;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class AudioProcessor {
	
	private static final Logger log = LoggerFactory.getLogger("AudioProcessor");
	
	private AudioFormat audioFormat;
	private SourceDataLine dataLine;
	
	private void initialize() {
		try {
			AudioFormat af = new AudioFormat(Encoding.PCM_SIGNED, 44100, 16, 1, 2, 44100, true);
			SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
			sdl.open(af);
			audioFormat = af;
			dataLine = sdl;
		} catch (Exception e) {
			log.error("Failed to initialize audio system", e);
			Throwables.propagate(e);
		}
	}
	
	public void run() {
		initialize();
	}
}
