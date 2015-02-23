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

import org.lwjgl.opengl.Display;

import com.gameminers.mav.component.TextField;

public abstract class InputScreen extends Screen {
	protected final TextField tf = new TextField();
	public InputScreen() {
		components.add(tf);
		tf.focus();
	}
	@Override
	public void preRender() {
		tf.setX(16);
		tf.setWidth(Display.getWidth()-32);
		tf.setY(Display.getHeight()-52);
		tf.setHeight(36);
	}
}
