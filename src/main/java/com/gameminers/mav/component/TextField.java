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

import com.gameminers.mav.render.RenderState;
import com.gameminers.mav.render.Rendering;


public class TextField extends Component {

	@Override
	public void doRender() {
		float[] fg = RenderState.getColor(0.8f);
		float[] bg = RenderState.getColor(0.3f);
		Rendering.drawRectangle(0, 0, 16, 16, 1, 0, 0, 0, 0);
		Rendering.drawRectangle(0, 0, width, height, fg[0], fg[1], fg[2], 1.0f, 0f);
		Rendering.drawRectangle(2, 2, width-4, height-4, bg[0], bg[1], bg[2], 1.0f, 0f);
	}

	@Override
	public void keyDown(int k, char c, long nanos) {
		System.out.println("keyDown - "+c+" ["+k+"] @ "+nanos);
	}

	@Override
	public void keyUp(int k, char c, long nanos) {
		System.out.println("keyUp - "+c+" ["+k+"] @ "+nanos);
	}

	@Override
	public void mouseMove(int x, int y, long nanos) {
		System.out.println("mouseMove - "+x+", "+y+" @ "+nanos);
	}
	
	@Override
	public void mouseDown(int x, int y, int button, long nanos) {
		System.out.println("mouseDown - "+x+", "+y+" ["+button+"] @ "+nanos);
	}

	@Override
	public void mouseUp(int x, int y, int button, long nanos) {
		System.out.println("mouseUp - "+x+", "+y+" ["+button+"] @ "+nanos);
	}

	@Override
	public void mouseWheel(int x, int y, int dWheel, long nanos) {
		System.out.println("mouseWheel - "+x+", "+y+" ["+dWheel+"] @ "+nanos);
	}

}
