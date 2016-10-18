package com.unascribed.mavkit.plugin;

import java.io.File;

import org.checkerframework.checker.guieffect.qual.UIEffect;

import com.unascribed.mavkit.render.Canvas;
import com.unascribed.mavkit.render.Canvas.ImageMode;

public interface Plugin {
	String getName();
	String getVersion();
	boolean isPrimary();
	
	/**
	 * Called on the UI thread during init. Load any resources you need.
	 * @see Canvas#loadFont(String, File)
	 * @see Canvas#loadFont(String, String)
	 * @see Canvas#loadImage(File, ImageMode...)
	 * @see Canvas#loadImage(String, ImageMode...)
	 */
	@UIEffect
	void loadResources(Canvas canvas);
	
	/**
	 * Called on the main thread to  
	 */
	void register();
}
