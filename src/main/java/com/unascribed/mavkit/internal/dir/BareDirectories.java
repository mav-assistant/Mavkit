package com.unascribed.mavkit.internal.dir;

import java.io.File;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.unascribed.mavkit.Directories;

public class BareDirectories implements Directories {

	private final String appName;
	private boolean hasUsedRuntimeDir = false;
	
	public BareDirectories(String appName) {
		this.appName = appName;
	}
	
	@Override
	@NonNull
	public File getDataHome() {
		File f = new File(getHome(), "data");
		f.mkdirs();
		return f;
	}

	@Override
	@NonNull
	public File getConfigHome() {
		File f = new File(getHome(), "config");
		f.mkdirs();
		return f;
	}

	@Override
	@NonNull
	public File getCacheHome() {
		File f = new File(getHome(), "cache");
		f.mkdirs();
		return f;
	}

	@Override
	@NonNull
	public File getPluginHome() {
		File f = new File(getHome(), "plugins");
		f.mkdirs();
		return f;
	}

	@Override
	@NonNull
	public File getRuntimeDir() {
		File f = new File(getHome(), "runtime");
		if (!hasUsedRuntimeDir) {
			hasUsedRuntimeDir = true;
			Directories.deleteOnExit(f);
		}
		f.mkdirs();
		return f;
	}

	private File getHome() {
		return new File(System.getProperty("user.home"), "."+appName);
	}

}
