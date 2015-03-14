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
package com.gameminers.mav.firstrun;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.Display;

import com.gameminers.mav.Mav;
import com.gameminers.mav.render.Fonts;
import com.gameminers.mav.render.RenderState;

public class TeachSphinxThread extends Thread {
	public TeachSphinxThread() {
		super("Sphinx teaching thread");
		setDaemon(true);
	}
	@Override
	public void run() {
		try {
			File training = new File(Mav.configDir, "training-data");
			training.mkdirs();
			while (Mav.silentFrames < 30) {
				sleep(100);
			}
			Mav.listening = true;
			InputStream prompts = ClassLoader.getSystemResourceAsStream("resources/sphinx/train/arcticAll.prompts");
			List<String> arctic = IOUtils.readLines(prompts);
			IOUtils.closeQuietly(prompts);
			Mav.audioManager.playClip("listen1");
			byte[] buf = new byte[2048];
			int start = 0;
			int end = 21;
			AudioInputStream in = Mav.audioManager.getSource().getAudioInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (true) {
				for (int i = start; i < end; i++) {
					baos.reset();
					String prompt = arctic.get(i);
					RenderState.setText("\u00A7LRead this aloud:\n"+Fonts.wrapStringToFit(prompt.substring(prompt.indexOf(':')+1), Fonts.base[1], Display.getWidth()));
					File file = new File(training, prompt.substring(0, prompt.indexOf(':'))+".wav");
					file.createNewFile();
					int read = 0;
					while (Mav.silentListenFrames > 0) {
						read = Mav.audioManager.getSource().getAudioInputStream().read(buf);
					}
					baos.write(buf, 0, read);
					while (Mav.silentListenFrames < 60) {
						in.read(buf);
						if (read == -1) {
							RenderState.setText("\u00A7LAn error occurred\nUnexpected end of stream\nPlease restart Mav");
							RenderState.targetHue = 0;
							return;
						}
						baos.write(buf, 0, read);
					}
					AudioSystem.write(new AudioInputStream(new ByteArrayInputStream(baos.toByteArray()), in.getFormat(), baos.size()/2), AudioFileFormat.Type.WAVE, file);
					Mav.audioManager.playClip("notif2");
				}
				Mav.ttsInterface.say(Mav.phoneticUserName+", that should be enough for now. Do you want to keep training anyway?");
				RenderState.setText("\u00A7LOkay, "+Mav.userName+"\nI think that should be\nenough. Do you want to\nkeep training anyway?\n\u00A7s(Say 'Yes' or 'No' out loud)");
				break;
				//start = end+1;
				//end += 20;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
