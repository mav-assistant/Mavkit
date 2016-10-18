package com.unascribed.mavkit.plugin;

public interface ProgressBar extends AutoCloseable {
	/**
	 * Create a second progress bar that will update in parallel. This is useful
	 * for showing progress on something in another thread or collection of
	 * threads that this bar needs to wait on.
	 * 
	 * @return a new ProgressBar that modifies the fork. Forking a forked
	 * 		bar is valid.
	 */
	ProgressBar fork();
	
	/**
	 * Set the title of this progress bar, such as "Loading resources"
	 * @param title The title to use
	 */
	void setTitle(String title);
	/**
	 * Set the subtitle of this progress bar, such as "resource_name.png"
	 * @param subtitle The subtitle to use
	 */
	void setSubtitle(String subtitle);
	/**
	 * Set the maximum value of this progress bar.
	 * @param maximum The maximum
	 */
	void setMaximum(long maximum);
	/**
	 * Increment the value of this progress bar. Attempts to advance past the
	 * maximum will be clamped.
	 * @param amount The amount to increment by
	 */
	void advance(long amount);
	
	
	String getTitle();
	String getSubtitle();
	long getValue();
	long getMaximum();
	
	/**
	 * Reset this progress bar's value to zero.
	 */
	void reset();
	
	/**
	 * Destroy this progress bar. Effectively the opposite of {@link #fork}.
	 */
	@Override
	void close();
}
