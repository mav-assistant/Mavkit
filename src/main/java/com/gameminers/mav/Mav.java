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
package com.gameminers.mav;

import java.io.File;

import javax.swing.UIManager;

import marytts.exceptions.SynthesisException;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.gameminers.mav.firstrun.FirstRunThread;
import com.gameminers.mav.personality.Personality;
import com.gameminers.mav.personality.poly.PolygonPersonality;
import com.gameminers.mav.personality.poly.TrianglePersonality;
import com.gameminers.mav.render.Fonts;
import com.gameminers.mav.render.PersonalityRenderer;
import com.gameminers.mav.render.RenderState;
import com.gameminers.mav.render.Rendering;
import com.gameminers.mav.screen.MainScreen;
import com.gameminers.mav.screen.Screen;
import com.gameminers.mav.tts.TTSInterface;
import com.gameminers.mav.tts.mary.MaryTTSInterface;

public class Mav {
	public static final File configDir = new File(System.getProperty("user.home"), ".mav");

	public static final int TARGET_FPS = 30;
	public static final float FADE_TIME = 30;

	private static int frameCounter = 0;
	private static int fps = 0;
	private static long lastFPSUpdate = System.currentTimeMillis();

	public static Screen currentScreen = new MainScreen();
	private static boolean run = true;
	private static boolean render = true;

	public static final PersonalityRenderer personalityRenderer = new PersonalityRenderer();
	
	public static Personality personality = new TrianglePersonality();

	public static long totalFrameCounter = 0;
	private static int fadeFrames = 0;
	private static int stopFrames = 0;
	
	public static VoiceThread voiceThread;
	public static TTSInterface ttsInterface;

	public static void stop() {
		RenderState.targetSat = 0;
		RenderState.targetLum = 0.3f;
		RenderState.text = "\u00A7LGoodbye.";
		RenderState.idle = false;
		if (personality instanceof PolygonPersonality) {
			((PolygonPersonality)personality).targetAngle = 0;
			((PolygonPersonality)personality).targetPulse = 0;
		}
		if (ttsInterface != null) {
			try {
				ttsInterface.say("Goodbye.");
			} catch (SynthesisException e) {
				e.printStackTrace();
			}
		}
		if (voiceThread != null) {
			voiceThread.interrupt();
		}
		run = false;
	}

	public static void main(String[] args) {
		configDir.mkdirs();
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception ex) {}
		}
		try {
			ttsInterface = new MaryTTSInterface();
		} catch (Throwable t) {
			Dialogs.showErrorDialog(null, "An error occurred while initializing MARY. Mav will now exit.", t);
			return;
		}
		try {
			Rendering.setUpDisplay();
			Fonts.loadFonts();
			Screen.initMouse();
		} catch (Throwable t) {
			Dialogs.showErrorDialog(null, "An error occurred while setting up the UI. Mav will now exit.", t);
			return;
		}
		try {
			Rendering.setUpGL();
			new FirstRunThread().start();
			/*voiceThread = new VoiceThread();
			voiceThread.start();*/
			while (render) {
				Rendering.beforeFrame();
				RenderState.update();
				doRender();
				
				totalFrameCounter++;
				frameCounter++;
				if (System.currentTimeMillis()-lastFPSUpdate >= 1000) {
					fps = frameCounter;
					frameCounter = 0;
					lastFPSUpdate = System.currentTimeMillis();
				}
				Display.update();
				Display.sync(TARGET_FPS);
				if (Display.isCloseRequested()) {
					stop();
				}
				if (!run) {
					stopFrames++;
					if (stopFrames >= 40) {
						fadeFrames++;
						if (fadeFrames >= FADE_TIME+10) {
							render = false;
						}
					}
				}
			}
		} catch (Throwable t) {
			Dialogs.showErrorDialog(null, "An uncaught exception was thrown in the render loop. Mav will now exit.", t);
		}
	}

	private static void doRender() {
		GL11.glPushMatrix();
		personalityRenderer.render();
		if (currentScreen != null) {
			GL11.glPushMatrix();
			currentScreen.render();
			GL11.glPopMatrix();
		}
		Screen.baseFont[0].drawString(8, 8, fps+" FPS");
		if (personality instanceof PolygonPersonality) {
			Screen.baseFont[0].drawString(8, 24, ((PolygonPersonality)personality).angle+"°");
		}
		if (totalFrameCounter < FADE_TIME) {
			fadeFrames = (int) (FADE_TIME-totalFrameCounter);
		}
		Rendering.drawRectangle(0, 0, Display.getWidth(), Display.getHeight(), 0, 0, 0, (fadeFrames/FADE_TIME), 1);
		GL11.glPopMatrix();
	}

}
