/*
 * This file is part of Mav.
 *
 * Mav is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Mav is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mav. If not, see <http://www.gnu.org/licenses/>.
 */

package com.unascribed.mav;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

public final class Mav {
	private static Logger log = LoggerFactory.getLogger("Mav");
	
	private String version;
	private String googleApiKey;
	
	private Display display;
	
	private List<Object> keepAlive = Lists.newArrayList();
	
	private static final String[] initMessages = {
		"Mav {} at your service.",
		"Initializing Mav version {}",
		"Hello, World! This is Mav {}",
	};
	
	public void initialize() {
		Thread.currentThread().setName("Main thread");
		
		version = System.getProperty("com.unascribed.mav.version", I18n.get("application.versionPlaceholder"));
		log.info(initMessages[(int)(Math.random()*initMessages.length)], version);
		googleApiKey = System.getProperty("com.unascribed.mav.googleApiKey");
		if (Strings.isNullOrEmpty(googleApiKey) || googleApiKey.equals("MY-API-KEY")) {
			googleApiKey = null;
		}
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			log.info("Using GTK+ LaF for error dialogs.");
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				log.info("Using system LaF for error dialogs.");
			} catch (Exception e2) {
				log.info("Using default LaF for error dialogs.");
			}
		}
		
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
			if (e instanceof Panic) {
				log.error("Panic!", e);
				JOptionPane.showMessageDialog(null, I18n.get("dialog.panic.title")+e.getLocalizedMessage(), I18n.get("window.title", version), JOptionPane.ERROR_MESSAGE, null);
				System.exit(2);
			} else {
				log.error("{} died", t.getName(), e);
				if ("Main thread".equals(t.getName())) {
					JOptionPane.showMessageDialog(null, I18n.get("dialog.error.title")+Throwables.getStackTraceAsString(e), I18n.get("window.title", version), JOptionPane.ERROR_MESSAGE, null);
					System.exit(1);
				}
			}
		});
		
		display = new Display(this);
		display.initialize();
		
		InterfaceRenderer ir = new InterfaceRenderer(this);
		AudioProcessor ap = new AudioProcessor(this);
		new Thread(ap::run, "Audio thread").start();
		
		ir.run();
	}
	
	public Display getDisplay() {
		return display;
	}
	
	public String getVersion() {
		return version;
	}

	public String getGoogleApiKey() {
		return googleApiKey;
	}

	/**
	 * Prevent the passed object from being garbage-collected.
	 */
	public void keepAlive(Object o) {
		keepAlive.add(o);
	}
	
	/**
	 * Stop preventing the passed object from being garbage-collected.
	 */
	public void kill(Object o) {
		keepAlive.remove(o);
	}

	
}
