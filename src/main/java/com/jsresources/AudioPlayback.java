/*
 * Copyright (c) 2001,2004 by Florian Bomers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jsresources;

import static com.jsresources.Constants.DEBUG;
import static com.jsresources.Constants.VERBOSE;
import static com.jsresources.Constants.out;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

// Class that reads its audio from an AudioInputStream
public class AudioPlayback extends AudioBase {

	protected AudioInputStream ais;
	private PlayThread thread;

	public AudioPlayback(int formatCode, Mixer mixer, int bufferSizeMillis) {
		super("Speaker", formatCode, mixer, bufferSizeMillis);
	}
	
	@Override
	protected void createLineImpl() throws Exception {
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, lineFormat);

		// get the playback data line for capture.
		if (mixer != null) {
			line = (SourceDataLine) mixer.getLine(info);
		} else {
			line = AudioSystem.getSourceDataLine(lineFormat);
		}
	}

	@Override
	protected void openLineImpl() throws Exception {
		SourceDataLine sdl = (SourceDataLine) line;
		sdl.open(lineFormat, bufferSize);
	}

	@Override
	public synchronized void start() throws Exception {
		boolean needStartThread = false;
		if (thread != null && thread.isTerminating()) {
			thread.terminate();
			needStartThread = true;
		}
		if (thread == null || needStartThread) {
			// start thread
			thread = new PlayThread();
			thread.start();
		}
		super.start();
	}

	@Override
	protected void closeLine(boolean willReopen) {
		PlayThread oldThread = null;
		synchronized (this) {
			if (!willReopen && thread != null) {
				thread.terminate();
			}
			super.closeLine(willReopen);
			if (!willReopen && thread != null) {
				oldThread = thread;
				thread = null;
			}
		}
		if (oldThread != null) {
			if (VERBOSE) {
				out("AudioPlayback.closeLine(): closing thread, waiting for it to die");
			}
			oldThread.waitFor();
			if (VERBOSE) {
				out("AudioPlayback.closeLine(): thread closed");
			}
		}
	}
	
	// in network format
	public void setAudioInputStream(AudioInputStream ais) {
		this.ais = AudioSystem.getAudioInputStream(lineFormat, ais);
	}

	class PlayThread extends Thread {
		private boolean doTerminate = false;
		private boolean terminated = false;

		@Override
		public void run() {
			if (VERBOSE) {
				out("Start AudioPlayback pull thread");
			}
			byte[] buffer = new byte[getBufferSize()];
			try {
				while (!doTerminate) {
					SourceDataLine sdl = (SourceDataLine) line;
					if (ais != null) {
						int r = ais.read(buffer, 0, buffer.length);
						if (r > 0) {
							if (isMuted()) {
								muteBuffer(buffer, 0, r);
							}
							// run some simple analysis
							calcCurrVol(buffer, 0, r);
							if (sdl != null) {
								sdl.write(buffer, 0, r);
							}
						} else {
							if (r == 0) {
								synchronized (this) {
									this.wait(40);
								}
							}
						}
					} else {
						synchronized (this) {
							this.wait(50);
						}
					}
				}
			} catch (IOException ioe) {
				// if (DEBUG) ioe.printStackTrace();
			} catch (InterruptedException ie) {
				if (DEBUG) {
					ie.printStackTrace();
				}
			}
			if (VERBOSE) {
				out("Stop AudioPlayback pull thread");
			}
			terminated = true;
		}

		public synchronized void terminate() {
			doTerminate = true;
			this.notifyAll();
		}

		public synchronized boolean isTerminating() {
			return doTerminate || terminated;
		}

		public synchronized void waitFor() {
			if (!terminated) {
				try {
					this.join();
				} catch (InterruptedException ie) {
					if (DEBUG) {
						ie.printStackTrace();
					}
				}
			}
		}
	}

	public AudioInputStream getAudioInputStream() {
		return ais;
	}

}
