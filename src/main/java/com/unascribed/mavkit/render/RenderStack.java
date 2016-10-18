package com.unascribed.mavkit.render;

import java.util.Collection;
import java.util.List;

import org.checkerframework.checker.guieffect.qual.UIEffect;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

public class RenderStack implements Renderable, Comparable<RenderStack> {
	protected final Logger log;
	
	protected final String id;
	protected final InterfaceRenderer ir;
	
	protected final List<Renderable> stack = Lists.newArrayList();
	
	protected int priority = 0;
	
	protected RenderStack(InterfaceRenderer ir, String id) {
		log = LoggerFactory.getLogger("RenderStack("+id+")");
		this.ir = ir;
		this.id = id;
	}
	
	@Override
	@UIEffect
	public void render(@NonNull Canvas c, double delta) {
		stack.forEach((r) -> r.render(c, delta));
	}
	
	/**
	 * Destroys this stack, causing it not to render anymore. A second call to
	 * {@link InterfaceRenderer#getStack(String)} with the same ID will return
	 * a new stack.
	 */
	@UIEffect
	public void destroy() {
		ir.stacks.remove(id);
		ir.sortedStacks.remove(this);
	}
	
	/**
	 * Push the given Renderable onto the top of this stack.
	 * @param r The Renderable to push
	 */
	@UIEffect
	public void push(Renderable r) {
		stack.add(r);
	}
	
	/**
	 * Insert the given Renderable into the bottom of this stack.
	 * @param r The Renderable to insert
	 */
	@UIEffect
	public void insert(Renderable r) {
		stack.add(0, r);
	}
	
	/**
	 * Replace this entire stack with the given Renderable, effectively
	 * displaying a new screen. If you want overlays that are not affected by
	 * this method, create multiple stacks.
	 * @param r The Renderable to replace the stack with
	 */
	@UIEffect
	public void replace(Renderable r) {
		stack.clear();
		stack.add(r);
	}
	
	/**
	 * Replace this entire stack with the given Renderables, effectively
	 * displaying a new screen. If you want overlays that are not affected by
	 * this method, create multiple stacks.
	 * @param arr The Renderables to replace the stack with
	 */
	@UIEffect
	public void replace(Renderable... arr) {
		stack.clear();
		for (Renderable r : arr) {
			stack.add(r);
		}
	}
	
	/**
	 * Replace this entire stack with the given Renderables, effectively
	 * displaying a new screen. If you want overlays that are not affected by
	 * this method, create multiple stacks.
	 * @param iter The Renderables to replace the stack with
	 */
	@UIEffect
	public void replace(Collection<Renderable> iter) {
		stack.clear();
		stack.addAll(iter);
	}
	
	/**
	 * @param priority The new priority for this stack. Higher priority stacks
	 * 		draw on top of lower priority stacks.
	 */
	public void setPriority(int priority) {
		this.priority = priority;
		ir.needsResort = true;
	}
	
	public int getPriority() {
		return priority;
	}

	@Override
	public int compareTo(RenderStack o) {
		return Ints.compare(priority, o.priority);
	}
	
	

}
