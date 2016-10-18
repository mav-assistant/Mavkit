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

package com.unascribed.mavkit.render;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.checkerframework.checker.guieffect.qual.UIEffect;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.unascribed.mavkit.Mavkit;

public class InterfaceRenderer {
	/**
	 * The ID of the default stack, for use when you don't have a complex use
	 * case and just need one stack.
	 */
	public static final String DEFAULT_STACK = "default";
	
	private static final Logger log = LoggerFactory.getLogger("InterfaceRenderer");
	private static final double NANOSECOND_RECIPROCAL = 1/1000000000D;
	
	@MonotonicNonNull
	private Mavkit mav;
	
	public double timeElapsed;
	
	
	@Nullable
	private Renderable background;
	protected final Map<String, RenderStack> stacks = Maps.newHashMap();
	protected final List<RenderStack> sortedStacks = Lists.newArrayList();
	protected boolean needsResort = true;
	
	@UIEffect
	@EnsuresNonNull("this.mav")
	public void start(Mavkit mav) {
		this.mav = mav;
	}
	
	@UIEffect
	@RequiresNonNull("this.mav")
	public void run() {
		Canvas canvas = mav.getDisplay().getCanvas();
		long lastTime = System.nanoTime();
		while (!mav.getDisplay().shouldClose()) {
			long curTime = System.nanoTime();
			double delta = nanosToSeconds(curTime-lastTime);
			lastTime = curTime;
			
			timeElapsed += delta;
			
			canvas.beginFrame();
			if (background != null) {
				background.render(canvas, delta);
			}
			if (needsResort) {
				Collections.sort(sortedStacks);
				needsResort = false;
			}
			sortedStacks.forEach((s) -> s.render(canvas, delta));
			
			canvas.endFrame();
			mav.getDisplay().swap();
		}
	}

	private double nanosToSeconds(long l) {
		return l*NANOSECOND_RECIPROCAL;
	}
	
	/**
	 * @return The default render stack
	 * @see #getStack(String)
	 * @see #DEFAULT_STACK
	 */
	@UIEffect
	public RenderStack getStack() {
		return getStack(DEFAULT_STACK);
	}
	
	/**
	 * @param id The ID of the stack, such as {@link #DEFAULT_STACK}
	 * @return The stack with the given ID, potentially newly created
	 */
	@UIEffect
	public RenderStack getStack(String id) {
		if (!stacks.containsKey(id)) {
			RenderStack stack = new RenderStack(null, id);
			stacks.put(id, stack);
			sortedStacks.add(stack);
		}
		return stacks.get(id);
	}
	
	/**
	 * @return The current background, which is rendered before anything in the
	 * 		render stack.
	 */
	@UIEffect
	public Renderable getBackground() {
		return background;
	}
	
	@UIEffect
	public void setBackground(Renderable background) {
		this.background = background;
	}
	
}
