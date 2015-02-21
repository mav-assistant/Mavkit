package com.gameminers.mav.tts;

import marytts.exceptions.SynthesisException;

public interface TTSInterface {

	void say(String msg) throws SynthesisException;
	void sayAndWait(String msg) throws SynthesisException, InterruptedException;

}
