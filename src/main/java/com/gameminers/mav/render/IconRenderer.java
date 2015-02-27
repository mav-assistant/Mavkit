package com.gameminers.mav.render;

import java.nio.ByteBuffer;

import org.apache.commons.lang.ArrayUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.opengl.PixelFormat;

import com.gameminers.mav.Mav;

public class IconRenderer extends Thread {
	public static final int SIZE = 64;
	private Pbuffer pbuffer;
	private volatile boolean run = true;
	private float[] previousRGB = null;
	public IconRenderer() {
		super("Icon render thread");
	}
	@Override
	public void run() {
		try {
			init();
		} catch (Exception e) {
			return;
		}
		try {
			final ByteBuffer icon = BufferUtils.createByteBuffer(SIZE*SIZE*4);
			while (run) {
				// LWJGL makes a deep copy of our ByteBuffer, so skip frames we don't need to render
				if (!ArrayUtils.isEquals(RenderState.lagRGB, previousRGB)) {
					previousRGB = RenderState.lagRGB.clone();
					Rendering.beforeFrame(SIZE, SIZE);
					GL11.glPushMatrix();
						Mav.personality.renderIconBackground(SIZE);
						GL11.glTranslatef(SIZE/2f, SIZE/2f, 0);
						Mav.personality.renderIconForeground(SIZE);
					GL11.glPopMatrix();
					GL11.glReadBuffer(GL11.GL_FRONT);
					GL11.glReadPixels(0, 0, SIZE, SIZE, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, icon);
					Display.setIcon(new ByteBuffer[] {icon});
				}
				sleep(33L); // We don't care very much about accuracy, and using LWJGL's sync() method screws up the main context
			}
		} catch (InterruptedException e) {}
	}
	protected void init() throws LWJGLException {
		try {
			pbuffer = new Pbuffer(SIZE, SIZE, new PixelFormat(24, 0, 0, 0, 4), null);
		} catch (LWJGLException e) {
			try {
				pbuffer = new Pbuffer(SIZE, SIZE, new PixelFormat(24, 0, 0, 0, 0), null);
			} catch (LWJGLException ex) {
				ex.printStackTrace();
				System.err.println("The current graphics driver is incapable of Pbuffers. The icon will be static.");
				throw ex;
			}
		}
		pbuffer.makeCurrent();
		Rendering.setUpGL();
	}
	public void finish() {
		run = false;
	}

}
