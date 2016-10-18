package com.unascribed.mavkit.internal.plugin;

import java.util.concurrent.atomic.LongAdder;

import com.unascribed.mavkit.plugin.ProgressBar;

public abstract class AtomicProgressBar implements ProgressBar {
	private volatile String title;
	private volatile String subtitle;
	private volatile long maximum; 
	private LongAdder value;
	

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	@Override
	public void setMaximum(long maximum) {
		this.maximum = maximum;
	}

	@Override
	public void advance(long amount) {
		value.add(amount);
	}

	@Override
	public void reset() {
		value.reset();
	}

	@Override
	public String getTitle() {
		return title;
	}
	
	@Override
	public String getSubtitle() {
		return subtitle;
	}
	
	@Override
	public long getMaximum() {
		return maximum;
	}
	
	@Override
	public long getValue() {
		return value.sum();
	}
	
	

}
