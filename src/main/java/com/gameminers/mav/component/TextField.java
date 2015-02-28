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
package com.gameminers.mav.component;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.gameminers.mav.Mav;
import com.gameminers.mav.render.Fonts;
import com.gameminers.mav.render.RenderState;
import com.gameminers.mav.render.Rendering;


public class TextField extends Component {
	private boolean focused;
	private int frames = 0;
	private StringBuilder content = new StringBuilder();
	private int cursorPos = 0;
	private int viewPos = 0;
	private String str = "";
	@Override
	public void doRender() {
		if (viewPos > str.length()) {
			viewPos = str.length();
		} else if (viewPos < 0) {
			viewPos = 0;
		}
		frames++;
		float[] fg = RenderState.getColor(0.8f);
		float[] bg = RenderState.getColor(0.3f);
		Rendering.drawRectangle(0, 0, 16, 16, 1, 0, 0, 0, 0);
		Rendering.drawRectangle(0, 0, width, height, fg[0], fg[1], fg[2], 1.0f, 0f);
		Rendering.drawRectangle(2, 2, width-4, height-4, bg[0], bg[1], bg[2], 1.0f, 0f);
		
		// roughly ported from Glass Pane
		if (cursorPos < 0) {
			cursorPos = 0;
		} else if (cursorPos > content.length()) {
			cursorPos = content.length();
		}
		String trimmedText = trimStringToWidth(str.substring(viewPos), width-24);
		int trimmedLength = trimmedText.length();
		if (cursorPos > viewPos+trimmedLength) {
			viewPos = cursorPos - trimmedLength;
		} else if (cursorPos < viewPos) {
			viewPos = cursorPos;
		}
		int len = Fonts.base[1].getWidth(trimmedText);
		int mod = (int) (len >= width-16 ? len - (width-16) : 0);
		if (viewPos == 0) {
			mod = 0;
		}
		if (focused) {
			Rendering.drawRectangle((8-mod)+Fonts.base[1].getWidth(trimmedText.substring(0, Math.min(trimmedLength, Math.max(0, cursorPos-viewPos)))), 6, 2, height-12, 1, 1, 1, (1.0f-((frames%25)/40f)) / (Display.isActive() ? 1f : 4f), 0.2f);
		}
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor((int)x+8, Display.getHeight()-(int)(y+height), (int)width-16, (int)height);
		Fonts.base[1].drawString(8-mod, 1, trimmedText);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}

	private String trimStringToWidth(String str, float width) {
		if (str.length() == 0) return str;
		StringBuilder sb = new StringBuilder();
		float totalWidth = 0;
		int idx = 0;
		do {
			String ch = Character.toString(str.charAt(idx));
			sb.append(ch);
			totalWidth += Fonts.base[1].getWidth(ch);
			idx++;
		} while (totalWidth < width && idx < str.length());
		return sb.toString();
	}

	@Override
	public void keyDown(int k, char c, long nanos) {
		if (focused) {
			if (k == Keyboard.KEY_BACK) {
				if (content.length() > 0 && cursorPos > 0) {
					content.deleteCharAt(cursorPos-1);
					str = content.toString();
					cursorPos--;
				} else {
					Mav.audioManager.playClip("fail2");
				}
			} else if (k == Keyboard.KEY_DELETE) {
				if (cursorPos < content.length()) {
					content.deleteCharAt(cursorPos);
					str = content.toString();
				} else {
					Mav.audioManager.playClip("fail2");
				}
			} else if (k == Keyboard.KEY_LEFT) {
				if (cursorPos > 0) {
					cursorPos--;
				} else {
					Mav.audioManager.playClip("fail2");
				}
			} else if (k == Keyboard.KEY_RIGHT) {
				if (cursorPos < content.length()) {
					cursorPos++;
				} else {
					Mav.audioManager.playClip("fail2");
				}
			} else if (k == Keyboard.KEY_HOME) {
				cursorPos = 0;
			} else if (k == Keyboard.KEY_END) {
				cursorPos = content.length();
			} else if (!Character.isISOControl(c)) {
				content.insert(cursorPos, c);
				str = content.toString();
				cursorPos++;
			}
		}
	}

	@Override
	public void keyUp(int k, char c, long nanos) {
	}

	@Override
	public void mouseMove(int x, int y, long nanos) {
	}
	
	@Override
	public void mouseDown(int x, int y, int button, long nanos) {
		if ((x >= this.x && x <= this.x+this.width) && (y >= this.y && y <= this.y+this.height)) {
			focused = true;
		} else {
			focused = false;
		}
	}

	@Override
	public void mouseUp(int x, int y, int button, long nanos) {
	}

	@Override
	public void mouseWheel(int x, int y, int dWheel, long nanos) {
	}

	public String getText() {
		return str;
	}

	public void setText(String str) {
		content.setLength(0);
		content.append(str);
		this.str = str;
	}

	public void focus() {
		focused = true;
	}

}
