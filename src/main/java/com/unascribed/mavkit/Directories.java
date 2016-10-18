package com.unascribed.mavkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.LoggerFactory;

public interface Directories {
	/**
	 * @return The OS-specific single base directory relative to which
	 * 		user-specific data files should be written.
	 */
	@NonNull
	public File getDataHome();
	
	/**
	 * @return The OS-specific single base directory relative to which
	 * 		user-specific configuration files should be written.
	 */
	@NonNull
	public File getConfigHome();
	
	/**
	 * @return The OS-specific single base directory relative to which
	 * 		user-specific non-essential (cached) data should be written.
	 */
	@NonNull
	public File getCacheHome();
	
	/**
	 * @return The OS-specific single base directory relative to which
	 * 		plugins will be found.
	 */
	@NonNull
	public File getPluginHome();
	
	/**
	 * @return The OS-specific single base directory relative to which
	 * 		user-specific non-essential runtime files and other file objects
	 * 		(such as sockets, named pipes, ...) should be stored.
	 */
	@NonNull
	public File getRuntimeDir();

	public static void deleteOnExit(File dir) {
		if (dir == null) return;
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				Files.walkFileTree(dir.toPath(), new SimpleFileVisitor<Path>() {
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
				LoggerFactory.getLogger("Directories").warn("Failed to delete {}", dir, e);
			}
		}, "Runtime directory cleanup thread"));
	}
}
