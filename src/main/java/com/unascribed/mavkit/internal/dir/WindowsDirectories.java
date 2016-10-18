package com.unascribed.mavkit.internal.dir;

import java.io.File;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.sun.jna.platform.win32.KnownFolders;
import com.sun.jna.platform.win32.Shell32Util;
import com.unascribed.mavkit.Directories;

public class WindowsDirectories implements Directories {

	private final String appName;
	private boolean hasUsedRuntimeDir = false;
	
	public WindowsDirectories(String appName) {
		this.appName = appName;
	}
	
	@Override
	@NonNull
	public File getDataHome() {
		File f = new File(getHome(), "Data");
		f.mkdirs();
		return f;
	}

	@Override
	@NonNull
	public File getConfigHome() {
		File f = new File(getHome(), "Config");
		f.mkdirs();
		return f;
	}

	@Override
	@NonNull
	public File getCacheHome() {
		File f = new File(getHome(), "Cache");
		f.mkdirs();
		return f;
	}

	@Override
	@NonNull
	public File getPluginHome() {
		File f = new File(getHome(), "Plugins");
		f.mkdirs();
		return f;
	}

	@Override
	@NonNull
	public File getRuntimeDir() {
		File f = new File(getHome(), "Runtime");
		if (!hasUsedRuntimeDir) {
			hasUsedRuntimeDir = true;
			Directories.deleteOnExit(f);
		}
		f.mkdirs();
		return f;
	}
	
	private File getHome() {
		return new File(Shell32Util.getKnownFolderPath(KnownFolders.FOLDERID_RoamingAppData), appName);
	}

}
