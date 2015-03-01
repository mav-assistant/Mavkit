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
package com.gameminers.mav.personality.poly;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.gameminers.mav.personality.Personality;
import com.gameminers.mav.render.RenderState;
import com.gameminers.mav.render.Rendering;

public class PolygonPersonality implements Personality {
	/*private static final float IDLE_TIME = 30000;
	private static final float IDLE_FADE_OUT_TIME = 10000;
	private static final float IDLE_FADE_IN_FRAMES = 30;*/
	
	public int sideCount;
	public PolygonPersonality(int sideCount) {
		this.sideCount = sideCount;
	}
	public long frameCount;
	public float angle;
	public float pulse;
	public float innerPulse;
	
	public float targetAngle;
	public float targetPulse;
	public float targetInnerPulse;
	
	protected float bgLum = 0.3f;
	//private int framesSinceFade = 0;
	@Override
	public void renderBackground() {
		/*long delta = System.currentTimeMillis()-Mav.lastInputEvent;
		if (delta > IDLE_TIME) {
			bgLum = (1.0f-(Math.min(delta-IDLE_TIME, IDLE_FADE_OUT_TIME)/IDLE_FADE_OUT_TIME))*0.3f;
			framesSinceFade = 0;
		} else {
			framesSinceFade++;
			bgLum = Math.min(0.3f, framesSinceFade/IDLE_FADE_IN_FRAMES);
		}*/
		float[] rgb = RenderState.getColor(bgLum);
		GL11.glClearColor(rgb[0], rgb[1], rgb[2], 1f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
	@Override
	public void renderForeground(float diameter) {
		GL11.glTranslatef(0, (0.3f-bgLum)*((Display.getHeight()/2f)-10), 0);
		diameter *= Math.max(1.0f, ((0.3f-bgLum)*3f)+1);
		setup();
		float radius = diameter/2f;
		float[] bg = RenderState.getColor(bgLum);
		float[] fg = RenderState.getColor(0.8f);
		Rendering.drawPolygon(0, 0, radius*(0.85f+(pulse*0.15f)), fg[0], fg[1], fg[2], 0.5f, sideCount, 0);
		Rendering.drawPolygon(0, 0, radius*0.8f, fg[0], fg[1], fg[2], 1, sideCount, 5f);
		Rendering.drawPolygon(0, 0, radius*0.7f, bg[0], bg[1], bg[2], 1, sideCount, 10f);
		Rendering.drawPolygon(0, 0, radius*0.7f, fg[0], fg[1], fg[2], 0.5f, sideCount, 15f);
		Rendering.drawPolygon(0, 0, radius*(0.7f-(innerPulse*0.15f)), bg[0], bg[1], bg[2], 1, sideCount, 20f);
	}
	@Override
	public void postRender() {
		
	}
	@Override
	public boolean renderScreen() {
		return bgLum == 0.3f;
	}
	protected void setup() {
		frameCount++;
		if (RenderState.attention) {
			angle = Rendering.tend(angle, targetAngle, 8f);
		} else {
			angle = (angle+0.05f)%360;
		}
		if (RenderState.idle) {
			targetPulse = (float)(Math.sin(frameCount/30f)+1)/2f;
		}
		pulse = Rendering.tend(pulse, targetPulse, 2f);
		innerPulse = Rendering.tend(innerPulse, targetInnerPulse, 2f);
		GL11.glRotatef(angle, 0f, 0f, 1f);
	}
	public int getSideCount() {
		return sideCount;
	}
	public void spin() {
		if (angle < 180) {
			targetAngle = 360;
		} else {
			targetAngle = 0;
		}
	}
	public void calm() {
		if (angle < 180) {
			targetAngle = 0;
		} else {
			targetAngle = 360;
		}
	}
	@Override
	public void renderIconBackground(int size) {
		renderBackground();
	}
	@Override
	public void renderIconForeground(int size) {
		if (sideCount < 4) {
			GL11.glTranslatef(0, -8, 0);
		}
		float radius = size/2f;
		float[] bg = RenderState.getColor(bgLum);
		float[] fg = RenderState.getColor(0.8f);
		Rendering.drawPolygon(0, 0, radius*1.1f, fg[0], fg[1], fg[2], 0.5f, sideCount, 0);
		Rendering.drawPolygon(0, 0, radius*0.7f, fg[0], fg[1], fg[2], 1, sideCount, 0.5f);
		Rendering.drawPolygon(0, 0, radius*0.4f, bg[0], bg[1], bg[2], 1, sideCount, 1f);
	}
}
