package com.unascribed.mavkit.internal.plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.unascribed.mavkit.Mavkit;
import com.unascribed.mavkit.plugin.Plugin;
import com.unascribed.mavkit.plugin.PluginDiscoverer;

public class SimplePluginDiscoverer implements PluginDiscoverer {
	private static final Logger log = LoggerFactory.getLogger("SimplePluginDiscoverer");
	private static final String PLUGIN_INTERNAL_NAME = Plugin.class.getName().replace('.', '/');
	
	@Override
	public List<Plugin> discover(Mavkit mav) {
		List<Plugin> list = Lists.newArrayList();
		try {
			Class<?> staticPluginBinder = Class.forName("com.unascribed.mavkit.StaticPluginBinder");
			Method m = staticPluginBinder.getMethod("getPlugins");
			list.addAll((List<Plugin>)m.invoke(null));
			log.info("Added {} plugins from the static plugin binder.", list.size());
		} catch (ClassNotFoundException e) {
			log.debug("No static plugin binder found.");
		} catch (Exception e) {
			log.warn("Error while attempting to call the static plugin binder, does it have a public static method named getPlugins returning a List<Plugin>?", e);
		}
		File dir = mav.getDirectories().getPluginHome();
		log.info("Scanning for plugins in {}", dir);
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				log.warn("{} is a directory, and cannot be loaded", f.getName());
				continue;
			}
			try (JarFile jf = new JarFile(f)) {
				URLClassLoader ucl = new URLClassLoader(new URL[] { f.toURI().toURL() }, SimplePluginDiscoverer.class.getClassLoader());
				log.debug("Scanning {} for plugins", f.getName());
				Enumeration<JarEntry> enumeration = jf.entries();
				while (enumeration.hasMoreElements()) {
					JarEntry je = enumeration.nextElement();
					InputStream in = jf.getInputStream(je);
					byte[] bys = ByteStreams.toByteArray(in);
					in.close();
					ClassReader cr;
					try {
						cr = new ClassReader(bys);
					} catch (Exception e) {
						continue;
					}
					ClassNode node = new ClassNode();
					cr.accept(node, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
					String actualName = node.name.replace('/', '.');
					for (String iface : (List<String>)node.interfaces) {
						if (PLUGIN_INTERNAL_NAME.equals(iface)) {
							try {
								Class<Plugin> clazz = (Class<Plugin>) Class.forName(actualName, true, ucl);
								Constructor<Plugin> cons = clazz.getConstructor();
								list.add(cons.newInstance());
								log.info("Found {}", clazz.getName());
								break;
							} catch (ClassNotFoundException e) {
								throw new AssertionError(e);
							} catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
								log.error("{} does not have a default public constructor", actualName);
							} catch (Exception e) {
								log.error("Unexpected error while trying to create {}", actualName, e);
							}
						}
					}
				}
			} catch (ZipException e) {
				log.warn("{} is corrupt or is not a jar file, and cannot be loaded", f.getName());
			} catch (IOException e) {
				log.warn("Error while trying to read {} as a jar", f.getName(), e);
			}
		}
		log.info("Found {} plugins", list.size());
		return list;
	}

}
