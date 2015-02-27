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

import com.gameminers.mav.render.RenderState;
import com.gameminers.mav.screen.Screen;

public class TeachSphinxScreen extends Screen {

	public TeachSphinxScreen() {
		RenderState.setText("\u00A7LGetting ready...");
		new TeachSphinxThread().start();
	}
	
	@Override
	public void onKeyDown(int k, char c, long nanos) {
		
	}

	@Override
	public void onKeyUp(int k, char c, long nanos) {

	}

	@Override
	public void onMouseMove(int x, int y, long nanos) {

	}

	@Override
	public void onMouseDown(int x, int y, int button, long nanos) {

	}

	@Override
	public void onMouseUp(int x, int y, int button, long nanos) {

	}

	@Override
	public void onMouseWheel(int x, int y, int dWheel, long nanos) {

	}

	@Override
	public void doRender() {

	}

	@Override
	public void preRender() {

	}

}
