package com.gameminers.mav.tts.mary;

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
		player = new AudioPlayer(mary.generateAudio(msg));
		player.start();
	}
	
	@Override
	public void sayAndWait(String msg) throws SynthesisException, InterruptedException {
		say(msg);
		player.join();
	}

}
