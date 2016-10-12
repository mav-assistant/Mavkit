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

import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.checkerframework.checker.i18n.qual.LocalizableKey;
import org.checkerframework.checker.i18n.qual.Localized;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.propkey.qual.PropertyKey;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

@SuppressWarnings("return.type.incompatible")
public final class I18n {
	private static final String BUNDLE_NAME = "com.unascribed.mavkit.lang";

	private static final List<ResourceBundle> LOCAL = Lists.newArrayList(ResourceBundle.getBundle(BUNDLE_NAME));
	private static final List<ResourceBundle> DEFAULT = Lists.newArrayList(ResourceBundle.getBundle(BUNDLE_NAME, Locale.ROOT));
	
	private static final Pattern TOKEN_PATTERN = Pattern.compile("\\{(.*?)\\}");

	private I18n() {}
	
	
	public static void merge(String bundleName) {
		LOCAL.add(0, ResourceBundle.getBundle(bundleName));
		DEFAULT.add(0, ResourceBundle.getBundle(bundleName, Locale.ROOT));
	}
	
	@NonNull
	@Localized
	public static String get(@LocalizableKey @PropertyKey String key, Object... args) {
		return format(key, LOCAL, args);
	}
	

	@NonNull
	public static String getDefault(@LocalizableKey @PropertyKey String key, Object... args) {
		return format(key, DEFAULT, args);
	}
	
	
	@NonNull
	@Localized
	// kind gets passed back into format, which expects a property key
	// if we're passing kind back in, we know it must be a property key
	// so ignore the error
	@SuppressWarnings("argument.type.incompatible")
	private static String format(@LocalizableKey @PropertyKey String key, List<ResourceBundle> bundles, Object... args) {
		if (key.isEmpty()) return "";
		String val = null;
		for (ResourceBundle rb : bundles) {
			try {
				val = rb.getString(key);
			} catch (MissingResourceException e) {}
		}
		if (val == null) {
			val = "<< cannot localize "+key+" >>";
		}
		
		Matcher m = TOKEN_PATTERN.matcher(val);
		// the Matcher API requires StringBuffer
		StringBuffer rtrn = new StringBuffer();
		while (m.find()) {
			String kind = m.group(1);
			if (kind == null) kind = "";
			Integer idx = Ints.tryParse(kind);
			Object replacement;
			if (idx != null) {
				if (idx == 0) {
					replacement = key;
				} else {
					if (idx > args.length) {
						replacement = "<< index "+idx+" is out of range >>";
					} else {
						replacement = args[idx-1];
					}
				}
			} else {
				replacement = format(kind, bundles, args);
			}
			m.appendReplacement(rtrn, String.valueOf(replacement).replace("\\", "\\\\").replace("$", "\\$"));
		}
		m.appendTail(rtrn);
		return rtrn.toString();
	}
}
