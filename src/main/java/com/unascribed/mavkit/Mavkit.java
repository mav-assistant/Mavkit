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

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.unascribed.mavkit.render.InterfaceRenderer;

public final class Mavkit {
	private static Logger log = LoggerFactory.getLogger("Mav");
	
	private final Display display;
	private final InterfaceRenderer interfaceRenderer;
	private final AudioProcessor audioProcessor;
	
	private final List<Object> keepAlive = Lists.newArrayList();
	
	public Mavkit() {
		Thread.currentThread().setName("Main thread");
		log.info("Starting Mavkit");
		
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			// Checker Framework doesn't understand a few things about JOptionPane
			@SuppressWarnings({"argument.type.incompatible", "call.invalid.ui"})
			public void uncaughtException(Thread t, Throwable e) {
				try {
					if (e instanceof Panic) {
						log.error("Panic!", e);
						setLaF();
						JOptionPane.showMessageDialog(null, I18n.get("dialog.panic.title")+e.getLocalizedMessage(), I18n.get("window.title"), JOptionPane.ERROR_MESSAGE, null);
						System.exit(2);
					} else {
						log.error("{} died", t.getName(), e);
						if ("Main thread".equals(t.getName())
								|| "UI thread".equals(t.getName())
								|| "Audio thread".equals(t.getName())) {
							setLaF();
							JOptionPane.showMessageDialog(null, I18n.get("dialog.error.title")+Throwables.getStackTraceAsString(e), I18n.get("window.title"), JOptionPane.ERROR_MESSAGE, null);
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
	
	private static void setLaF() {
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
	}
	
	public Display getDisplay() {
		return display;
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
