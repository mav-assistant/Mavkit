/*
 * This file is part of Mav.
 *
 * Mav is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Mav is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with Mav. If not, see <http://www.gnu.org/licenses/>.
 */

package com.unascribed.mav;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

public class Bootstrap {

	private static Logger log = LoggerFactory.getLogger("Bootstrap");
	
	public static void main(String[] args) {
		extractNatives();
		Mav.instance.initialize();
	}

	private static void extractNatives() {
		try {
			String platform;
			String osName = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
			if (osName.contains("win")) {
				platform = "windows";
			} else if (osName.contains("mac")) {
				platform = "osx";
			} else if (osName.contains("linux")) {
				platform = "linux";
			} else {
				platform = "unknown";
			}
			log.info("Extracting natives for platform {}", platform);
			File location = new File(Bootstrap.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
			if (!location.isDirectory()) {
				File nativesDir = Files.createTempDir();
				nativesDir.deleteOnExit();
				log.info("Extracting natives to {}", nativesDir);
				log.info("Using {}", location);
				JarFile file = new JarFile(location);
				Enumeration<JarEntry> iter = file.entries();
				String prefix = "natives/" + platform + "/";
				while (iter.hasMoreElements()) {
					JarEntry en = iter.nextElement();
					if (en.getName().startsWith(prefix) && !en.getName().equals(prefix)) {
						log.debug("Processing {}", en.getName());
						File out = new File(nativesDir, en.getName().replaceFirst("^\\Q" + prefix + "\\E", ""));
						Files.createParentDirs(out);
						out.createNewFile();
						InputStream in = file.getInputStream(en);
						FileOutputStream fos = new FileOutputStream(out);
						ByteStreams.copy(in, fos);
						in.close();
						fos.close();
						out.deleteOnExit();
					}
				}
				file.close();
				System.setProperty("org.lwjgl.librarypath", nativesDir.getCanonicalPath());
			} else {
				log.info("Running from a directory, assuming natives are already in java.library.path");
			}
		} catch (Exception e) {
			Throwables.propagate(e);
		}
	}
	
}
