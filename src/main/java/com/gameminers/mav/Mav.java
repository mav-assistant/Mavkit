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

import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

public class Mav {
	public static final File configDir = new File(System.getProperty("user.home"), ".mav");

	public static final int TARGET_FPS = 30;

	private static int frameCounter = 0;
	private static int fps = 0;
	private static long lastFPSUpdate = System.currentTimeMillis();

	public static Screen currentScreen = new MainScreen();
	private static boolean run = true;

	public static Personality personality = new TrianglePersonality();

	public static float targetHue = 120;
	public static float lagHue = 120;
	
	public static float targetSat = 1.0f;
	public static float lagSat = 1.0f;

	public static boolean idle = true;
	
	public static long totalFrameCounter = 0;

	public static String text = "What can I do for you?";
	
	public static VoiceThread voiceThread;

	public static void stop() {
		Display.destroy();
		voiceThread.interrupt();
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
			Display.setDisplayMode(new DisplayMode(320, 480));
			Display.setTitle("Mav");
			Display.setResizable(true);
			Display.create(new PixelFormat(24, 8, 8, 8, 8));
		} catch (Throwable t) {
			showErrorDialog(null, "An error occurred while initializing LWJGL. Mav will now exit.", t);
		}
		try {
			InputStream baseIn = ClassLoader.getSystemResourceAsStream("resources/fonts/OpenSans-Regular.ttf");
			InputStream lightIn = ClassLoader.getSystemResourceAsStream("resources/fonts/OpenSans-Light.ttf");

			Font baseFont = Font.createFont(Font.TRUETYPE_FONT, baseIn);
			Font lightFont = Font.createFont(Font.TRUETYPE_FONT, lightIn);

			baseIn.close();
			lightIn.close();
			
			Screen.baseFont = new TrueTypeFont[] {
					new TrueTypeFont(baseFont.deriveFont(12.0f), true),
					new TrueTypeFont(baseFont.deriveFont(24.0f), true),
					new TrueTypeFont(baseFont.deriveFont(36.0f), true),
					new TrueTypeFont(baseFont.deriveFont(48.0f), true),
			};
			Screen.lightFont = new TrueTypeFont[] {
					new TrueTypeFont(lightFont.deriveFont(12.0f), true),
					new TrueTypeFont(lightFont.deriveFont(24.0f), true),
					new TrueTypeFont(lightFont.deriveFont(36.0f), true),
					new TrueTypeFont(lightFont.deriveFont(48.0f), true),
			};
		} catch (Throwable t) {
			showErrorDialog(null, "An error occurred while initializing assets. Mav will now exit.", t);
		}
		try {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_LIGHTING);

			GL11.glClearDepth(1);
			
			GL11.glShadeModel(GL11.GL_FLAT);
			
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			voiceThread = new VoiceThread();
			voiceThread.start();
			text = "\u00A7LHi! I'm Mav.\nI don't know who you are yet,\nso let's fix that.\n\n\u00A7sSay 'Hey, Mav' to get started.";
			while (run) {
				GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
				GL11.glMatrixMode(GL11.GL_MODELVIEW);

				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				
				lagHue = tend(lagHue, targetHue, 16f);
				lagSat = tend(lagSat, targetSat, 16f);
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
			}
		} catch (Throwable t) {
			showErrorDialog(null, "An uncaught exception was thrown in the render loop. Mav will now exit.", t);
		}
	}

	private static void doRender() {
		/*((TrianglePersonality)personality).targetPulse = 0f;
		targetSat = 0f;
		idle = false;
		text = "\u00A7LListening paused.";*/
		float[] rgb = getColor(0.3f);
		GL11.glClearColor(rgb[0], rgb[1], rgb[2], 1);
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glPushMatrix();
		if (currentScreen != null) {
			GL11.glPushMatrix();
			currentScreen.render();
			GL11.glPopMatrix();
		} else {
			Screen.baseFont[1].drawString(4, 30, "There's no screen being displayed.", Color.white);
			Screen.lightFont[1].drawString(4, 60, "This shouldn't happen.", Color.white);
		}
		Screen.baseFont[0].drawString(8, 8, fps+" FPS");
		GL11.glPopMatrix();
	}

	public static void showErrorDialog(Component parent, String message, Throwable t) {
		t.printStackTrace();
		JOptionPane.showMessageDialog(parent, message+"\n"+t.toString()+"\n\nSee the console for more details.", "Error", JOptionPane.ERROR_MESSAGE, null);
	}
	
	public static float[] getColor(float lum) {
		return new java.awt.Color(java.awt.Color.HSBtoRGB(lagHue/360f, lagSat, lum)).getComponents(null);
	}

	public static float tend(float a, float b, float c) {
		if (a > b) {
			return a - ((a - b)/c);
		} else {
			return a + ((b - a)/c);
		}
	}

}
