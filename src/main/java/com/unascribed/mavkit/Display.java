/*
 * This file is part of Mavkit.
 *
 * Mavkit is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Mavkit is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mavkit. If not, see <http://www.gnu.org/licenses/>.
 */

package com.unascribed.mavkit;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.IntBuffer;
import java.util.Map;

import org.checkerframework.checker.guieffect.qual.UIEffect;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengles.GLES;
import org.lwjgl.opengles.GLES20;
import org.lwjgl.system.APIUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unascribed.mavkit.internal.canvas.gl.NanoVGGL2Canvas;
import com.unascribed.mavkit.internal.canvas.gl.NanoVGGL3Canvas;
import com.unascribed.mavkit.internal.canvas.gles.NanoVGGLES2Canvas;
import com.unascribed.mavkit.internal.canvas.gles.NanoVGGLES3Canvas;
import com.unascribed.mavkit.render.Canvas;

public class Display {
	private static final Logger log = LoggerFactory.getLogger("Display");
	private static final int DEFAULT_WIDTH = 240;
	private static final int DEFAULT_HEIGHT = 480;
	
	@MonotonicNonNull
	private Mavkit mav;
	
	private IntBuffer width = BufferUtils.createIntBuffer(1);
	private IntBuffer height = BufferUtils.createIntBuffer(1);
	private float pixelRatio;
	
	private long window;
	private Canvas canvas = new NoOpCanvas();
	
	@EnsuresNonNull({"this.mav", "this.canvas"})
	@UIEffect
	public void start(Mavkit mav) {
		this.mav = mav;
		String[] lastError = new String[] { I18n.get("panic.glfwInitFailed.default") };
		// Checker Framework is assuming @NonNull for all params on apiClassTokens
		@SuppressWarnings("argument.type.incompatible")
		Map<Integer, String> ERROR_CODES = APIUtil.apiClassTokens((field, value) -> 0x10000 < value && value < 0x20000, null, GLFW.class);
		glfwSetErrorCallback(GLFWErrorCallback.create((e, d) -> {
			lastError[0] = ERROR_CODES.get(e)+"\n"+GLFWErrorCallback.getDescription(d);
		}));
		if (!glfwInit()) {
			throw new Panic("panic.glfwInitFailed", lastError[0]);
		}
		
		boolean[] hasError = new boolean[] { false };
		
		GLFWErrorCallback ecb = GLFWErrorCallback.create((e, d) -> {
			hasError[0] = true;
		});
		glfwSetErrorCallback(ecb);
		
		
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		
		log.debug("Trying OpenGL 3.0");
		glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_API);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		if (tryInitialization(hasError, false)) return;
		
		log.debug("Trying OpenGL 2.0");
		glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_API);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_ANY_PROFILE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
		if (tryInitialization(hasError, false)) return;
		
		log.debug("Trying OpenGL ES 3.0");
		glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_ES_API);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
		if (tryInitialization(hasError, true)) return;
		
		log.debug("Trying OpenGL ES 2.0");
		glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_ES_API);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
		if (tryInitialization(hasError, true)) return;
		
		glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_API);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 1);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
		createWindow();
		
		String version;
		if (hasError[0]) {
			hasError[0] = false;
			glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_ANY_PROFILE);
			createWindow();
			if (hasError[0]) {
				hasError[0] = false;
				glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_ES_API);
				createWindow();
				if (hasError[0]) {
					version = I18n.get("panic.noSuitableContext.nothing");
				} else {
					version = I18n.get("panic.noSuitableContext.onlyGlEs", getGLVersion(true));
				}
			} else {
				version = I18n.get("panic.noSuitableContext.onlyGl", getGLVersion(false));
			}
		} else {
			version = I18n.get("panic.noSuitableContext.onlyGl", getGLVersion(false));
		}
		
		if (window != 0) {
			glfwDestroyWindow(window);
			window = 0;
		}
		
		throw new Panic("panic.noSuitableContext", version);
	}
	
	public int getWidth() {
		return width.get(0);
	}
	public int getHeight() {
		return height.get(0);
	}
	public float getPixelRatio() {
		return pixelRatio;
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	@UIEffect
	public boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}
	
	@UIEffect
	public void swap() {
		glfwSwapBuffers(window);
		glfwPollEvents();
	}

	@RequiresNonNull("this.mav")
	@UIEffect
	private boolean tryInitialization(boolean[] arr, boolean es) {
		createWindow();
		if (arr[0]) {
			arr[0] = false;
			return false;
		} else {
			finishInitialization(es);
			return true;
		}
	}
	
	@RequiresNonNull("this.mav")
	@UIEffect
	private void createWindow() {
		window = glfwCreateWindow(DEFAULT_WIDTH, DEFAULT_HEIGHT, I18n.get("window.title"), 0, 0);
		glfwMakeContextCurrent(window);
	}
	
	@RequiresNonNull("this.mav")
	@EnsuresNonNull("this.canvas")
	@UIEffect
	private void finishInitialization(boolean es) {
		GLFWErrorCallback ecb = GLFWErrorCallback.createPrint(System.err);
		mav.keepAlive(ecb);
		glfwSetErrorCallback(ecb);
		
		GLFWWindowSizeCallback scb = GLFWWindowSizeCallback.create(this::updateWindowSize);
		mav.keepAlive(scb);
		glfwSetWindowSizeCallback(window, scb);
		
		updateWindowSize(window, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		
		String version = getGLVersion(es);
		int major = version.charAt(0)-'0';
		int minor = version.charAt(2)-'0';
		
		if (es) {
			if (major >= 3) {
				log.info("Using OpenGL ES 3 rendering");
				canvas = new NanoVGGLES3Canvas(mav);
			} else if (major >= 2) {
				log.info("Using OpenGL ES 2 rendering");
				canvas = new NanoVGGLES2Canvas(mav);
			} else {
				throw new Panic("panic.noSuitableContext", I18n.get("panic.noSuitableContext.onlyGlEs", version));
			}
		} else {
			if (major >= 3) {
				log.info("Using OpenGL 3 rendering");
				canvas = new NanoVGGL3Canvas(mav);
			} else if (major >= 2) {
				log.info("Using OpenGL 2 rendering");
				canvas = new NanoVGGL2Canvas(mav);
			} else {
				throw new Panic("panic.noSuitableContext", I18n.get("panic.noSuitableContext.onlyGl", version));
			}
		}
		
		glfwShowWindow(window);
		
		if (glfwExtensionSupported("WGL_EXT_swap_control_tear") || glfwExtensionSupported("GLX_EXT_swap_control_tear")) {
			log.info("Using tearing prevention");
			glfwSwapInterval(-1);
		} else {
			glfwSwapInterval(1);
		}
	}

	@UIEffect
	private String getGLVersion(boolean es) {
		String fullVersion;
		String renderer;
		
		if (es) {
			GLES.createCapabilities();
			fullVersion = GLES20.glGetString(GLES20.GL_VERSION);
			renderer = GLES20.glGetString(GLES20.GL_RENDERER);
		} else {
			GL.createCapabilities();
			fullVersion = GL11.glGetString(GL11.GL_VERSION);
			renderer = GL11.glGetString(GL11.GL_RENDERER);
		}
		
		log.info("{}", fullVersion);
		log.info("{}", renderer);
		
		String version = fullVersion.split(" ", 2)[0];
		return version;
	}

	@UIEffect
	private void updateWindowSize(long window, int baseWidth, int baseHeight) {
		glfwGetFramebufferSize(window, width, height);
		pixelRatio = getWidth()/(float)baseWidth;
	}
	
}
