/*
 *	AudioUtils.java
 */

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

//TODO: enable dynamic change of audio format - especially for owner client...

package com.jsresources;

import static com.jsresources.Constants.FORMAT_CODE_CD;
import static com.jsresources.Constants.FORMAT_CODE_FM;
import static com.jsresources.Constants.FORMAT_CODE_GSM;
import static com.jsresources.Constants.FORMAT_CODE_TELEPHONE;

import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioUtils {
	private static final float[] netSampleRate = { 1.0f, // nothing
			44100.0f, // CD
			22050.0f, // FM
			8000.0f, // Telephone
			8000.0f // GSM
	};

	public static long bytes2millis(long bytes, AudioFormat format) {
		return (long) (bytes / format.getFrameRate() * 1000 / format
				.getFrameSize());
	}

	public static long millis2bytes(long ms, AudioFormat format) {
		return (long) (ms * format.getFrameRate() / 1000 * format
				.getFrameSize());
	}

	public static AudioFormat getLineAudioFormat(int formatCode) {
		return getLineAudioFormat(netSampleRate[formatCode]);
	}

	public static AudioFormat getLineAudioFormat(float sampleRate) {
		return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, // sampleRate
				16, // sampleSizeInBits
				1, // channels
				2, // frameSize
				sampleRate, // frameRate
				false); // bigEndian
	}

	public static AudioFormat getNetAudioFormat(int formatCode)
			throws UnsupportedAudioFileException {
		if (formatCode == FORMAT_CODE_CD)
			return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0f, // sampleRate
					16, // sampleSizeInBits
					1, // channels
					2, // frameSize
					44100.0f, // frameRate
					true); // bigEndian
		else if (formatCode == FORMAT_CODE_FM)
			return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 22050.0f, // sampleRate
					16, // sampleSizeInBits
					1, // channels
					2, // frameSize
					22050.0f, // frameRate
					true); // bigEndian
		else if (formatCode == FORMAT_CODE_TELEPHONE)
			return new AudioFormat(AudioFormat.Encoding.ULAW, 8000.0f, // sampleRate
					8, // sampleSizeInBits
					1, // channels
					1, // frameSize
					8000.0f, // frameRate
					false); // bigEndian
		else if (formatCode == FORMAT_CODE_GSM)
			/*
			 * try { Class.forName("org.tritonus.share.sampled.Encodings"); }
			 * catch (ClassNotFoundException cnfe) { throw new
			 * RuntimeException("Tritonus shared classes not properly installed!"
			 * ); } return new AudioFormat(
			 * org.tritonus.share.sampled.Encodings.getEncoding("GSM0610"),
			 * 8000.0F, // sampleRate -1, // sampleSizeInBits 1, // channels 33,
			 * // frameSize 50.0F, // frameRate false); // bigEndian
			 */
			return new AudioFormat(new AudioFormat.Encoding("GSM0610"),
					8000.0F, // sampleRate
					-1, // sampleSizeInBits
					1, // channels
					33, // frameSize
					50.0F, // frameRate
					false); // bigEndian
		throw new RuntimeException("Wrong format code!");
	}

	public static AudioInputStream createNetAudioInputStream(int formatCode,
			InputStream stream) {
		try {
			AudioFormat format = getNetAudioFormat(formatCode);
			return new AudioInputStream(stream, format,
					AudioSystem.NOT_SPECIFIED);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int getFormatCode(AudioFormat format) {
		AudioFormat.Encoding encoding = format.getEncoding();
		// very simple check...
		if (encoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
			if (format.getSampleRate() == 44100.0f)
				return FORMAT_CODE_CD;
			else
				return FORMAT_CODE_FM;
		}
		if (encoding.equals(AudioFormat.Encoding.ULAW))
			return FORMAT_CODE_TELEPHONE;
		if (encoding.toString().equals("GSM0610"))
			return FORMAT_CODE_GSM;
		throw new RuntimeException("Wrong Format");
	}

}
