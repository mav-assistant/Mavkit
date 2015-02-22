package com.gameminers.mav.audio;

import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import org.classpath.icedtea.pulseaudio.PulseAudioSourceDataLine;
import org.classpath.icedtea.pulseaudio.PulseAudioTargetDataLine;

public class PulseAudioHelper {

	public static void setLineName(SourceDataLine line, String name) {
		((PulseAudioSourceDataLine)line).setName(name);
	}
	
	public static void setLineName(TargetDataLine line, String name) {
		((PulseAudioTargetDataLine)line).setName(name);
	}

}
