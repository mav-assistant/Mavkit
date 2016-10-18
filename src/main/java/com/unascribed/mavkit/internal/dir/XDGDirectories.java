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

package com.unascribed.mavkit.internal.dir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermissions;

import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.PolyNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unascribed.mavkit.Directories;

/**
 * Basic implementation of the <a href="https://specifications.freedesktop.org/basedir-spec/basedir-spec-latest.html">XDG Base Directory Specification</a>,
 * in Java (obviously).
 */
public class XDGDirectories implements Directories {
	private static final Logger log = LoggerFactory.getLogger("XDGDirectories");
	
	private final String appName;
	@MonotonicNonNull
	private File runtimeDir;
	
	public XDGDirectories(String appName) {
		this.appName = appName;
	}
	
	/**
	 * @return The single base directory relative to which user-specific data
	 * 		files should be written. This directory is defined by the
	 * 		environment variable {@code $XDG_DATA_HOME}. 
	 */
	@Override
	@NonNull
	public File getDataHome() {
		return getBaseDir("XDG_DATA_HOME", "/.local/share");
	}
	
	@Override
	@NonNull
	public File getPluginHome() {
		return new File(getDataHome(), "plugins");
	}
	
	/**
	 * @return The single base directory relative to which user-specific
	 * 		configuration files should be written. This directory is defined by
	 * 		the environment variable {@code $XDG_CONFIG_HOME}. 
	 */
	@Override
	@NonNull
	public File getConfigHome() {
		return getBaseDir("XDG_CONFIG_HOME", "/.config");
	}
	
	/**
	 * @return The single base directory relative to which user-specific
	 * 		non-essential (cached) data should be written. This directory is defined
	 * 		by the environment variable {@code $XDG_CACHE_HOME}.
	 */
	@Override
	@NonNull
	public File getCacheHome() {
		return getBaseDir("XDG_CACHE_HOME", "/.cache");
	}
	
	/**
	 * @return The single base directory relative to which user-specific
	 * 		non-essential runtime files and other file objects (such as sockets,
	 * 		named pipes, ...) should be stored.
	 */
	@Override
	@NonNull
	@EnsuresNonNull("this.runtimeDir")
	public File getRuntimeDir() {
		if (runtimeDir != null) return runtimeDir;
		File dir = getBaseDir("XDG_RUNTIME_DIR", null);
		if (dir == null) {
			log.warn("Synthesizing runtime directory, as $XDG_RUNTIME_DIR is unset");
			dir = new File(System.getProperty("java.io.tmpdir"));
			dir = new File(dir, appName+"-"+System.getProperty("user.name"));
			dir.mkdirs();
		}
		try {
			Files.setPosixFilePermissions(dir.toPath(), PosixFilePermissions.fromString("rwx------"));
		} catch (IOException | UnsupportedOperationException e) {
			log.warn("Failed to set directory permissions on {} to owner-only", dir, e);
		}
		runtimeDir = dir;
		Directories.deleteOnExit(dir);
		return dir;
	}

	@PolyNull
	private File getBaseDir(@NonNull String env, @PolyNull String def) {
		String home = System.getenv("HOME");
		if (home == null || home.trim().isEmpty()) {
			home = System.getProperty("user.home");
		}
		String dir = System.getenv(env);
		if (dir == null || dir.trim().isEmpty()) {
			if (def == null) return null;
			dir = home+def;
		}
		File f = new File(dir, appName);
		f.mkdirs();
		return f;
	}
	
}
