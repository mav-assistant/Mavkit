package com.unascribed.mavkit.internal.dir;

import java.io.File;

import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

import com.google.common.io.Files;
import com.unascribed.mavkit.Directories;

/**
 * <a href="https://developer.apple.com/library/content/qa/qa1170/_index.html">https://developer.apple.com/library/content/qa/qa1170/_index.html</a>
 */
public class MacDirectories implements Directories {

	private final String appName;
	@MonotonicNonNull
	private File runtimeDir;
	
	public MacDirectories(String appName) {
		this.appName = appName;
	}
	
	@Override
	@NonNull
	public File getDataHome() {
		File f = new File(getConfigHome(), "Data");
		f.mkdirs();
		return f;
	}

	@Override
	@NonNull
	public File getConfigHome() {
		File f = new File(getLibrary("Preferences"), appName);
		f.mkdirs();
		return f;
	}

	@Override
	@NonNull
	public File getCacheHome() {
		File f = new File(getLibrary("Caches"), appName);
		f.mkdirs();
		return f;
	}
	
	@Override
	@NonNull
	public File getPluginHome() {
		File f = new File(getLibrary("Application Support"), appName);
		f.mkdirs();
		return f;
	}

	private File getLibrary(String base) {
		return new File(System.getProperty("user.home"), "Library/"+base);
	}

	@Override
	@NonNull
	@EnsuresNonNull("this.runtimeDir")
	public File getRuntimeDir() {
		if (runtimeDir == null) {
			runtimeDir = Files.createTempDir();
			Directories.deleteOnExit(runtimeDir);
		}
		return runtimeDir;
	}

}
