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

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;

import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.PolyNull;
import org.lwjgl.system.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic implementation of the <a href="https://specifications.freedesktop.org/basedir-spec/basedir-spec-latest.html">XDG Base Directory Specification</a>,
 * in Java (obviously).
 */
public class XDGDirectories {
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
	@NonNull
	public File getDataHome() {
		return getBaseDir("XDG_DATA_HOME", "/.local/share");
	}
	
	/**
	 * @return The single base directory relative to which user-specific
	 * 		configuration files should be written. This directory is defined by
	 * 		the environment variable {@code $XDG_CONFIG_HOME}. 
	 */
	@NonNull
	public File getConfigHome() {
		return getBaseDir("XDG_CONFIG_HOME", "/.config");
	}
	
	/**
	 * @return The single base directory relative to which user-specific
	 * 		non-essential (cached) data should be written. This directory is defined
	 * 		by the environment variable {@code $XDG_CACHE_HOME}.
	 */
	@NonNull
	public File getCacheHome() {
		return getBaseDir("XDG_CACHE_HOME", "/.cache");
	}
	
	/**
	 * @return The single base directory relative to which user-specific
	 * 		non-essential runtime files and other file objects (such as sockets,
	 * 		named pipes, ...) should be stored.
	 */
	@NonNull
	@EnsuresNonNull("this.runtimeDir")
	public File getRuntimeDir() {
		if (runtimeDir != null) return runtimeDir;
		File dir = getBaseDir("XDG_RUNTIME_DIR", null);
		if (dir == null) {
			log.warn("Synthesizing runtime directory, as $XDG_RUNTIME_DIR is unset");
			String tempDir;
			if (Platform.get() == Platform.WINDOWS) {
				tempDir = System.getenv("TEMP");
				if (tempDir == null || tempDir.trim().isEmpty()) {
					// XXX is this actually correct? I don't have access to a
					// Windows box to test on...
					tempDir = "C:\\Windows\\Temp";
				}
			} else {
				tempDir = System.getenv("TMPDIR");
				if (tempDir == null || tempDir.trim().isEmpty()) {
					tempDir = "/tmp";
				}
			}
			dir = new File(tempDir);
			dir = new File(dir, appName+"-"+System.getProperty("user.name"));
			dir.mkdirs();
		}
		try {
			Files.setPosixFilePermissions(dir.toPath(), PosixFilePermissions.fromString("rwx------"));
		} catch (IOException | UnsupportedOperationException e) {
			if (Platform.get() != Platform.WINDOWS) {
				log.warn("Failed to set directory permissions on {} to owner-only", dir, e);
			}
		}
		runtimeDir = dir;
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				if (runtimeDir == null) return;
				Files.walkFileTree(runtimeDir.toPath(), new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						Files.delete(file);
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
						Files.delete(dir);
						return FileVisitResult.CONTINUE;
					}
				});
			} catch (IOException e) {
				log.warn("Failed to delete {}", runtimeDir, e);
			}
		}, "Runtime directory cleanup thread"));
		return dir;
	}

	@PolyNull
	private File getBaseDir(@NonNull String env, @PolyNull String def) {
		String home = System.getenv("HOME");
		if (home == null || home.trim().isEmpty()) {
			// Since we require Java 8, this is safe. On 7 and earlier, this
			// could return the wrong directory sometimes on Windows.
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
