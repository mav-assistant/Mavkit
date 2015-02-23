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
package com.gameminers.mav.screen;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.TrueTypeFont;

import com.gameminers.mav.component.Component;

public abstract class Screen {
	public static TrueTypeFont[] lightFont;
	public static TrueTypeFont[] baseFont;
	private static BitSet mouseButtonStates;
	protected final List<Component> components = new ArrayList<>();
	
	public static void initMouse() {
		mouseButtonStates = new BitSet(Mouse.getButtonCount());
	}
	
	public final void render() {
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				keyDown(Keyboard.getEventKey(), Keyboard.getEventCharacter(), Keyboard.getEventNanoseconds());
			} else {
				keyUp(Keyboard.getEventKey(), Keyboard.getEventCharacter(), Keyboard.getEventNanoseconds());
			}
		}
		while (Mouse.next()) {
			if (Mouse.getEventButton() != -1 && Mouse.getEventButtonState() != mouseButtonStates.get(Mouse.getEventButton())) {
				mouseButtonStates.set(Mouse.getEventButton(), Mouse.getEventButtonState());
				if (Mouse.getEventButtonState()) {
					mouseDown(Mouse.getEventX(), Display.getHeight()-Mouse.getEventY(), Mouse.getEventButton(), Mouse.getEventNanoseconds());
				} else {
					mouseUp(Mouse.getEventX(), Display.getHeight()-Mouse.getEventY(), Mouse.getEventButton(), Mouse.getEventNanoseconds());
				}
			} else {
				mouseMove(Mouse.getEventX(), Display.getHeight()-Mouse.getEventY(), Mouse.getEventNanoseconds());
			}
			if (Mouse.getEventDWheel() != 0) {
				mouseWheel(Mouse.getEventX(), Display.getHeight()-Mouse.getEventY(), Mouse.getEventDWheel(), Mouse.getEventNanoseconds());
			}
		}
		preRender();
		for (Component c : components) {
			c.render();
		}
		doRender();
	}
	
	
	
	
	public final void keyDown(int k, char c, long nanos) {
		for (Component co : components) {
			co.keyDown(k, c, nanos);
		}
		onKeyDown(k, c, nanos);
	}
	public final void keyUp(int k, char c, long nanos) {
		for (Component co : components) {
			co.keyUp(k, c, nanos);
		}
		onKeyUp(k, c, nanos);
	}
	
	public final void mouseMove(int x, int y, long nanos) {
		for (Component co : components) {
			co.mouseMove(x, y, nanos);
		}
		onMouseMove(x, y, nanos);
	}
	public final void mouseDown(int x, int y, int button, long nanos) {
		for (Component co : components) {
			co.mouseDown(x, y, button, nanos);
		}
		onMouseDown(x, y, button, nanos);
	}
	public final void mouseUp(int x, int y, int button, long nanos) {
		for (Component co : components) {
			co.mouseUp(x, y, button, nanos);
		}
		onMouseUp(x, y, button, nanos);
	}
	public final void mouseWheel(int x, int y, int dWheel, long nanos) {
		for (Component co : components) {
			co.mouseWheel(x, y, dWheel, nanos);
		}
		onMouseWheel(x, y, dWheel, nanos);
	}




	public abstract void onKeyDown(int k, char c, long nanos);
	public abstract void onKeyUp(int k, char c, long nanos);
	
	public abstract void onMouseMove(int x, int y, long nanos);
	public abstract void onMouseDown(int x, int y, int button, long nanos);
	public abstract void onMouseUp(int x, int y, int button, long nanos);
	public abstract void onMouseWheel(int x, int y, int dWheel, long nanos);
	
	public abstract void doRender();
	public abstract void preRender();

}
