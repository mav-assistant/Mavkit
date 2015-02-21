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

import org.lwjgl.opengl.Display;

import com.gameminers.mav.component.TextField;
import com.gameminers.mav.screen.Screen;

public class FirstRunScreen extends Screen {
	private final TextField tf = new TextField();
	public FirstRunScreen() {
		components.add(tf);
	}
	@Override
	public void preRender() {
		tf.setX(16);
		tf.setWidth(Display.getWidth()-32);
		tf.setY(Display.getHeight()-52);
		tf.setHeight(36);
	}
	@Override
	public void doRender() {
		
	}
	@Override
	public void onKeyDown(int k, char c, long nanos) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onKeyUp(int k, char c, long nanos) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMouseMove(int x, int y, long nanos) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMouseDown(int x, int y, int button, long nanos) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMouseUp(int x, int y, int button, long nanos) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMouseWheel(int x, int y, int dWheel, long nanos) {
		// TODO Auto-generated method stub
		
	}

}
