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

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import com.gameminers.mav.audio.AudioManager;
import com.gameminers.mav.firstrun.EnterNameScreen;
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
	public static final AudioManager audioManager = new AudioManager();
	
	public static Personality personality = new TrianglePersonality();

	public static long totalFrameCounter = 0;
	private static int fadeFrames = 0;
	private static int stopFrames = 0;
	
	public static String userName;
	public static String phoneticUserName;
	
	public static VoiceThread voiceThread;
	public static TTSInterface ttsInterface;

	public static void stop() {
		if (!run) return;
		RenderState.targetSat = 0;
		RenderState.targetDim = 0.3f;
		RenderState.text = "\u00A7LGoodbye.";
		RenderState.idle = false;
		currentScreen = null;
		if (personality instanceof PolygonPersonality) {
			((PolygonPersonality)personality).calm();
			((PolygonPersonality)personality).targetPulse = 0.075f;
		}
		if (ttsInterface != null) {
			try {
				ttsInterface.sayWithEmotion("<emotionml version='1.0' xmlns='http://www.w3.org/2009/10/emotionml' category-set='http://www.w3.org/TR/emotion-voc/xml#everyday-categories'><emotion><category name='sad'/>Goodbye.</emotion></emotionml>", "Goodbye.");
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
		Thread.currentThread().setName("Init thread");
		configDir.mkdirs();
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception ex) {}
		}
		try {
			Rendering.setUpDisplay();
		} catch (Throwable t) {
			Dialogs.showErrorDialog(null, "An error occurred while initializing LWJGL. Mav will now exit.", t);
		}
		try {
			Fonts.loadFonts();
			for (int i = 0; i < FADE_TIME+10; i++) {
				drawBasicScreen("Getting ready", 1.0f-(i/FADE_TIME));
				Display.sync(TARGET_FPS);
			}
			drawBasicScreen("Initializing MARY", 0.0f);
			ttsInterface = new MaryTTSInterface();
			drawBasicScreen("Setting up audio", 0.0f);
			audioManager.init();
			drawBasicScreen("Setting up input", 0.0f);
			Screen.initMouse();
			Keyboard.enableRepeatEvents(true);
		} catch (Throwable t) {
			Dialogs.showErrorDialog(null, "An error occurred while setting up the UI. Mav will now exit.", t);
			return;
		}
		try {
			Thread.currentThread().setName("Render thread");
			Rendering.setUpGL();
			RenderState.text = "\u00A7LHi! I'm Mav.\nI don't know who you are,\nso let's fix that.\n\nFirst off, what's your name?\n\u00A7sClick inside the box to start typing.\n\u00A7sPress Enter when you're finished.";
			currentScreen = new EnterNameScreen(false);
			ttsInterface.sayWithEmotion("<emotionml version='1.0' xmlns='http://www.w3.org/2009/10/emotionml' category-set='http://www.w3.org/TR/emotion-voc/xml#everyday-categories'><emotion><category name='happy'/>Hi! I'm Mav.</emotion>I dont know who you are, so let's fix that.<emotion><category name='interested'/>First off, what is your name?</emotion></emotionml>", "Hi! I'm Mav. I dont know who you are, so let's fix that. First off, what is your name?");
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
							audioManager.destroy();
							render = false;
						}
					}
				}
			}
		} catch (Throwable t) {
			Dialogs.showErrorDialog(null, "An uncaught exception was thrown in the render loop. Mav will now exit.", t);
		}
	}

	private static void drawBasicScreen(String s, float dim) {
		Rendering.setUpGL();
		Rendering.beforeFrame();
		GL11.glClearColor(0, 0, 0, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		Screen.lightFont[2].drawString((Display.getWidth()/2)-(Screen.lightFont[2].getWidth(s)/2), 160, s, Color.white);
		Rendering.drawTriangle(Display.getWidth()/2f, 90, 74, 0.5f, 0.5f, 0.5f, 0.5f, 0);
		Rendering.drawTriangle(Display.getWidth()/2f, 90, 64, 0.5f, 0.5f, 0.5f, 1, 0.5f);
		Rendering.drawTriangle(Display.getWidth()/2f, 90, 56, 0, 0, 0, 1, 1f);
		Rendering.drawRectangle(0, 0, Display.getWidth(), Display.getHeight(), 0, 0, 0, dim, 1);
		Display.update();
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
		Rendering.drawRectangle(0, 0, Display.getWidth(), Display.getHeight(), 0, 0, 0, (fadeFrames/FADE_TIME), 1);
		GL11.glPopMatrix();
	}

}
