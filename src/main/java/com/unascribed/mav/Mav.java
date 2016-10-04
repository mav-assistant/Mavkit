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

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.swing.JOptionPane;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.unascribed.mav.render.InterfaceRenderer;

public final class Mav {
	private static Logger log = LoggerFactory.getLogger("Mav");
	
	@NonNull
	private final String version;
	@Nullable
	private final String googleApiKey;
	
	private final Display display;
	private final InterfaceRenderer interfaceRenderer;
	private final AudioProcessor audioProcessor;
	
	private final List<Object> keepAlive = Lists.newArrayList();
	
	private static final String[] initMessages = {
		"Mav {} at your service.",
		"Initializing Mav version {}",
		"Hello, World! This is Mav {}",
	};
	
	public Mav() {
		Thread.currentThread().setName("Main thread");
		
		version = System.getProperty("com.unascribed.mav.version", I18n.get("application.versionPlaceholder"));
		log.info(initMessages[(int)(Math.random()*initMessages.length)], version);
		String googleApiKey = Strings.emptyToNull(System.getProperty("com.unascribed.mav.googleApiKey"));
		if ("MY-API-KEY".equals(googleApiKey)) {
			googleApiKey = null;
		}
		this.googleApiKey = googleApiKey;
		
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			// Checker Framework doesn't understand a few things about JOptionPane
			@SuppressWarnings({"argument.type.incompatible", "call.invalid.ui"})
			public void uncaughtException(Thread t, Throwable e) {
				try {
					if (e instanceof Panic) {
						log.error("Panic!", e);
						JOptionPane.showMessageDialog(null, I18n.get("dialog.panic.title")+e.getLocalizedMessage(), I18n.get("window.title", version), JOptionPane.ERROR_MESSAGE, null);
						System.exit(2);
					} else {
						log.error("{} died", t.getName(), e);
						if ("Main thread".equals(t.getName())
								|| "UI thread".equals(t.getName())
								|| "Audio thread".equals(t.getName())) {
							JOptionPane.showMessageDialog(null, I18n.get("dialog.error.title")+Throwables.getStackTraceAsString(e), I18n.get("window.title", version), JOptionPane.ERROR_MESSAGE, null);
							System.exit(1);
						}
					}
				} catch (Exception ex) {
					Throwables.propagate(ex);
				}
			}
		});

		display = new Display();
		interfaceRenderer = new InterfaceRenderer();
		audioProcessor = new AudioProcessor();
	}
	
	
	// this method is the crossover from non-UI threads to UI threads, so ignore
	@SuppressWarnings("call.invalid.ui")
	public void start() {
		CountDownLatch init = new CountDownLatch(2);
		
		new Thread(() -> {
			display.start(this);
			interfaceRenderer.start(this);
			init.countDown();
			interfaceRenderer.run();
		}, "UI thread").start();
		
		new Thread(() -> {
			audioProcessor.start(this);
			init.countDown();
			audioProcessor.run();
		}, "Audio thread").start();
		
		try {
			init.await();
		} catch (InterruptedException e1) {
			throw Throwables.propagate(e1);
		}
	}
	
	public Display getDisplay() {
		return display;
	}
	
	public String getVersion() {
		return version;
	}

	@Nullable
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
